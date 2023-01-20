package org.cadnusdevs.sandroid.jwcongregationasignment.ui.screens

import android.Manifest
import android.annotation.SuppressLint
import android.content.ClipboardManager
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.preference.PreferenceManager
import android.view.View
import android.widget.Button
import android.widget.TableLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.res.ResourcesCompat
import org.cadnusdevs.sandroid.jwcongregationasignment.BuildConfig
import org.cadnusdevs.sandroid.jwcongregationasignment.R
import org.cadnusdevs.sandroid.jwcongregationasignment.models.TerritoryCard
import org.cadnusdevs.sandroid.jwcongregationasignment.repositories.DbContracts
import org.cadnusdevs.sandroid.jwcongregationasignment.repositories.DbContracts.territoryCardRelationship
import org.cadnusdevs.sandroid.jwcongregationasignment.service.GeoLocationService
import org.cadnusdevs.sandroid.jwcongregationasignment.ui.screens.adapters.BaseTableAdapter
import org.cadnusdevs.sandroid.jwcongregationasignment.ui.shared.BaseFragment
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

@Suppress("DEPRECATION")
class TerritoryCardFragment : BaseFragment(), LocationListener {
    private var userLocation: GeoPoint = GeoPoint(0,0)
    private lateinit var clipboard: ClipboardManager
    private lateinit var locationService: GeoLocationService
    private lateinit var card: TerritoryCard
    private lateinit var table: DirectionTable
    private lateinit var locationManager: LocationManager
    private var map: MapView? = null

    override fun getTemplate() = R.layout.fragment_territory_card

    override fun configureLayout(view: View?) {
        setMapView(view)
        setTableView(view)
        val tableNames = territoryCardRelationship.map { it.table.name }
    }

    private fun setTableView(view: View?) {
        table = DirectionTable(view,R.id.direction_table)
    }

    override fun setViewData() {
        card = TerritoryCard.card703A
        locationService = GeoLocationService()
        clipboard = requireActivity().getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    }

    override fun setEvents() {
        setInitialPosition()
        setMapMarks()
        setLocationUpdates()
        setDirectionsTable()
    }

    private fun setMapMarks() {
        if (card.directions.isNotEmpty()) {
            map!!.overlays.clear()
            card.directions.forEach { addMarker(it) }
        }
    }

    private fun setDirectionsTable() {
        table.setData(card.directions.asList()).addRows()
        table.addFooter(generatePastButton())
    }

    private fun generatePastButton(): Button {
        var btn = Button(requireActivity())
        btn.text = "Pegar de la Clipboard"
        btn.setOnClickListener{
            var directions = ArrayList<TerritoryCard.Direction>()
            val item = clipboard.primaryClip?.getItemAt(0)?.text
            if(item != null){
                val lines = item.toString()
                    .replace("_","")
                    .replace("Localidad: ","")
                    .replace("Territ. ", "")
                    .replace("\r","")
                    .split("\n")

                if(lines.size > 2) {
                    map!!.overlays.clear()
                    val amount = (lines.size + 1) / 2
                    val line2 = lines[1].split(" Nº: ")
                    val neighborhood = line2[0].map {
                        return@map if(it.isUpperCase()) " $it" else it
                    }.joinToString("")
                    val cardNumber  = line2[1]

                    for (i in 3..amount) {
                        var lineIndex = i-1
                        var streetName = lines[lineIndex]
                        var numbers = lines[lineIndex+amount-1].split(",").toTypedArray()

                        locationService.getLocationByAddress(streetName,numbers[0], neighborhood.split("-")[0]){ geoPoint ->
                            var location = geoPoint ?: userLocation
                            var direction = TerritoryCard.Direction(i-2, streetName, numbers, location.latitude , location.longitude)
                            directions.add(direction)
                            addMarker(direction)
                            if(directions.isNotEmpty()){
                                directions.sortBy { it.directionNumber }
                                card = TerritoryCard(cardNumber, neighborhood, directions.toTypedArray())
                                setDirectionsTable()
                                centerMapBy(location)
                            }
                        }
                    }
                }
            }
        }
        return btn
    }

    private fun setLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                this.requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this.requireActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                ),
                99
            )
            return
        }
        locationManager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0L,0F, this)
        var location = getLastLocation()
        location?.let {
            userLocation = GeoPoint(it.latitude, it.longitude)
            centerMapBy(userLocation)
            addMarker(TerritoryCard.Direction(0,"", arrayOf(),it.latitude,it.longitude), R.drawable.ic_add_brother)
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation(): Location? {
        val providers  = locationManager.getProviders(true)
        var bestLocation: Location? = null
        for (provider in providers) {
            val l: Location = locationManager.getLastKnownLocation(provider) ?: continue
            if (bestLocation == null || l.accuracy < bestLocation.accuracy) {
                bestLocation = l
            }
        }
        return bestLocation
    }

    private fun centerMapBy(startPoint: GeoPoint) {
        map?.controller?.animateTo(startPoint);
    }

    private fun setMapView(view: View?) {
        val ctx = view?.context;
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        Configuration.getInstance().userAgentValue = BuildConfig.APPLICATION_ID;
        map = q.find<MapView>(R.id.map)
        map?.setTileSource(TileSourceFactory.MAPNIK)
    }

    private fun setInitialPosition() {
        val mapController = map?.controller
        mapController?.setZoom(16)
    }

    private fun addMarker(direction: TerritoryCard.Direction, icon: Int? = R.drawable.ic_house_green) {
        val marker = Marker(map)
        marker.position = GeoPoint(direction.lat, direction.long)
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        if(icon != null) {
            val iconFromResources = ResourcesCompat.getDrawable(resources, icon, null)
            marker.icon = iconFromResources
        }
        map?.overlays?.add(marker)
        map?.invalidate()
    }

    override fun onLocationChanged(location: Location) {
        userLocation = GeoPoint(location.latitude, location.longitude)
        centerMapBy(userLocation)
    }

    class DirectionTable(parentView: View?,
                         tableId: Int
    ) : BaseTableAdapter<TerritoryCard.Direction>(parentView, tableId) {
        override fun onSave() {
        }

        override fun editForm(position: Int): View {
            return q.Text("TODO")
        }

        override fun headers() = arrayOf("#", "Calle", "Número(s)")

        override fun weekendRow(table: TableLayout?, it: TerritoryCard.Direction) {
            row(table, arrayOf(it.directionNumber.toString(), it.streetName, it.houseNumbers.joinToString(",")))
        }
    }
}
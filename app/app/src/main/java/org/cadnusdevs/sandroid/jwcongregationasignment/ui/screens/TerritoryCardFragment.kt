package org.cadnusdevs.sandroid.jwcongregationasignment.ui.screens

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.preference.PreferenceManager
import android.view.View
import androidx.core.app.ActivityCompat
import org.cadnusdevs.sandroid.jwcongregationasignment.BuildConfig
import org.cadnusdevs.sandroid.jwcongregationasignment.R
import org.cadnusdevs.sandroid.jwcongregationasignment.models.TerritoryCard
import org.cadnusdevs.sandroid.jwcongregationasignment.ui.shared.BaseFragment
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

@Suppress("DEPRECATION")
class TerritoryCardFragment : BaseFragment(), LocationListener {
    private lateinit var locationManager: LocationManager
    private var map: MapView? = null

    override fun getTemplate() = R.layout.fragment_territory_card

    override fun configureLayout(view: View?) {
        setMapView(view)
    }

    override fun setViewData() {

    }

    override fun setEvents() {
        setInitialPosition()
        setLocationUpdates()
    }

    private fun setLocationUpdates() {
        locationManager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (ActivityCompat.checkSelfPermission(
                this.requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this.requireActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            val result = ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                ),
                99
            )
            return
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0L,0F, this)
        val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)!!
        centerMapBy(location)
    }

    private fun centerMapBy(location: Location) {
        val startPoint = GeoPoint(location.latitude, location.longitude);
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
        val card = TerritoryCard.card703A
        val mapController = map?.controller
        mapController?.setZoom(14)

//        val startPoint = GeoPoint(card.directions[0].lat, card.directions[0].long);
//        mapController?.animateTo(startPoint);
        if (card.directions.isNotEmpty()) {
            map!!.overlays.clear()
            card.directions.forEach { addMarker(it) }
        }
    }

    private fun addMarker(direction: TerritoryCard.Direction) {
        val marker = Marker(map)
        marker.position = GeoPoint(direction.lat, direction.long)
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        map?.overlays?.add(marker)
        map?.invalidate()
    }

    override fun onLocationChanged(location: Location) {
        centerMapBy(location)
    }
}
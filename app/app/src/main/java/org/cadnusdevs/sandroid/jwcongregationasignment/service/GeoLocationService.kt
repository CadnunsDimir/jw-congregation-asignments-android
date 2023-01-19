package org.cadnusdevs.sandroid.jwcongregationasignment.service

import org.cadnusdevs.sandroid.jwcongregationasignment.models.TerritoryCard
import org.osmdroid.util.GeoPoint

class GeoLocationService {
    fun getLocationByAddress(street: String, houseNumber: String, neighborHood: String) : GeoPoint {
        val adress = "$street, $houseNumber - $neighborHood"
        val card = TerritoryCard.card703A
        return GeoPoint(card.directions[0].lat, card.directions[0].long)
    }
}
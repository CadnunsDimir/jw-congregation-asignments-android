package org.cadnusdevs.sandroid.jwcongregationasignment.models

class TerritoryCard (val cardNumber: String,val  neighborhood: String, val directions: Array<Direction>, var id: Long = 0 ) {
    class Direction(val directionNumber: Int, val streetName: String, val houseNumbers: Array<String>, val lat: Double, val long: Double, var id: Long = 0, var territoryCardId: Long = 0)

    fun updateDirectionsWithCardId() {
        directions.forEach { it.territoryCardId = id }
    }

    companion object {
        val card703A = TerritoryCard(
            "703A",
            "Vila Jacuí - Limoeiro",
            arrayOf(
                Direction(
                    // https://nominatim.openstreetmap.org/search?q=R.%20Trevo%20de%20Santa%20Maria%20253%20-%20Limoeiro&format=json&polygon=1&addressdetails=1
                1,"R. Trevo de Santa Maria",
                    arrayOf("1","2","3",),
                    -23.514648,-46.4625198),
                Direction(
                     2,"R. Dr. Jorge Assunção",
                    arrayOf("1","2","3"),
                    -23.5125073,-46.4634901)
            )
        )
    }
}
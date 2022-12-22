package org.cadnusdevs.sandroid.jwcongregationasignment.repositories

import org.cadnusdevs.sandroid.jwcongregationasignment.dbMock
import org.cadnusdevs.sandroid.jwcongregationasignment.models.Brother

class BrotherRepository {
    fun insert(brother: Brother) {
        dbMock.brothers.add(brother)
    }
    fun select(predicate: (Brother) -> Boolean): Brother? {
        return dbMock.brothers.find(predicate)
    }

    fun update(brother: Brother) {
        var brotherFromDb = this.select { x-> x.name == brother.name }
        brotherFromDb?.let {
            it.canBeUsher = brother.canBeUsher
            it.canBeMicrophone = brother.canBeMicrophone
            it.canBeComputer = brother.canBeComputer
            it.canBeSoundSystem = brother.canBeSoundSystem
        }
    }

    fun delete(brother: Brother) {
        dbMock.brothers.remove(this.select { x -> x.name == brother.name })
    }

    fun selectAll(): List<Brother> {
        return dbMock.brothers;
    }
}
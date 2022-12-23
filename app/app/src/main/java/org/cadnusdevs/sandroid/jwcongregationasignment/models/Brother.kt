package org.cadnusdevs.sandroid.jwcongregationasignment.models

class Brother(
    var id: Long,
    var name: String,
    var canBeUsher: Boolean,
    var canBeMicrophone: Boolean,
    var canBeComputer: Boolean,
    var canBeSoundSystem: Boolean,
) {
    fun satisfyRules(): Boolean {
        return name.length >= 3 && (canBeUsher || canBeMicrophone || canBeComputer || canBeSoundSystem)
    }
}

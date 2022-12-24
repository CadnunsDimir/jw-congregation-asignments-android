package org.cadnusdevs.sandroid.jwcongregationasignment.models

class SpinnerItem<T>(val item: T, private val toStringResolver: (item: T)-> String) {
    override fun toString(): String {
        return toStringResolver(item)
    }

    companion object {
        fun <T> list(itens: List<T>, toStringResolver: (item: T)-> String) : List<SpinnerItem<T>>{
            return itens.map {
                SpinnerItem(it, toStringResolver)
            }
        }
    }
}
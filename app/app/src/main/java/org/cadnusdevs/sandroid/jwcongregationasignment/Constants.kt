package org.cadnusdevs.sandroid.jwcongregationasignment

import org.cadnusdevs.sandroid.jwcongregationasignment.models.Brother

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
const val UNIQUE_BROTHER_NAME_KEY = "UNIQUE_BROTHER_NAME_KEY"

class dbMock {
    companion object {
        var brothers: ArrayList<Brother> = ArrayList()
    }
    init {
    }
}
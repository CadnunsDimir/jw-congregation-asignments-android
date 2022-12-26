package org.cadnusdevs.sandroid.jwcongregationasignment

import org.cadnusdevs.sandroid.jwcongregationasignment.models.Brother

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
const val UNIQUE_BROTHER_KEY = "UNIQUE_BROTHER_KEY"
const val BROTHER_ERROR_MSG = "Você deve preencher o nome com pelo menos 3 letras e atribuir pelo menos 1 designação"

val SPINNER_NO_OPTION_TEXT_PtBR = "Selecione um Opção"
class dbMock {
    companion object {
        var brothers: ArrayList<Brother> = ArrayList()
    }
    init {
    }
}
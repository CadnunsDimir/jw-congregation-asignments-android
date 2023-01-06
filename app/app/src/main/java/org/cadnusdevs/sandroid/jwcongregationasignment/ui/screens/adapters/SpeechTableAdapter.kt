package org.cadnusdevs.sandroid.jwcongregationasignment.ui.screens.adapters

import android.view.View
import android.widget.TableLayout
import org.cadnusdevs.sandroid.jwcongregationasignment.models.SpeechesArrangement

class SpeechTableAdapter(parentView: View?, tableId: Int) :
    BaseTableAdapter<SpeechesArrangement.Speech>(parentView, tableId) {
    override fun headers() = arrayOf("Fecha", "Tema", "Orador")
    override fun weekendRow(table: TableLayout?, it: SpeechesArrangement.Speech) {
        row(table, arrayOf(it.date.shortDate, it.title, it.speaker))
    }
}
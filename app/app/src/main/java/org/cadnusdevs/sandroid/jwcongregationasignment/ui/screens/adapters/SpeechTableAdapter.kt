package org.cadnusdevs.sandroid.jwcongregationasignment.ui.screens.adapters

import android.view.View
import android.widget.TableLayout
import org.cadnusdevs.sandroid.jwcongregationasignment.models.SpeechesArrangement
import org.cadnusdevs.sandroid.jwcongregationasignment.models.Weekend

class SpeechTableAdapter(parentView: View?, tableId: Int) :
    BaseTableAdapter<Weekend>(parentView, tableId) {
    override fun headers() = arrayOf("Fecha", "Tema", "Orador")
    override fun weekendRow(table: TableLayout?, it: Weekend) {
        row(table, arrayOf(it.date.shortDate, it.speech.title, it.speech.speaker))
    }

    override fun editForm(position: Int): View {
        return q.Text("TODO")
    }

    override fun onSave() {

    }
}
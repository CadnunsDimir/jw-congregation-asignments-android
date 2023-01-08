package org.cadnusdevs.sandroid.jwcongregationasignment.ui.screens.adapters

import android.view.View
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TableLayout
import org.cadnusdevs.sandroid.jwcongregationasignment.models.Brother
import org.cadnusdevs.sandroid.jwcongregationasignment.models.Weekend
import org.cadnusdevs.sandroid.jwcongregationasignment.repositories.WeekendRepository

class WeekendTableAdapter(parentView: View?, private val tableId: Int, val brothers: List<Brother>) :
    BaseTableAdapter<Weekend>(parentView, tableId) {
    private lateinit var readerSpinner: Spinner
    private lateinit var presidentSpinner: Spinner
    private val readerHeader = "Lector"
    private val presidentHeader = "Presidente"
    override fun headers() = arrayOf("Fecha", presidentHeader, readerHeader)

    override fun weekendRow(table: TableLayout?, it: Weekend) {
        row(table, arrayOf(it.date.shortDate,it.meetingPresident?.name ?:"", it.watchTowerReader?.name ?:""))
    }

    override fun editForm(position: Int): View {
        val layout = LinearLayout(parentView?.context)
        layout.orientation = LinearLayout.VERTICAL
        formPosition = position
        val weekend = getFormData()
        presidentSpinner = brotherSpinner(weekend.meetingPresident)
        readerSpinner = brotherSpinner(weekend.watchTowerReader)
        arrayOf(
            q.Text(presidentHeader),
            presidentSpinner,
            q.Text(readerHeader),
            readerSpinner
        ).forEach { layout.addView(it) }
        return layout
    }

    private fun brotherSpinner(brother: Brother?): Spinner {
        val spinner = Spinner(parentView?.context)
        q.setBrotherSpinnerItems(spinner, brothers) { b -> b.name }
        q.setSelectedBrother(spinner, brothers, brother)
        return spinner
    }

    override fun onSave() {
        val weekendRepository = WeekendRepository()
        val formData = getFormData()
        val weekend = weekendRepository.getFromDate(formData.date)
        weekend.meetingPresident = getValueOrNull(presidentSpinner)
        weekend.watchTowerReader = getValueOrNull(readerSpinner)
        setFormData(weekend)
        weekendRepository.update(weekend)
        setData(data).addRows()
    }

    private fun getValueOrNull(spinner: Spinner): Brother? {
        var brother = q.getSpinnerSelectedItem<Brother>(spinner)
        return if(brother.id > 0) brother else null
    }
}
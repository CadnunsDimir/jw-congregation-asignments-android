package org.cadnusdevs.sandroid.jwcongregationasignment.ui.screens.adapters

import android.text.InputType
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TableLayout
import org.cadnusdevs.sandroid.jwcongregationasignment.models.Brother
import org.cadnusdevs.sandroid.jwcongregationasignment.models.SpeechesArrangement
import org.cadnusdevs.sandroid.jwcongregationasignment.models.Weekend
import org.cadnusdevs.sandroid.jwcongregationasignment.repositories.WeekendRepository

class SpeechTableAdapter(parentView: View?, tableId: Int, val brothers: List<Brother>) :
    BaseTableAdapter<Weekend>(parentView, tableId) {
    private lateinit var speakerEdit: EditText
    private lateinit var titleEdit: EditText

    override fun headers() = arrayOf("Fecha", "Tema", "Orador")
    override fun weekendRow(table: TableLayout?, it: Weekend) {
        row(table, arrayOf(it.date.shortDate, it.speech.title, it.speech.speaker))
    }

    override fun editForm(position: Int): View {
        val layout = LinearLayout(parentView?.context)
        layout.orientation = LinearLayout.VERTICAL
        formPosition = position
        val weekend = getFormData()
        titleEdit = q.editText(weekend.speech.title)
        titleEdit.inputType = InputType.TYPE_TEXT_FLAG_CAP_WORDS
        speakerEdit = q.editText(weekend.speech.speaker)
        speakerEdit.inputType = InputType.TYPE_TEXT_FLAG_CAP_WORDS
        arrayOf(
            q.Text(headers()[1]),
            titleEdit,
            q.Text(headers()[2]),
            speakerEdit
        ).forEach { layout.addView(it) }
        return layout
    }

    override fun onSave() {
        val weekendRepository = WeekendRepository(parentView?.context!!, brothers)
        val formData = getFormData()
        val weekend = weekendRepository.getFromDate(formData.date)!!
        weekend.speech.title = titleEdit.text.toString()
        weekend.speech.speaker = speakerEdit.text.toString()
        setFormData(weekend)
        weekendRepository.update(weekend)
        setData(data).addRows()
    }
}
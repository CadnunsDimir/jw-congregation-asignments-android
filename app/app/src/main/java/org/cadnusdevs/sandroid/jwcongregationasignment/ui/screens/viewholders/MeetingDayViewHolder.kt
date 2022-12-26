package org.cadnusdevs.sandroid.jwcongregationasignment.ui.screens.viewholders;

import android.app.DatePickerDialog
import android.view.View;
import android.widget.Spinner
import android.widget.TextView
import org.cadnusdevs.sandroid.jwcongregationasignment.R
import org.cadnusdevs.sandroid.jwcongregationasignment.SPINNER_NO_OPTION_TEXT_PtBR
import org.cadnusdevs.sandroid.jwcongregationasignment.models.Brother
import org.cadnusdevs.sandroid.jwcongregationasignment.ui.screens.QueryViews
import java.util.Calendar

class MeetingDayViewHolder private constructor(
    val date: TextView,
    val usherA: Spinner,
    val usherB: Spinner,
    val micA: Spinner,
    val micB: Spinner,
    val computer: Spinner,
    val soundSystem: Spinner
) {
    private lateinit var q: QueryViews

    var brothers: List<Brother> = ArrayList()
        set(value) {
            field = value
            fillAllSpinners()
        }

    private fun fillAllSpinners() {
        val spinners = arrayOf(this.usherA,usherB, micA, micB, computer, soundSystem)
        spinners.forEach { spinner ->
            val items = generateSpinnerItems(brothers)
            spinner?.let {
                    spinner -> q.setSpinnerItems(spinner, items) { it.name }
            }
        }
    }

    private fun generateSpinnerItems(brothers: List<Brother>): List<Brother> {
        val options = ArrayList<Brother>()
        val noOption = Brother(0,SPINNER_NO_OPTION_TEXT_PtBR, false, false, false, false)
        options.add(noOption)
        options.addAll(brothers)
        return options
    }

    companion object {
        fun hold(view: View): MeetingDayViewHolder {
            val q = QueryViews(view);
            val date = q.find<TextView>(R.id.textViewMeetingDate)
            val usherA = q.find<Spinner>(R.id.spinnerUsherA)
            val usherB = q.find<Spinner>(R.id.spinnerUsherB)
            val micA = q.find<Spinner>(R.id.spinnerMicA)
            val micB = q.find<Spinner>(R.id.spinnerMicB)
            val computer = q.find<Spinner>(R.id.spinnerComputer)
            val soundSystem = q.find<Spinner>(R.id.spinnerSoundSystem)
            val holder = MeetingDayViewHolder(date!!,usherA!!,usherB!!,micA!!,micB!!,computer!!,soundSystem!!)
            holder.q = q
            holder.date.setOnClickListener { editText ->
                val setListener = DatePickerDialog.OnDateSetListener { _, year, month, day ->
                    val dayString = if(day < 10) "0$day" else day
                    val monthInt = month+1
                    val monthString = if(monthInt < 10) "0$monthInt" else monthInt
                    holder.date.text = "$dayString/$monthString/$year"
                }
                val date = Calendar.getInstance()
                var year = date.get(Calendar.YEAR)
                var month = date.get(Calendar.MONTH)
                var day = date.get(Calendar.DAY_OF_MONTH)
                if(holder.date.text != null && holder.date.text.length == 10) {
                    val dateArray = holder.date.text.split("/")
                    day = dateArray[0].toInt()
                    month = dateArray[1].toInt()-1
                    year = dateArray[2].toInt()
                }
                val datePicker = DatePickerDialog(editText.context,setListener,year, month, day)
                datePicker.show()
            }
            return holder;
        }
    }
}

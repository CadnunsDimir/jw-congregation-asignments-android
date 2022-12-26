package org.cadnusdevs.sandroid.jwcongregationasignment.ui.screens.viewholders;

import android.app.Activity
import android.view.View;
import android.widget.EditText
import android.widget.Spinner
import org.cadnusdevs.sandroid.jwcongregationasignment.R
import org.cadnusdevs.sandroid.jwcongregationasignment.SPINNER_NO_OPTION_TEXT_PtBR
import org.cadnusdevs.sandroid.jwcongregationasignment.models.Brother
import org.cadnusdevs.sandroid.jwcongregationasignment.ui.screens.QueryViews

class MeetingDayViewHolder private constructor(
    val date: EditText?,
    val usherA: Spinner?,
    val usherB: Spinner?,
    val micA: Spinner?,
    val micB: Spinner?,
    val computer: Spinner?,
    val soundSystem: Spinner?
) {
    private lateinit var activity: Activity
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
                    spinner -> q.setSpinnerItems(activity, spinner, items) { it.name }
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
        fun hold(activity: Activity, view: View): MeetingDayViewHolder {
            val q = QueryViews(view);
            val date = q.find<EditText>(R.id.editTextMeetingDate)
            val usherA = q.find<Spinner>(R.id.spinnerUsherA)
            val usherB = q.find<Spinner>(R.id.spinnerUsherB)
            val micA = q.find<Spinner>(R.id.spinnerMicA)
            val micB = q.find<Spinner>(R.id.spinnerMicB)
            val computer = q.find<Spinner>(R.id.spinnerComputer)
            val soundSystem = q.find<Spinner>(R.id.spinnerSoundSystem)
            val holder = MeetingDayViewHolder(date,usherA,usherB,micA,micB,computer,soundSystem)
            holder.q = q
            holder.activity = activity
            return holder;
        }
    }
}

package org.cadnusdevs.sandroid.jwcongregationasignment.ui.screens.viewholders;

import android.app.Activity
import android.view.View;
import android.widget.EditText
import android.widget.Spinner
import org.cadnusdevs.sandroid.jwcongregationasignment.R
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
            fillAllSpinners()
            field = value
        }

    private fun fillAllSpinners() {
        val spinners = arrayOf(this.usherA,usherB, micA, micB, computer, soundSystem)
        spinners.forEach { spinner ->
            spinner?.let {
                    spinner -> q.setSpinnerItems(activity, spinner, brothers) { it.name }
            }
        }
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

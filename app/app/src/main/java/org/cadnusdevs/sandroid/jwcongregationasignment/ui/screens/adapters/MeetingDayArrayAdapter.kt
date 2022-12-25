package org.cadnusdevs.sandroid.jwcongregationasignment.ui.screens.adapters

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import org.cadnusdevs.sandroid.jwcongregationasignment.R
import org.cadnusdevs.sandroid.jwcongregationasignment.models.Brother
import org.cadnusdevs.sandroid.jwcongregationasignment.models.MeetingDay
import org.cadnusdevs.sandroid.jwcongregationasignment.ui.screens.viewholders.MeetingDayViewHolder

class MeetingDayArrayAdapter(private val activity: Activity, private val meetings: List<MeetingDay>, private val brothers: List<Brother>)
    : ArrayAdapter<MeetingDay>(activity, 0,meetings) {

    private lateinit var holder: MeetingDayViewHolder

    override fun getCount(): Int = meetings.size

    override fun getItem(position: Int): MeetingDay = meetings[position]

    override fun getItemId(position: Int): Long = 0

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(activity).inflate(R.layout.meeting_day_list_item, parent, false)
        this.holder = MeetingDayViewHolder.hold(activity, view)
        this.holder.brothers = this.brothers
        return view
    }
}
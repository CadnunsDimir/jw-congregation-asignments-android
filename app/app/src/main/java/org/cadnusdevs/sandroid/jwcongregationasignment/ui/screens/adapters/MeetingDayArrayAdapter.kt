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

class MeetingDayArrayAdapter(private val activity: Activity, private val monthYear: String, private val meetings: List<MeetingDay>, private val brothers: List<Brother>)
    : ArrayAdapter<MeetingDay>(activity, 0,meetings) {

    private var onChangeListener: OnChange? = null
//    val itens: ArrayList<MeetingDayViewHolder?> = ArrayList(this.meetings.map { null })

    override fun getCount(): Int = meetings.size

    override fun getItem(position: Int): MeetingDay = meetings[position]

    override fun getItemId(position: Int): Long = 0

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(activity).inflate(R.layout.meeting_day_list_item, parent, false)

        val holder = MeetingDayViewHolder.hold(view)
        holder.brothers = this.brothers
        holder.setValue(getItem(position))
        holder.setOnChange{
            var meeting = holder.toModel(monthYear)
            onChangeListener?.onChange(meeting)
        }
        return view
    }

    interface OnChange{
        fun onChange(meeting: MeetingDay)
    }

    fun setOnChange(listener: OnChange) {
        this.onChangeListener = listener
    }
}
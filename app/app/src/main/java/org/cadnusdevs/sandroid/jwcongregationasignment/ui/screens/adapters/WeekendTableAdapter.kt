package org.cadnusdevs.sandroid.jwcongregationasignment.ui.screens.adapters

import android.graphics.Color
import android.view.View
import android.widget.TableLayout
import android.widget.TableRow
import org.cadnusdevs.sandroid.jwcongregationasignment.DateUtils.WeekDay
import org.cadnusdevs.sandroid.jwcongregationasignment.models.MeetingDay
import org.cadnusdevs.sandroid.jwcongregationasignment.models.Weekend
import org.cadnusdevs.sandroid.jwcongregationasignment.ui.screens.QueryViews

class WeekendTableAdapter(parentView: View?, private val tableId: Int) :
    BaseTableAdapter<Weekend>(parentView, tableId) {
    override fun headers() = arrayOf("Fecha", "Presidente", "Lector")
    fun setDataFromMeetingDayList(monthNumber: Int, meetings: List<MeetingDay>): WeekendTableAdapter{
        setData(Weekend.listFrom(meetings.filter { meetingDay -> WeekDay.weekEndDays.contains(meetingDay.date.weekDay) && meetingDay.month == monthNumber }))
        return this
    }

    override fun weekendRow(table: TableLayout?, it: Weekend) {
        row(table, arrayOf(it.date.shortDate,it.meetingPresident?.name ?:"", it.watchTowerReader?.name ?:""))
    }
}
package org.cadnusdevs.sandroid.jwcongregationasignment.ui.screens.adapters

import android.view.View
import android.widget.TableLayout
import org.cadnusdevs.sandroid.jwcongregationasignment.DateUtils.WeekDay
import org.cadnusdevs.sandroid.jwcongregationasignment.models.MeetingDay
import org.cadnusdevs.sandroid.jwcongregationasignment.models.Weekend
import org.cadnusdevs.sandroid.jwcongregationasignment.ui.screens.QueryViews

class WeekendTableAdapter(parentView: View?, private val tableId: Int) {
    private val cellPadding = 10
    private val readerHeader = "Lector"
    private val presidentHeader = "Presidente"
    private val q = QueryViews(parentView!!)
    private val tablePadding = 50
    private val dateHeader = "Fecha"

    fun addRows(monthNumber: Int, meetings: List<MeetingDay>) : WeekendTableAdapter{
        val table = q.find<TableLayout>(tableId)
        table?.setPadding(tablePadding,tablePadding,tablePadding,tablePadding)
        table?.isStretchAllColumns = true
        val list = Weekend.listFrom(meetings.filter { meetingDay -> WeekDay.weekEndDays.contains(meetingDay.date.weekDay) && meetingDay.month == monthNumber })
        header(table)
        list.forEach {
            weekendRow(table, it)
        }
        return this
    }

    private fun row(table: TableLayout?, cells: Array<View?>) {
        val row = q.tableRow()
        table?.addView(row)
        cells.forEach {
            row.addView(it)
            it?.setPadding(cellPadding, cellPadding, cellPadding, cellPadding)
        }
    }

    private fun header(table: TableLayout?) {
        row(table, arrayOf(q.boldText(dateHeader), q.boldText(presidentHeader), q.boldText(readerHeader)))
    }

    private fun weekendRow(table: TableLayout?, it: Weekend) {
        row(table, arrayOf(q.Text(it.date.shortDate),q.Text(it.meetingPresident?.name ?:""), q.Text(it.watchTowerReader?.name ?:"")))
    }
}
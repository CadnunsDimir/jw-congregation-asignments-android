package org.cadnusdevs.sandroid.jwcongregationasignment.ui.screens.adapters

import android.graphics.Color
import android.view.View
import android.widget.TableLayout
import android.widget.TableRow
import org.cadnusdevs.sandroid.jwcongregationasignment.DateUtils
import org.cadnusdevs.sandroid.jwcongregationasignment.models.MeetingDay
import org.cadnusdevs.sandroid.jwcongregationasignment.models.Weekend
import org.cadnusdevs.sandroid.jwcongregationasignment.ui.screens.QueryViews

abstract class BaseTableAdapter<T>(parentView: View?, private val tableId: Int) {
    private var list: List<T> = ArrayList()
//    private var headers = arrayOf<String>()

    private var indexLine: Int = 1
    private val cellPadding = 10
    private val q = QueryViews(parentView!!)
    private val tablePadding = 20
    private val tableBackground = "#555555"

    fun setData(list: List<T>) : BaseTableAdapter<T>{
        this.list = list
        return this
    }

    fun addRows() : BaseTableAdapter<T>{
        val table = q.find<TableLayout>(tableId)
        table?.setPadding(tablePadding,tablePadding,tablePadding,tablePadding)
        table?.isStretchAllColumns = true
        table?.setBackgroundColor(Color.parseColor(tableBackground))
        header(table)
        list.forEach {
            weekendRow(table, it)
        }
        indexLine = 0
        return this
    }

    fun row(table: TableLayout?, cells: Array<String>, isBold: Boolean = false) {
        val row = coloredRow()
        table?.addView(row)
        cells.forEach {
            val cell = if(isBold) q.boldText(it) else q.Text(it)
            row.addView(cell)
            cell?.setPadding(cellPadding, cellPadding, cellPadding, cellPadding)
        }
    }

    private fun coloredRow(): TableRow {
        val row = q.tableRow()
        q.setRowColor(row, indexLine)
        ++indexLine
        return row
    }

    private fun header(table: TableLayout?) {
        row(table, this.headers(), isBold = true)
    }

    abstract fun headers(): Array<String>

    abstract fun weekendRow(table: TableLayout?, it: T)
}
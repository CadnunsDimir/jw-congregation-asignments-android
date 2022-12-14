package org.cadnusdevs.sandroid.jwcongregationasignment.ui.screens

import android.app.Activity
import android.graphics.Color
import android.widget.TableLayout
import android.widget.TableRow
import androidx.core.view.setPadding

class StatsDialog {
    companion object{
        private val textSize = 14F

        fun open(q: QueryViews, activity:Activity, brotherVersusDates: HashMap<String, HashMap<Int, String>>, tableRowDates: String){
            val table = TableLayout(activity)
            table.setPadding(25)
            table?.isStretchAllColumns = true
            val header = brotherVersusDates[tableRowDates]
            var indexLine = 1

            addRow(q, activity, tableRowDates, table, header, indexLine)

            brotherVersusDates.forEach { (brother, hashmap) ->
                run {
                    if(brother != tableRowDates) {
                        ++indexLine
                        addRow(q, activity, brother, table, hashmap, indexLine)
                    }
                }
            }
            q.openDialog(table)
        }

        private fun addRow(q: QueryViews, activity:Activity, headerTitle: String, table: TableLayout, header: HashMap<Int, String>?, indexLine: Int) {
            val row = TableRow(activity)
            row.setPadding(10,5,10,5)
            val titleView = q.Text(headerTitle, textSize)
            titleView.setPadding(10,5,10,5)
            row.addView(titleView)
            q.setRowColor(row, indexLine)
            header?.forEach { (_, assignment) ->
                val text = q.Text(assignment, textSize)
                row.addView(text)
                text.setPadding(10,5,10,5)
            }
            table.addView(row)
        }
    }
}
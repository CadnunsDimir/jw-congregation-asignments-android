package org.cadnusdevs.sandroid.jwcongregationasignment.ui.screens.adapters

import android.app.Dialog
import android.graphics.Color
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TableLayout
import android.widget.TableRow
import androidx.core.view.setPadding
import org.cadnusdevs.sandroid.jwcongregationasignment.models.Weekend
import org.cadnusdevs.sandroid.jwcongregationasignment.ui.screens.QueryViews

abstract class BaseTableAdapter<T>(val parentView: View?, private val tableId: Int) {
    private val saveButtonText = "Salvar"
    private var dialog: Dialog? = null
    private val cancelButtonText = "Cancelar"
    private var list: ArrayList<T> = ArrayList()
    private var rows: ArrayList<TableRow> = ArrayList()
    private var indexLine: Int = 1
    private val cellPadding = 10
    val q = QueryViews(parentView!!)
    private val tablePadding = 20
    private val tableBackground = "#555555"
    var formPosition: Int = -1

    fun setData(list: List<T>) : BaseTableAdapter<T>{
        this.list = ArrayList(list)
        return this
    }

    val data: ArrayList<T>
    get() = list

    fun addRows() : BaseTableAdapter<T>{
        val table = q.find<TableLayout>(tableId)
        table?.removeAllViews()
        rows.clear()
        table?.setPadding(tablePadding,tablePadding,tablePadding,tablePadding)
        table?.isStretchAllColumns = true
        table?.setBackgroundColor(Color.parseColor(tableBackground))
        header(table)
        list.forEach {
            weekendRow(table, it)
        }
        indexLine = 1
        return this
    }

    fun row(table: TableLayout?, cells: Array<String>, isBold: Boolean = false) {
        val row = coloredRow()
        rows.add(row)
        table?.addView(row)
        cells.forEach {
            val cell = if(isBold) q.boldText(it) else q.Text(it)
            row.addView(cell)
            cell?.setPadding(cellPadding, cellPadding, cellPadding, cellPadding)
        }
        val listener = View.OnClickListener {
            val position = rows.indexOf(it)-1
            dialog = q.openDialog(completeForm(position))
        }
        row.setOnClickListener(listener)
    }

    private fun completeForm(position: Int): View {
        val mainLayout = LinearLayout(parentView?.context)
        mainLayout.orientation = LinearLayout.VERTICAL
        mainLayout.setPadding(tablePadding)
        var buttons = formButtons()
        mainLayout.addView(editForm(position))
        mainLayout.addView(buttons)
        return mainLayout
    }

    private fun formButtons(): View? {
        val buttons = LinearLayout(parentView?.context)
        buttons.orientation = LinearLayout.HORIZONTAL
        val cancel = Button(parentView?.context)
        cancel.text = cancelButtonText
        cancel.setOnClickListener{
            dialog?.dismiss()
            dialog = null
        }
        buttons.addView(cancel)

        val save = Button(parentView?.context)
        save.text = saveButtonText
        save.setOnClickListener{
            this.onSave()
            dialog?.dismiss()
            dialog = null
        }
        buttons.addView(save)
        return buttons
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

    fun setFormData(item: T) {
        data[formPosition] = item
    }

    fun getFormData() = data[formPosition]

    abstract fun onSave()
    abstract fun editForm(position: Int): View
    abstract fun headers(): Array<String>
    abstract fun weekendRow(table: TableLayout?, it: T)
}
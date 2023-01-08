package org.cadnusdevs.sandroid.jwcongregationasignment.ui.screens

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.Typeface
import android.text.Editable
import android.text.SpannableString
import android.text.style.StyleSpan
import android.util.TypedValue
import android.view.View
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import org.cadnusdevs.sandroid.jwcongregationasignment.R
import org.cadnusdevs.sandroid.jwcongregationasignment.SPINNER_NO_OPTION_TEXT_PtBR
import org.cadnusdevs.sandroid.jwcongregationasignment.models.Brother
import org.cadnusdevs.sandroid.jwcongregationasignment.models.SpinnerItem


@Suppress("UNCHECKED_CAST")
class QueryViews(private var _view: View) {

    fun <T> find(id: Int): T? { return this._view?.findViewById(id) }

    fun getEditTextValue(componentId: Int): String {
        return find<EditText>(componentId)?.text.toString()
    }

    fun checkBoxValue(id: Int): Boolean {
        return find<CheckBox>(id)?.isChecked?: false
    }

    fun setText(id: Int, text: String) {
        find<TextView>(id)?.text = text
    }

    fun makeReadOnly(id: Int) {
        find<View>(id)?.isEnabled = false
    }

    fun setBool(id: Int, value: Boolean) {
        find<CheckBox>(id)?.isChecked = value
    }
    fun onClick(componentId: Int, action: View.OnClickListener) {
        val button = find<View>(componentId)
        button?.setOnClickListener(action)
    }

    fun showIf(id: Int, value: Boolean) {
        find<View>(id)?.visibility = if(value) View.VISIBLE else View.GONE
    }

    fun openFragment(fragment: Fragment, isFromMainActivity: Boolean = false) {
        if(_view.context is FragmentActivity){
            val supportFragmentManager = (_view.context as FragmentActivity).supportFragmentManager
            val currentFragment = if(supportFragmentManager.fragments.size > 0) supportFragmentManager.fragments.last().javaClass else Object().javaClass
            val newFragment = fragment.javaClass
            if(currentFragment != newFragment) {
                val transaction = supportFragmentManager.beginTransaction()
                    .replace(R.id.container, fragment)
                if(isFromMainActivity){
                    transaction.commitNow()
                }else{
                    transaction.addToBackStack(null).commit()
                }
            }
        }
    }

    fun setColor(id: Int, hexColor: String) {
        find<View>(id)?.setBackgroundColor(Color.parseColor(hexColor))
    }

    fun  <T> setSpinnerItems(spinner:Spinner, items: List<T>, toStringResolver: (item: T)-> String) {

        val adapter = ArrayAdapter(spinner.context, android.R.layout.simple_spinner_item, SpinnerItem.list(items, toStringResolver))
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }

    fun <T> getSpinnerSelectedItem(spinner: Spinner): T {
        return (spinner.selectedItem as SpinnerItem<T>).item
    }

    fun openDialog(content: View): Dialog {
        _view.context ?.let {
            val builder = AlertDialog.Builder(it)
            var dialog = builder.setView(content).create()
            dialog.show()
            return dialog
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    fun Text(text: String = "", textSize: Float = 18F): TextView {
        val textView = TextView(_view.context)
        textView.text = text
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize)
        return textView
    }

    fun boldText(text: String): View? {
        val view = Text()
        val spanString = SpannableString(text)
        spanString.setSpan(StyleSpan(Typeface.BOLD), 0, spanString.length, 0)
        view.text = spanString
        return view
    }

    fun tableRow() = TableRow(_view.context)
    fun setRowColor(row: TableRow, indexLine: Int) {
        if(indexLine % 2 == 1){
            row.setBackgroundColor(Color.parseColor("#333333"))
        }
    }

    fun setMargin(table: TableLayout?, tableMargin: Int) {
        val lp = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )
        lp.setMargins(tableMargin, tableMargin, tableMargin, tableMargin)
        table?.layoutParams = lp
    }
    fun setSelectedBrother(spinner: Spinner, brothers: List<Brother>, brother: Brother?) {
        val position = brothers.indexOf(brothers.firstOrNull { x -> x.name == brother?.name })
        if(position >= 0)
            spinner.setSelection(position + 1)
    }
    fun setBrotherSpinnerItems(spinner: Spinner, brothers: List<Brother>, toStringResolver: (item: Brother)-> String){
        val items = generateSpinnerItems(brothers)
        setSpinnerItems(spinner,items,toStringResolver)
    }
    private fun generateSpinnerItems(brothers: List<Brother>): List<Brother> {
        val options = ArrayList<Brother>()
        val noOption = Brother(0, SPINNER_NO_OPTION_TEXT_PtBR,
            canBeUsher = false,
            canBeMicrophone = false,
            canBeComputer = false,
            canBeSoundSystem = false
        )
        options.add(noOption)
        options.addAll(brothers)
        return options
    }

    fun editText(text: String): EditText {
        val component = EditText(_view.context)
        component.setText(text)
        return component
    }
}
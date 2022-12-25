package org.cadnusdevs.sandroid.jwcongregationasignment.ui.screens

import android.app.Activity
import android.view.View
import android.widget.*
import org.cadnusdevs.sandroid.jwcongregationasignment.models.SpinnerItem

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

    fun  <T> setSpinnerItems(activity: Activity, spinner:Spinner, items: List<T>, toStringResolver: (item: T)-> String) {
        val adapter = ArrayAdapter(activity, android.R.layout.simple_spinner_item, SpinnerItem.list(items, toStringResolver))
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
    }

    fun showIf(id: Int, value: Boolean) {
        find<View>(id)?.visibility = if(value) View.VISIBLE else View.GONE
    }
}
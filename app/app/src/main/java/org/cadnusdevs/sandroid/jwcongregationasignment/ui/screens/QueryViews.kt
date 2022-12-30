package org.cadnusdevs.sandroid.jwcongregationasignment.ui.screens

import android.app.Activity
import android.graphics.Color
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import org.cadnusdevs.sandroid.jwcongregationasignment.R
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

    fun  <T> setSpinnerItems(spinner:Spinner, items: List<T>, toStringResolver: (item: T)-> String) {
        val adapter = ArrayAdapter(spinner.context, android.R.layout.simple_spinner_item, SpinnerItem.list(items, toStringResolver))
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
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

    fun <T> getSpinnerSelectedItem(spinner: Spinner): T {
        return (spinner.selectedItem as SpinnerItem<T>).item
    }
}
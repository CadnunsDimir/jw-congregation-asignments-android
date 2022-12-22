package org.cadnusdevs.sandroid.jwcongregationasignment.ui.shared

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import org.cadnusdevs.sandroid.jwcongregationasignment.R

abstract class BaseFragment : Fragment(){

    private var _view: View? = null;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setViewData()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        this._view = inflater.inflate(this.getTemplate(), container, false)
        configureLayoutManager(this._view)
        setEvents()
        return this._view
    }

    fun onClick(componentId: Int, action: View.OnClickListener) {
        val button = _view?.findViewById(componentId) as View
        button.setOnClickListener(action)
    }

    fun openFragment(fragment: Fragment) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.container, fragment)
            .addToBackStack(null)
            .commit()
    }

    fun back() {
        requireActivity().supportFragmentManager.popBackStack()
    }

    abstract fun getTemplate(): Int
    abstract fun configureLayoutManager(view: View?)
    abstract fun setViewData()
    abstract fun setEvents()
    fun getEditTextValue(componentId: Int): String {
        return this._view?.findViewById<EditText>(componentId)?.text.toString()
    }

    fun checkBoxValue(id: Int): Boolean {
        return this._view?.findViewById<CheckBox>(id)?.isChecked?: false
    }

    fun setText(id: Int, text: String) {
        this._view?.findViewById<TextView>(id)?.text = text
    }

    fun makeReadOnly(id: Int) {
        this._view?.findViewById<View>(id)?.isEnabled = false
    }

    fun setBool(id: Int, value: Boolean) {
        this._view?.findViewById<CheckBox>(id)?.isChecked = value
    }
}
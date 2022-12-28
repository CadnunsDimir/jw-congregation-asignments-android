package org.cadnusdevs.sandroid.jwcongregationasignment.ui.shared

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import org.cadnusdevs.sandroid.jwcongregationasignment.R
import org.cadnusdevs.sandroid.jwcongregationasignment.ui.screens.QueryViews


abstract class BaseFragment : Fragment(){
    lateinit var q: QueryViews

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setViewData()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(this.getTemplate(), container, false)
        q = QueryViews(view)
        configureLayout(view)
        setEvents()
        return view
    }

    fun openFragment(fragment: Fragment) {
        q.openFragment(fragment)
    }

    fun goBack() {
        requireActivity().supportFragmentManager.popBackStack()
    }

    abstract fun getTemplate(): Int
    abstract fun configureLayout(view: View?)
    abstract fun setViewData()
    abstract fun setEvents()

    fun showToast(message: String) {
        Toast.makeText(
            activity, message,
            Toast.LENGTH_LONG
        ).show()
    }
}
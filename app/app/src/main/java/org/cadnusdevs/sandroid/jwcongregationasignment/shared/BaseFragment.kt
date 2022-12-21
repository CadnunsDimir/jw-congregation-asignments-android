package org.cadnusdevs.sandroid.jwcongregationasignment.shared

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.setViewData()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(this.getTemplate(), container, false)
        setEvents(view);
        return view
    }
    abstract fun getTemplate(): Int
    abstract fun setViewData()
    abstract fun setEvents(view: View?)
}
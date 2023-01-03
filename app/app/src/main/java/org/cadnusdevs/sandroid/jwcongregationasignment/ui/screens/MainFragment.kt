package org.cadnusdevs.sandroid.jwcongregationasignment.ui.screens

import android.view.View
import androidx.lifecycle.ViewModelProvider
import org.cadnusdevs.sandroid.jwcongregationasignment.R
import org.cadnusdevs.sandroid.jwcongregationasignment.models.MainViewModel
import org.cadnusdevs.sandroid.jwcongregationasignment.ui.shared.BaseFragment


class MainFragment : BaseFragment() {

    private lateinit var viewModel: MainViewModel

    override fun getTemplate() = R.layout.fragment_main
    override fun configureLayout(view: View?) {
    }

    override fun setViewData() {
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
    }
    override fun setEvents() {

    }

    companion object {
        fun newInstance() = MainFragment()
    }
}
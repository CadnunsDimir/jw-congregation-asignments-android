package org.cadnusdevs.sandroid.jwcongregationasignment.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import org.cadnusdevs.sandroid.jwcongregationasignment.R
import org.cadnusdevs.sandroid.jwcongregationasignment.shared.BaseFragment


class MainFragment : BaseFragment() {

    private lateinit var viewModel: MainViewModel

    override fun getTemplate() = R.layout.fragment_main
    override fun setViewData() {
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
    }
    override fun setEvents() {
        this.onClick (R.id.new_brother_screen_button) {
            this.openFragment(NewBrotherFragment.newInstance("atr1", "atr2"))
        }
    }

    companion object {
        fun newInstance() = MainFragment()
    }
}
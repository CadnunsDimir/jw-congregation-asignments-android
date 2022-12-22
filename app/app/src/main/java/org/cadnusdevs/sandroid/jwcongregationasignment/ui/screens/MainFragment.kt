package org.cadnusdevs.sandroid.jwcongregationasignment.ui.screens

import android.view.View
import androidx.lifecycle.ViewModelProvider
import org.cadnusdevs.sandroid.jwcongregationasignment.R
import org.cadnusdevs.sandroid.jwcongregationasignment.models.MainViewModel
import org.cadnusdevs.sandroid.jwcongregationasignment.ui.shared.BaseFragment


class MainFragment : BaseFragment() {

    private lateinit var viewModel: MainViewModel

    override fun getTemplate() = R.layout.fragment_main
    override fun configureLayoutManager(view: View?) {
    }

    override fun setViewData() {
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
    }
    override fun setEvents() {
        this.onClick (R.id.new_brother_screen_button) {
            this.openFragment(EditBrotherFragment.newInstance())
        }

        this.onClick (R.id.list_brother_screen_button) {
            this.openFragment(ListBrotherFragment.newInstance())
        }
    }

    companion object {
        fun newInstance() = MainFragment()
    }
}
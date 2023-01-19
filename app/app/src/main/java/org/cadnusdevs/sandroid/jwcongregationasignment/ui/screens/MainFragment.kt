package org.cadnusdevs.sandroid.jwcongregationasignment.ui.screens

import android.view.View
import android.widget.Button
import androidx.lifecycle.ViewModelProvider
import org.cadnusdevs.sandroid.jwcongregationasignment.R
import org.cadnusdevs.sandroid.jwcongregationasignment.models.MainViewModel
import org.cadnusdevs.sandroid.jwcongregationasignment.ui.shared.BaseFragment


class MainFragment : BaseFragment() {
    private var btnTerritory: Button? = null

    private lateinit var viewModel: MainViewModel

    override fun getTemplate() = R.layout.fragment_main
    override fun configureLayout(view: View?) {
        btnTerritory = q.find<Button>(R.id.btn_territory)
    }

    override fun setViewData() {
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
    }
    override fun setEvents() {
        btnTerritory?.setOnClickListener {
            q.openFragment(TerritoryCardFragment())
        }
    }

    companion object {
        fun newInstance() = MainFragment()
    }
}
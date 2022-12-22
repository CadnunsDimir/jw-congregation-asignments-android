package org.cadnusdevs.sandroid.jwcongregationasignment.ui.screens

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import org.cadnusdevs.sandroid.jwcongregationasignment.ui.screens.adapters.MyBrotherRecyclerViewAdapter
import org.cadnusdevs.sandroid.jwcongregationasignment.R
import org.cadnusdevs.sandroid.jwcongregationasignment.db
import org.cadnusdevs.sandroid.jwcongregationasignment.ui.shared.BaseFragment

/**
 * A fragment representing a list of Items.
 */
class BrotherFragment : BaseFragment() {
    private var columnCount = 1

    override fun getTemplate() = R.layout.fragment_item_list
    override fun configureLayoutManager(view: View?) {
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                adapter = MyBrotherRecyclerViewAdapter(db.brothers)
            }
        }
    }
    override fun setViewData() {
        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }
    }

    override fun setEvents(){
    }

    companion object {
        const val ARG_COLUMN_COUNT = "column-count"
        @JvmStatic
        fun newInstance() =
            BrotherFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, 1)
                }
            }
    }
}
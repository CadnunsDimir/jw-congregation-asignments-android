package org.cadnusdevs.sandroid.jwcongregationasignment.ui.screens

import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import org.cadnusdevs.sandroid.jwcongregationasignment.ui.screens.adapters.MyBrotherRecyclerViewAdapter
import org.cadnusdevs.sandroid.jwcongregationasignment.R
import org.cadnusdevs.sandroid.jwcongregationasignment.models.Brother
import org.cadnusdevs.sandroid.jwcongregationasignment.repositories.BrotherRepository
import org.cadnusdevs.sandroid.jwcongregationasignment.ui.shared.BaseFragment

/**
 * A fragment representing a list of Items.
 */
class ListBrotherFragment : BaseFragment() {
    private var columnCount = 1
    private lateinit var repository: BrotherRepository

    override fun getTemplate() = R.layout.fragment_item_list
    override fun configureLayout(view: View?) {
        this.repository = BrotherRepository(requireActivity())
        var repository = this.repository
        var fragment = this;
        if (view is RecyclerView) {
            with(view) {
                layoutManager = when {
                    columnCount <= 1 -> LinearLayoutManager(context)
                    else -> GridLayoutManager(context, columnCount)
                }
                var brotherAdapter = MyBrotherRecyclerViewAdapter(repository.selectAll())
                brotherAdapter.defineItemClickBehavior{
                    fragment.onItemClick(it)
                }
                adapter = brotherAdapter
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

    fun onItemClick(brother: Brother) {
        this.openFragment(EditBrotherFragment.newInstance(brother))
    }

    companion object {
        const val ARG_COLUMN_COUNT = "column-count"
        @JvmStatic
        fun newInstance() =
            ListBrotherFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, 1)
                }
            }
    }
}
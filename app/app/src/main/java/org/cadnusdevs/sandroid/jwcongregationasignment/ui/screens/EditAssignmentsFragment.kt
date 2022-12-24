package org.cadnusdevs.sandroid.jwcongregationasignment.ui.screens

import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import org.cadnusdevs.sandroid.jwcongregationasignment.R
import org.cadnusdevs.sandroid.jwcongregationasignment.models.MeetingDay
import org.cadnusdevs.sandroid.jwcongregationasignment.repositories.BrotherRepository
import org.cadnusdevs.sandroid.jwcongregationasignment.ui.screens.adapters.MeetingDayArrayAdapter
import org.cadnusdevs.sandroid.jwcongregationasignment.ui.shared.BaseFragment


// template: R.layout.fragment_edit_asignations
class EditAssignmentsFragment : BaseFragment() {

    private lateinit var repository: BrotherRepository

    override fun getTemplate() = R.layout.fragment_edit_asignations

    override fun configureLayout(view: View?) {
        this.repository = BrotherRepository(requireActivity())
        var sheet = MeetingDay.generateDefaultList();
        var lisView = q.find<ListView>(R.id.meetings_list_view)
        lisView?.adapter = MeetingDayArrayAdapter(requireActivity(), sheet, this.repository.selectAll())
    }

    override fun setViewData() {
    }

    override fun setEvents() {
    }
}
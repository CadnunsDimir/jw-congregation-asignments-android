package org.cadnusdevs.sandroid.jwcongregationasignment.ui.screens

import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import org.cadnusdevs.sandroid.jwcongregationasignment.DateUtils
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
        setTitle();
        this.repository = BrotherRepository(requireActivity())
        var sheet = MeetingDay.generateDefaultList(10)
        var lisView = q.find<ListView>(R.id.meetings_list_view)
        lisView?.adapter = MeetingDayArrayAdapter(requireActivity(), sheet, this.repository.selectAll())
    }

    private fun setTitle() {
        val title =  this.q.find<TextView>(R.id.frag_edit_assignment_title)
        val date = DateUtils.ZeroBasedDate()
        date.addMonth(1)
        title?.text = title?.text.toString()
            .replace("__Month__", date.monthAsString(DateUtils.SupportedLanguages.Es))
            .replace("__year__", date.year.toString())
    }

    override fun setViewData() {
    }

    override fun setEvents() {
    }
}
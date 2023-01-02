package org.cadnusdevs.sandroid.jwcongregationasignment.ui.screens

import android.view.View
import android.widget.Button
import android.widget.ListView
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import org.cadnusdevs.sandroid.jwcongregationasignment.DateUtils
import org.cadnusdevs.sandroid.jwcongregationasignment.R
import org.cadnusdevs.sandroid.jwcongregationasignment.dbMock.Companion.brothers
import org.cadnusdevs.sandroid.jwcongregationasignment.models.Brother
import org.cadnusdevs.sandroid.jwcongregationasignment.models.MeetingDay
import org.cadnusdevs.sandroid.jwcongregationasignment.repositories.BrotherRepository
import org.cadnusdevs.sandroid.jwcongregationasignment.ui.screens.adapters.MeetingDayArrayAdapter
import org.cadnusdevs.sandroid.jwcongregationasignment.ui.shared.BaseFragment


// template: R.layout.fragment_edit_asignations
class EditAssignmentsFragment : BaseFragment(), MeetingDayArrayAdapter.OnChange {

    private lateinit var month: DateUtils.ZeroBasedDate
    private lateinit var statsButton: Button
    private lateinit var listViewAdapter: MeetingDayArrayAdapter
    private lateinit var lisView: ListView
    private lateinit var repository: BrotherRepository
    val brotherVersusDates = HashMap<String, HashMap<Int, String>>()

    override fun getTemplate() = R.layout.fragment_edit_asignations

    override fun configureLayout(view: View?) {
        month = DateUtils.ZeroBasedDate()
        month.addMonth(1)
        setTitle();
        this.repository = BrotherRepository(requireActivity())
        var sheet = MeetingDay.generateDefaultList(10, month)
        lisView = q.find<ListView>(R.id.meetings_list_view)!!
        brothers = this.repository.selectAll() as ArrayList<Brother>
        listViewAdapter = MeetingDayArrayAdapter(requireActivity(), month.formatMonthYearBr(), sheet, brothers)
        lisView.adapter = listViewAdapter
        statsButton = q.find<Button>(R.id.stats_btn)!!
    }

    private fun setTitle() {
        val title =  this.q.find<TextView>(R.id.frag_edit_assignment_title)

        title?.text = title?.text.toString()
            .replace("__Month__", month.monthAsString(DateUtils.SupportedLanguages.Es))
            .replace("__year__", month.year.toString())
    }

    override fun setViewData() {
    }

    override fun setEvents() {
        listViewAdapter.setOnChange(this)
        statsButton.setOnClickListener{
            val table = TableLayout(requireActivity())
            brotherVersusDates.forEach { (brother, hashmap) ->
                run {
                    val row = TableRow(requireActivity())
                    row.addView(q.Text(brother))
                    hashmap.forEach { (_, assignment) ->
                        val text = q.Text(assignment)
                        row.addView(text)
                        text.layoutParams.width = 50
                    }
                    table.addView(row)
                }
            }
            q.openDialog(table)
        }
    }

    override fun onChange(days: List<MeetingDay>) {
        println(days)
        generateStatistics(days)
    }

    private fun generateStatistics(days: List<MeetingDay>) {
        brotherVersusDates.clear()
        brothers.forEach { brother ->
            run {
                val itens = HashMap<Int, String>()
                days.forEach { meetingDay -> itens[days.indexOf(meetingDay)] = "" }
                brotherVersusDates[brother.name] = itens
            }
        }
        days.forEach {
            val position = days.indexOf(it)
            setOnMap(it.usherA, brotherVersusDates, position, "A")
            setOnMap(it.usherB, brotherVersusDates, position, "A")
            setOnMap(it.microphoneA, brotherVersusDates, position, "M")
            setOnMap(it.microphoneB, brotherVersusDates, position, "M")
            setOnMap(it.computer, brotherVersusDates, position, "C")
            setOnMap(it.soundSystem, brotherVersusDates, position, "S")
        }

        brothers.forEach { brother ->
            run {
                val dates = brotherVersusDates[brother.name]
                val sum = dates?.filter { it.value.isNotEmpty() }?.size
                dates?.set(days.size, "$sum")
            }
        }
    }

    private fun setOnMap(
        brother: Brother?,
        brotherVersusDates: HashMap<String, HashMap<Int, String>>,
        position: Int,
        value: String
    ) {
        if(brother != null && brother.id > 0) {
            val date = brotherVersusDates[brother.name]!!
            date[position] = value
        }
    }
}
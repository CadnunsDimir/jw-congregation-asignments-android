package org.cadnusdevs.sandroid.jwcongregationasignment.ui.screens

import android.view.View
import android.widget.Button
import android.widget.ListView
import android.widget.TableLayout
import android.widget.TableRow
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import org.cadnusdevs.sandroid.jwcongregationasignment.DateUtils
import org.cadnusdevs.sandroid.jwcongregationasignment.R
import org.cadnusdevs.sandroid.jwcongregationasignment.dbMock.Companion.brothers
import org.cadnusdevs.sandroid.jwcongregationasignment.models.Brother
import org.cadnusdevs.sandroid.jwcongregationasignment.models.MeetingDay
import org.cadnusdevs.sandroid.jwcongregationasignment.repositories.BrotherRepository
import org.cadnusdevs.sandroid.jwcongregationasignment.repositories.MeetingDayRepository
import org.cadnusdevs.sandroid.jwcongregationasignment.service.PrintService
import org.cadnusdevs.sandroid.jwcongregationasignment.ui.screens.adapters.MeetingDayArrayAdapter
import org.cadnusdevs.sandroid.jwcongregationasignment.ui.shared.BaseFragment


// template: R.layout.fragment_edit_asignations
class EditAssignmentsFragment : BaseFragment(), MeetingDayArrayAdapter.OnChange {

    private lateinit var titleView: TextView
    private lateinit var printService: PrintService
    private lateinit var floating: FloatingActionButton
    private lateinit var meetingDayRepository: MeetingDayRepository
    private lateinit var month: DateUtils.ZeroBasedDate
    private lateinit var statsButton: Button
    private lateinit var listViewAdapter: MeetingDayArrayAdapter
    private lateinit var lisView: ListView
    private lateinit var brotherRepository: BrotherRepository
    private val brotherVersusDates = HashMap<String, HashMap<Int, String>>()

    override fun getTemplate() = R.layout.fragment_edit_asignations

    override fun configureLayout(view: View?) {
        month = DateUtils.firstDayNextMonth()
        setTitle();
        brotherRepository = BrotherRepository(requireActivity())
        meetingDayRepository = MeetingDayRepository()
        val meetings = ArrayList(meetingDayRepository.getAllByMonthYear(month.formatMonthYearBr()))
        if(meetings.isEmpty()){
            meetings.addAll(MeetingDay.generateDefaultList(10, month))
            meetingDayRepository.saveAll(meetings)
        }
        lisView = q.find<ListView>(R.id.meetings_list_view)!!
        brothers = this.brotherRepository.selectAll() as ArrayList<Brother>
        listViewAdapter = MeetingDayArrayAdapter(requireActivity(), month.formatMonthYearBr(), meetings, brothers)
        lisView.adapter = listViewAdapter
        statsButton = q.find<Button>(R.id.stats_btn)!!
        floating = q.find<FloatingActionButton>(R.id.fab)!!
        printService = PrintService(requireActivity())
    }

    private fun setTitle() {
        titleView =  this.q.find<TextView>(R.id.frag_edit_assignment_title)!!

        titleView?.text = titleView?.text.toString()
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
        floating.setOnClickListener{
            val meetings = meetingDayRepository.getAllByMonthYear(month.formatMonthYearBr())
            printService.print(titleView.text.toString(), meetings)
        }
    }

    override fun onChange(meeting: MeetingDay) {
        meetingDayRepository.update(meeting)
        val days = meetingDayRepository.getAllByMonthYear(month.formatMonthYearBr())
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
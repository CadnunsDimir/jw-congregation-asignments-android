package org.cadnusdevs.sandroid.jwcongregationasignment.ui.screens

import android.view.View
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayout.OnTabSelectedListener
import org.cadnusdevs.sandroid.jwcongregationasignment.DateUtils
import org.cadnusdevs.sandroid.jwcongregationasignment.R
import org.cadnusdevs.sandroid.jwcongregationasignment.dbMock.Companion.brothers
import org.cadnusdevs.sandroid.jwcongregationasignment.models.Brother
import org.cadnusdevs.sandroid.jwcongregationasignment.models.MeetingDay
import org.cadnusdevs.sandroid.jwcongregationasignment.models.SpeechesArrangement
import org.cadnusdevs.sandroid.jwcongregationasignment.models.Weekend
import org.cadnusdevs.sandroid.jwcongregationasignment.repositories.BrotherRepository
import org.cadnusdevs.sandroid.jwcongregationasignment.repositories.MeetingDayRepository
import org.cadnusdevs.sandroid.jwcongregationasignment.repositories.SpeechesArrangementRepository
import org.cadnusdevs.sandroid.jwcongregationasignment.repositories.WeekendRepository
import org.cadnusdevs.sandroid.jwcongregationasignment.service.PrintService
import org.cadnusdevs.sandroid.jwcongregationasignment.ui.screens.adapters.MeetingDayArrayAdapter
import org.cadnusdevs.sandroid.jwcongregationasignment.ui.screens.adapters.SpeechTableAdapter
import org.cadnusdevs.sandroid.jwcongregationasignment.ui.screens.adapters.WeekendTableAdapter
import org.cadnusdevs.sandroid.jwcongregationasignment.ui.shared.BaseFragment


// template: R.layout.fragment_edit_asignations
class EditAssignmentsFragment : BaseFragment(), MeetingDayArrayAdapter.OnChange {

    private lateinit var arrangementEditText: EditText
    private lateinit var speechesRepository: SpeechesArrangementRepository
    private lateinit var weekendRepository: WeekendRepository
    private lateinit var titleView: TextView
    private lateinit var printService: PrintService
    private lateinit var floating: FloatingActionButton
    private lateinit var meetingDayRepository: MeetingDayRepository
    private lateinit var month: DateUtils.ZeroBasedDate
    private lateinit var statsButton: FloatingActionButton
    private lateinit var listViewAdapter: MeetingDayArrayAdapter
    private lateinit var lisView: ListView
    private lateinit var brotherRepository: BrotherRepository
    private val brotherVersusDates = HashMap<String, HashMap<Int, String>>()
    private val tableRowDates = "fechas"

    override fun getTemplate() = R.layout.fragment_edit_asignations

    override fun configureLayout(view: View?) {
        month = DateUtils.firstDayNextMonth()
        setTitle();
        brotherRepository = BrotherRepository(requireActivity())
        brothers = this.brotherRepository.selectAll() as ArrayList<Brother>
        meetingDayRepository = MeetingDayRepository(requireActivity(), brothers)
        val meetings = ArrayList(meetingDayRepository.getAllByMonthYear(month.formatMonthYearBr()))
        if(meetings.isEmpty()){
            meetings.addAll(MeetingDay.generateDefaultList(10, month))
            meetingDayRepository.saveAll(meetings)
        }
        lisView = q.find<ListView>(R.id.meetings_list_view)!!
        listViewAdapter = MeetingDayArrayAdapter(requireActivity(), month.formatMonthYearBr(), meetings, brothers)
        lisView.adapter = listViewAdapter
        statsButton = q.find<FloatingActionButton>(R.id.fab_stats)!!
        floating = q.find<FloatingActionButton>(R.id.fab)!!
        printService = PrintService(requireActivity())
        generateStatistics(meetings)
        defineTabWeekend(view,meetings)
    }

    private fun defineTabWeekend(view: View?, meetings: List<MeetingDay>) {
        weekendRepository = WeekendRepository(requireActivity(), brothers)

        var weekends = getWeekendsFromDb()
        if(weekends.isEmpty()){
            weekends = Weekend.listFrom(month.monthZeroBased, meetings)
            weekendRepository.saveAll(weekends)
        }
        WeekendTableAdapter(view, R.id.tab_weekend_table, brothers)
            .setData(weekends)
            .addRows()
        SpeechTableAdapter(view, R.id.speeches_table, brothers)
            .setData(weekends)
            .addRows()

        speechesRepository = SpeechesArrangementRepository(requireActivity())
        var arrangement = speechesRepository.getFromMonth(month)
        if (arrangement == null) {
            arrangement = SpeechesArrangement(month.year, month.monthZeroBased, "")
            speechesRepository.insert(arrangement)
        }

        arrangementEditText = q.find<EditText>(R.id.invited_congregation_editText)!!
        arrangementEditText?.setText(arrangement.invitedCongregation)

    }

    private fun getWeekendsFromDb(): List<Weekend> {
        val nextMonth = month.clone()
        nextMonth.addMonth(1)
        return weekendRepository.listBetweenDates(month, nextMonth)
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
        val tabWeekEnd = q.find<View>(R.id.tab_weekend_layout)
        listViewAdapter.setOnChange(this)
        statsButton.setOnClickListener{
            StatsDialog.open(q, requireActivity(), brotherVersusDates, tableRowDates)
        }
        floating.setOnClickListener{
            val meetings = meetingDayRepository.getAllByMonthYear(month.formatMonthYearBr())
            val weekends = getWeekendsFromDb()
            val invitedSpeechesCongregation = q.getEditTextValue(R.id.invited_congregation_editText)
            printService.print(titleView.text.toString(), meetings, weekends, invitedSpeechesCongregation)
        }

        tabWeekEnd?.visibility = View.GONE
        lisView.visibility = View.VISIBLE
        val listener = object : OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                if(tab?.text == "Mecânicas") {
                    tabWeekEnd?.visibility = View.GONE
                    lisView.visibility = View.VISIBLE
                }
                if(tab?.text == "Fin de Semana") {
                    tabWeekEnd?.visibility = View.VISIBLE
                    lisView.visibility = View.GONE
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
        }
        q.find<TabLayout>(R.id.tabLayout)?.addOnTabSelectedListener(listener)

        arrangementEditText?.onFocusChangeListener =
            View.OnFocusChangeListener { view, hasFocus ->
                if(!hasFocus){
                    var arrangement = speechesRepository.getFromMonth(month)!!
                    arrangement.invitedCongregation = arrangementEditText.text.toString()
                    speechesRepository.update(arrangement)
                }
            }
    }

    override fun onChange(meeting: MeetingDay) {
        meetingDayRepository.update(meeting)
        val days = meetingDayRepository.getAllByMonthYear(month.formatMonthYearBr())
        generateStatistics(days)
    }

    private fun generateStatistics(days: List<MeetingDay>) {
        brotherVersusDates.clear()
        brotherVersusDates[tableRowDates] = HashMap<Int, String>()
        brothers.forEach { brother ->
            run {
                val itens = HashMap<Int, String>()
                days.forEach { meetingDay -> itens[days.indexOf(meetingDay)] = "" }
                brotherVersusDates[brother.name] = itens
            }
        }
        days.forEach {
            val position = days.indexOf(it)
            brotherVersusDates[tableRowDates]?.set(position, it.day.toString())
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
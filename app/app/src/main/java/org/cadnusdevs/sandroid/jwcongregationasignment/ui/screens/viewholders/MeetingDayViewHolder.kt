package org.cadnusdevs.sandroid.jwcongregationasignment.ui.screens.viewholders;

import android.app.DatePickerDialog
import android.view.View;
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import org.cadnusdevs.sandroid.jwcongregationasignment.DateUtils
import org.cadnusdevs.sandroid.jwcongregationasignment.R
import org.cadnusdevs.sandroid.jwcongregationasignment.SPINNER_NO_OPTION_TEXT_PtBR
import org.cadnusdevs.sandroid.jwcongregationasignment.models.Brother
import org.cadnusdevs.sandroid.jwcongregationasignment.models.MeetingDay
import org.cadnusdevs.sandroid.jwcongregationasignment.ui.screens.QueryViews
import java.util.Calendar

class MeetingDayViewHolder
    private constructor(
        val date: TextView,
        val usherA: Spinner,
        val usherB: Spinner,
        val micA: Spinner,
        val micB: Spinner,
        val computer: Spinner,
        val soundSystem: Spinner,
        val cleanGroup: Spinner
    ) : OnItemSelectedListener {
    lateinit var meetingDayOriginalValue: MeetingDay
    private var onChangeListener: (() -> Unit?)? = null
    private var _dateZeroBased: DateUtils.ZeroBasedDate? = null
    private lateinit var q: QueryViews

    val setListener = DatePickerDialog.OnDateSetListener { _, year, month, day ->
        this._dateZeroBased = DateUtils.toDate(year, month, day)
        this.date.text = "${_dateZeroBased?.dayOfWeekAsString(DateUtils.SupportedLanguages.Es)}, ${_dateZeroBased?.formatPtBr()}"
    }

    var brothers: List<Brother> = ArrayList()
        set(value) {
            field = value
            fillAllSpinners()
        }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        this.onChangeListener?.invoke()
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
//        this.onChangeListener?.invoke()
    }
    private fun fillAllSpinners() {
        this.spinners().forEach { spinner ->
            val items = generateSpinnerItems(brothers)
            spinner?.let {
                    spinner -> q.setSpinnerItems(spinner, items) { it.name }
            }
        }
    }

    private fun spinners() = arrayOf(this.usherA,usherB, micA, micB, computer, soundSystem)

    private fun generateSpinnerItems(brothers: List<Brother>): List<Brother> {
        val options = ArrayList<Brother>()
        val noOption = Brother(0,SPINNER_NO_OPTION_TEXT_PtBR, false, false, false, false)
        options.add(noOption)
        options.addAll(brothers)
        return options
    }

    fun setValue(meetingDay: MeetingDay) {
        meetingDayOriginalValue = meetingDay
        cleanGroup.setSelection((cleanGroup.adapter as ArrayAdapter<String>).getPosition("${meetingDay.cleanGroupId}"))
    }

    fun getDate(): DateUtils.ZeroBasedDate {
        return if(this._dateZeroBased != null)
            this._dateZeroBased!!
        else DateUtils.ZeroBasedDate()
    }

    private fun setEvents() {
        date.setOnClickListener { editText ->
            val date = this.getDate()
            val datePicker = DatePickerDialog(editText.context, this.setListener,date.year, date.monthZeroBased, date.dayOfMonth)
            datePicker.show()
        }

        this.spinners().forEach { it.setOnItemSelectedListener(this) }
    }

    fun toModel(): MeetingDay {
        return MeetingDay(_dateZeroBased?.dayOfMonth,
            _dateZeroBased?.monthZeroBased,
            q.getSpinnerSelectedItem<Brother>(usherA),
            q.getSpinnerSelectedItem<Brother>(usherB),
            q.getSpinnerSelectedItem<Brother>(micA),
            q.getSpinnerSelectedItem<Brother>(micB),
            q.getSpinnerSelectedItem<Brother>(computer),
            q.getSpinnerSelectedItem<Brother>(soundSystem),
            (cleanGroup.selectedItem as String).toInt(),
        )
    }

    fun setOnChange(function: () -> Unit?) {
        onChangeListener = function
    }

    companion object {
        fun hold(view: View): MeetingDayViewHolder {
            val q = QueryViews(view);
            val date = q.find<TextView>(R.id.textViewMeetingDate)
            val usherA = q.find<Spinner>(R.id.spinnerUsherA)
            val usherB = q.find<Spinner>(R.id.spinnerUsherB)
            val micA = q.find<Spinner>(R.id.spinnerMicA)
            val micB = q.find<Spinner>(R.id.spinnerMicB)
            val computer = q.find<Spinner>(R.id.spinnerComputer)
            val soundSystem = q.find<Spinner>(R.id.spinnerSoundSystem)
            val cleanGroup = q.find<Spinner>(R.id.spinnerCleanGroup)
            val holder = MeetingDayViewHolder(date!!,usherA!!,usherB!!,micA!!,micB!!,computer!!,soundSystem!!, cleanGroup!!)
            holder.q = q
            holder.setEvents();
            return holder;
        }
    }


}

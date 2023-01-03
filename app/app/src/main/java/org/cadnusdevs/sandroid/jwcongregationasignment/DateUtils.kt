package org.cadnusdevs.sandroid.jwcongregationasignment

import java.text.SimpleDateFormat
import java.util.*

class DateUtils {
    enum class SupportedLanguages {
        Pt,
        Es
    }
    enum class WeekDay(val value: Int) {
        Sunday(Calendar.SUNDAY),
        Monday(Calendar.MONDAY),
        Tuesday(Calendar.TUESDAY),
        Wednesday(Calendar.WEDNESDAY),
        Thursday(Calendar.THURSDAY),
        Friday(Calendar.FRIDAY),
        Saturday(Calendar.SATURDAY);

        companion object{
            private val types = values().associateBy { it.value }

            fun from(calendar: Calendar): WeekDay? = types[calendar.get(Calendar.DAY_OF_WEEK)]
            fun from(value: Int): WeekDay? = types[value]
        }
    }



    class ZeroBasedDate(var year: Int, var monthZeroBased: Int, var dayOfMonth: Int) {
        constructor() : this(Calendar.getInstance())

        private constructor(date: Calendar) : this(0,0,0){
            this.year = date.get(Calendar.YEAR)
            this.monthZeroBased = date.get(Calendar.MONTH)
            this.dayOfMonth = date.get(Calendar.DAY_OF_MONTH)
        }

        private val ptBRLocale = Locale("pt", "BR")
        private val esESLocale = Locale("es", "ES")
        fun formatPtBr(): String {
            val dayString = if(dayOfMonth < 10) "0$dayOfMonth" else dayOfMonth
            val monthInt = monthZeroBased+1
            val monthString = if(monthInt < 10) "0$monthInt" else monthInt
            return "$dayString/$monthString/$year"
        }
        private fun format(language: SupportedLanguages, pattern: String): String {
            val locale = when(language){
                SupportedLanguages.Es-> esESLocale
                SupportedLanguages.Pt-> ptBRLocale
            }
            val sdf = SimpleDateFormat("dd/MM/yyyy", locale)
            val myDate: Date = sdf.parse(this.formatPtBr())!!
            sdf.applyPattern(pattern)
            return sdf.format(myDate)
                .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString() }
        }

        fun dayOfWeekAsString(language: SupportedLanguages) : String{
            return this.format(language, "EEEE")
        }

        fun addMonth(amount: Int) {
            val date = this.toCalendar()
            date.add(Calendar.MONTH, amount)
            this.year = date.get(Calendar.YEAR)
            this.monthZeroBased = date.get(Calendar.MONTH)
            this.dayOfMonth = date.get(Calendar.DAY_OF_MONTH)
        }

        private fun toCalendar(): Calendar {
            val date = Calendar.getInstance()
            date.set(year, monthZeroBased,dayOfMonth)
            return date
        }

        fun monthAsString(languages: SupportedLanguages) = this.format(languages, "MMMM")

        fun formatMonthYearBr() = this.format(SupportedLanguages.Pt, "MM/yyyy")

        private fun getNextDateFromCalendar(calendar: Calendar, vararg calendarWeekDays: WeekDay): ZeroBasedDate {
            while (!calendarWeekDays.contains(WeekDay.from(calendar))){
                calendar.add(Calendar.DAY_OF_MONTH,1)
            }
            return ZeroBasedDate(calendar)
        }

        fun nextDate(vararg calendarWeekDays: WeekDay): ZeroBasedDate {
            val calendar = toCalendar()
            calendar.add(Calendar.DAY_OF_MONTH,1)
            return getNextDateFromCalendar(calendar, *calendarWeekDays)
        }

        fun currentDateOrNext(vararg calendarWeekDays: WeekDay): ZeroBasedDate {
            val calendar = toCalendar()
            return if(calendarWeekDays.contains(WeekDay.from(calendar)))
                clone()
            else{
                getNextDateFromCalendar(calendar, *calendarWeekDays)
            }
        }

        private fun clone() = ZeroBasedDate(year, monthZeroBased, dayOfMonth)
    }

    companion object{
        fun toDate(year: Int, monthZeroBased: Int, dayOfMonth: Int) = ZeroBasedDate(year,monthZeroBased, dayOfMonth)
        fun firstDayNextMonth(): ZeroBasedDate {
            val month = ZeroBasedDate()
            month.dayOfMonth = 1
            month.addMonth(1)
            return month
        }
    }
}
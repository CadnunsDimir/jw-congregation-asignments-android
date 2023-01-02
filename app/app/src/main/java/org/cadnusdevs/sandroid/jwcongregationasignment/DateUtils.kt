package org.cadnusdevs.sandroid.jwcongregationasignment

import java.text.SimpleDateFormat
import java.util.*

class DateUtils {
    enum class SupportedLanguages {
        Pt,
        Es
    }




    class ZeroBasedDate(var year: Int, var monthZeroBased: Int, var dayOfMonth: Int) {
        constructor() : this(Calendar.getInstance())

        private constructor(date: Calendar) : this(0,0,0){
            this.year = date.get(Calendar.YEAR)
            this.monthZeroBased = date.get(Calendar.MONTH)
            this.dayOfMonth = date.get(Calendar.DAY_OF_MONTH)
        }

        private val PtBRLocale = Locale("pt", "BR")
        private val EsESLocale = Locale("es", "ES")
        fun formatPtBr(): String {
            val dayString = if(dayOfMonth < 10) "0$dayOfMonth" else dayOfMonth
            val monthInt = monthZeroBased+1
            val monthString = if(monthInt < 10) "0$monthInt" else monthInt
            return "$dayString/$monthString/$year"
        }
        private fun format(language: SupportedLanguages, pattern: String): String {
            val locale = when(language){
                SupportedLanguages.Es-> EsESLocale
                SupportedLanguages.Pt-> PtBRLocale
            }
            val sdf = SimpleDateFormat("dd/MM/yyyy", locale)
            val myDate: Date = sdf.parse(this.formatPtBr())
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

        fun monthAsString(languages: SupportedLanguages): String {
            return this.format(languages, "MMMM")
        }

        fun formatMonthYearBr(): String {
            return this.format(SupportedLanguages.Pt, "MM/yyyy")
        }

        fun currentDateOrNext(vararg calendarWeekDays: Int): ZeroBasedDate {
            val calendar = toCalendar()
            if(calendarWeekDays.contains(calendar.get(Calendar.DAY_OF_WEEK)))
                return this
            else{
                return nextDate(calendar, *calendarWeekDays)
            }
        }

        private fun nextDate(calendar: Calendar, vararg calendarWeekDays: Int): ZeroBasedDate {
            var counter = 1
            while (counter<=7 || !calendarWeekDays.contains(calendar.get(Calendar.DAY_OF_WEEK))){
                calendar.add(Calendar.DAY_OF_MONTH,1)
                ++counter
            }
            return ZeroBasedDate(calendar)
        }



        fun nextDate(vararg calendarWeekDays: Int): ZeroBasedDate {
            val calendar = toCalendar()
            return nextDate(calendar, *calendarWeekDays)
        }
    }

    companion object{
        fun toDate(year: Int, monthZeroBased: Int, dayOfMonth: Int) = ZeroBasedDate(year,monthZeroBased, dayOfMonth)
    }
}
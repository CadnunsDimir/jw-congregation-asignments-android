package org.cadnusdevs.sandroid.jwcongregationasignment

import java.text.SimpleDateFormat
import java.util.*

class DateUtils {
    enum class SupportedLanguages {
        Pt,
        Es
    }



    class ZeroBasedDate(var year: Int, var monthZeroBased: Int, var dayOfMonth: Int) {
        constructor() : this(0,0,0){
            val date = Calendar.getInstance()
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
        fun dayOfWeekAsString(language: SupportedLanguages) : String{
            val locale = when(language){
                SupportedLanguages.Es-> EsESLocale
                SupportedLanguages.Pt-> PtBRLocale
            }
            val sdf = SimpleDateFormat("dd/MM/yyyy", locale)
            val myDate: Date = sdf.parse(this.formatPtBr())
            sdf.applyPattern("EEEE")
            return sdf.format(myDate)
        }
    }

    companion object{
        fun toDate(year: Int, monthZeroBased: Int, dayOfMonth: Int) = ZeroBasedDate(year,monthZeroBased, dayOfMonth)
    }
}
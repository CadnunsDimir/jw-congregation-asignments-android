package org.cadnusdevs.sandroid.jwcongregationasignment

class DateUtils {
    class ZeroBasedDate(val year: Int, val monthZeroBased: Int, val dayOfMonth: Int){}

    companion object{
        fun formatPtBr(year: Int, monthZeroBased: Int, dayOfMonth: Int): String {
            val dayString = if(dayOfMonth < 10) "0$dayOfMonth" else dayOfMonth
            val monthInt = monthZeroBased+1
            val monthString = if(monthInt < 10) "0$monthInt" else monthInt
            return "$dayString/$monthString/$year"
        }
    }
}
package org.cadnusdevs.sandroid.jwcongregationasignment.models

import org.cadnusdevs.sandroid.jwcongregationasignment.DateUtils.*

class MeetingDay(val day: Int?,
                 val month: Int?,
                 val year: Int?,
                 val monthYearSheet: String?,
                 val usherA: Brother?,
                 val usherB: Brother?,
                 val microphoneA: Brother?,
                 val microphoneB: Brother?,
                 val computer: Brother?,
                 val soundSystem: Brother?,
                 val cleanGroupId: Int
                 ) {
    val date
    get(): ZeroBasedDate {
        return ZeroBasedDate(year!!, month!!, day!!)
    }
    companion object {
        fun generateDefaultList(days: Int, initialDate: ZeroBasedDate) : ArrayList<MeetingDay>{
            val meetingDays = arrayOf(WeekDay.Friday, WeekDay.Sunday)
            val sheet = ArrayList<MeetingDay>()
            var currentDate = initialDate.currentDateOrNext(*meetingDays)
            for(i in 1..days) {
                val groupId = (i - 1) % 4 + 1
                sheet.add(MeetingDay(
                    currentDate.dayOfMonth,
                    currentDate.monthZeroBased,
                    currentDate.year,
                    initialDate.formatMonthYearBr(),null,null,null,null,null,null,
                    groupId))
                currentDate = currentDate.nextDate(*meetingDays)
            }
            return sheet;
        }
    }
}


package org.cadnusdevs.sandroid.jwcongregationasignment.models

import org.cadnusdevs.sandroid.jwcongregationasignment.DateUtils
import java.util.Calendar

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

    companion object {
        fun generateDefaultList(days: Int, initialDate: DateUtils.ZeroBasedDate) : ArrayList<MeetingDay>{
            val sheet = ArrayList<MeetingDay>()
            var currentDate = initialDate.currentDateOrNext(Calendar.FRIDAY, Calendar.SUNDAY)
            for(i in 1..days) {
                val groupId = (i - 1) % 4 + 1
                sheet.add(MeetingDay(
                    currentDate.year,
                    currentDate.monthZeroBased,
                    currentDate.dayOfMonth,
                    initialDate.formatMonthYearBr(),null,null,null,null,null,null,
                    groupId))
                currentDate = currentDate.nextDate(Calendar.FRIDAY, Calendar.SUNDAY)
            }
            return sheet;
        }
    }
}


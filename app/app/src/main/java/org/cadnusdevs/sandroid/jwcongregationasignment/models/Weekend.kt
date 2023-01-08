package org.cadnusdevs.sandroid.jwcongregationasignment.models

import org.cadnusdevs.sandroid.jwcongregationasignment.DateUtils

class Weekend(val date: DateUtils.ZeroBasedDate,
              var meetingPresident: Brother?,
              var watchTowerReader: Brother?,
              var speech: Speech
              ) {
    class Speech(var title: String, var speaker: String)
    companion object{
        fun listFrom(monthNumber: Int, meetings: List<MeetingDay>): List<Weekend> {
            return meetings
                .filter { meetingDay -> DateUtils.WeekDay.weekEndDays.contains(meetingDay.date.weekDay) && meetingDay.month == monthNumber }
                .map { Weekend(it.date,null, null, Speech("", "")) }.toList()
        }
    }
}
package org.cadnusdevs.sandroid.jwcongregationasignment.models

import org.cadnusdevs.sandroid.jwcongregationasignment.DateUtils

class SpeechesArrangement (val invitedCongregation: String, val speechesDuringMonth: List<Speech>) {
    class Speech(val date: DateUtils.ZeroBasedDate, val title: String, val speaker: String)
    companion object{
        fun fromWeekDayList(invitedCongregation: String, monthNumber: Int, meetings: List<MeetingDay>): SpeechesArrangement{
            return SpeechesArrangement(invitedCongregation,
                meetings.filter { meetingDay -> DateUtils.WeekDay.weekEndDays.contains(meetingDay.date.weekDay) && meetingDay.month == monthNumber }
                    .map{x-> Speech(x.date,"","")}.toList()
            )
        }
    }
}
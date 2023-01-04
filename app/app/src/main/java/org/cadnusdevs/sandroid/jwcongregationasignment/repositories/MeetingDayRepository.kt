package org.cadnusdevs.sandroid.jwcongregationasignment.repositories

import org.cadnusdevs.sandroid.jwcongregationasignment.models.MeetingDay

class MeetingDayRepository {
    fun getAllByMonthYear(monthYear: String): List<MeetingDay> {
        return list.filter{meetingDay -> monthYear == meetingDay.monthYearSheet }
    }

    fun saveAll(days: List<MeetingDay>) {
        if(days.isNotEmpty()){
            val monthYear = days.first().monthYearSheet
            list.removeAll(list.filter{ meetingDay -> monthYear.equals(meetingDay.monthYearSheet) }.toSet())
            list.addAll(days)
        }
    }

    fun update(meeting: MeetingDay) {
        val position = list.indexOf(list.first { x-> x.monthYearSheet == meeting.monthYearSheet && x.day == meeting.day && x.month == meeting.month })
        if(position >= 0) list[position] = meeting
    }

    companion object {
        private val list = ArrayList<MeetingDay>()
    }
}
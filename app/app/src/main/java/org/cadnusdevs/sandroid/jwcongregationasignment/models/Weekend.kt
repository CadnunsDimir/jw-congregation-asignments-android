package org.cadnusdevs.sandroid.jwcongregationasignment.models

import org.cadnusdevs.sandroid.jwcongregationasignment.DateUtils

class Weekend(val date: DateUtils.ZeroBasedDate, val meetingPresident: Brother?, val watchTowerReader: Brother?) {
    companion object{
        fun listFrom(meetings: List<MeetingDay>): List<Weekend> {
            return meetings.map { Weekend(it.date,null, null) }.toList()
        }
    }
}
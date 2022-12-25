package org.cadnusdevs.sandroid.jwcongregationasignment.models

class MeetingDay(day: Int?, month: Int?,
                 usherA: Brother?,
                 usherB: Brother?,
                 microphoneA: Brother?,
                 microphoneB: Brother?,
                 computer: Brother?,
                 soundSystem: Brother?
                 ) {

    companion object {
        fun generateDefaultList(days: Int) : ArrayList<MeetingDay>{
            var sheet = ArrayList<MeetingDay>()
            for(i in 1..days) {
                sheet.add(MeetingDay(null,null,null,null,null,null,null,null))
            }
            return sheet;
        }
    }
}


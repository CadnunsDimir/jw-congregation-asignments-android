package org.cadnusdevs.sandroid.jwcongregationasignment.models

class MeetingDay(val day: Int?,
                 val month: Int?,
                 val usherA: Brother?,
                 val usherB: Brother?,
                 val microphoneA: Brother?,
                 val microphoneB: Brother?,
                 val computer: Brother?,
                 val soundSystem: Brother?,
                 val cleanGroupId: Int
                 ) {

    companion object {
        fun generateDefaultList(days: Int) : ArrayList<MeetingDay>{
            var sheet = ArrayList<MeetingDay>()
            for(i in 1..days) {
                val groupId = (i - 1) % 4 + 1
                sheet.add(MeetingDay(null,null,null,null,null,null,null,null,groupId))
            }
            return sheet;
        }
    }
}


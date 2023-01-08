package org.cadnusdevs.sandroid.jwcongregationasignment.repositories

import org.cadnusdevs.sandroid.jwcongregationasignment.DateUtils
import org.cadnusdevs.sandroid.jwcongregationasignment.models.Weekend

class WeekendRepository {
    fun listBetweenDates(
        month: DateUtils.ZeroBasedDate,
        nextMonth: DateUtils.ZeroBasedDate
    ): List<Weekend> {
        return virtualDB.filter {
            val date = it.date.asTimeStamp()
            return@filter date >= month.asTimeStamp() && date < nextMonth.asTimeStamp()
        }
    }

    fun saveAll(weekends: List<Weekend>) {
        virtualDB.addAll(weekends)
    }

    fun getFromDate(date: DateUtils.ZeroBasedDate): Weekend {
        return virtualDB.first { date.isSameDate(it.date) }
    }

    fun update(weekend: Weekend) {
        val position = virtualDB.indexOf(virtualDB.first { it.date.asTimeStamp() == weekend.date.asTimeStamp() })
        virtualDB[position] = weekend
    }

    companion object{
        private val virtualDB = ArrayList<Weekend>()
    }
}
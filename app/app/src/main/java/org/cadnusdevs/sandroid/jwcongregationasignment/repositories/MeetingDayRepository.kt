package org.cadnusdevs.sandroid.jwcongregationasignment.repositories

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.provider.BaseColumns
import org.cadnusdevs.sandroid.jwcongregationasignment.DateUtils
import org.cadnusdevs.sandroid.jwcongregationasignment.models.Brother
import org.cadnusdevs.sandroid.jwcongregationasignment.models.MeetingDay

class MeetingDayRepository (ctx: Context, private val brothers: List<Brother>){
    private var dbHelper = AppDbHelper(ctx)
    private var table = DbContracts.MeetingDayEntry
    fun getAllByMonthYear(monthYear: String): List<MeetingDay> {
//        return list.filter{meetingDay -> monthYear == meetingDay.monthYearSheet }
        val db = dbHelper.readableDatabase
        val projection = table.columnsList

        val selection = "${table.COLUMN_MONTH_YEAR_SHEET} LIKE ?"
        val selectionArgs = arrayOf(monthYear)

        val sortOrder = "${table.COLUMN_DATE} ASC"

        val cursor = db.query(
            table.TABLE_NAME,
            projection,
            selection,
            selectionArgs,
            null,
            null,
            sortOrder
        )
        var meetings = ArrayList<MeetingDay>()
        with(cursor) {
            while (moveToNext()) {
                var date = DateUtils.toDateFromTimeStamp(getLong(getColumnIndexOrThrow(table.COLUMN_DATE)))
                BrotherRepository.setDbInfo(cursor, brothers)
                val meeting = MeetingDay(
                    date.dayOfMonth,
                    date.monthZeroBased,
                    date.year,
                    getString(getColumnIndexOrThrow(table.COLUMN_MONTH_YEAR_SHEET)),
                    BrotherRepository.getBrother(table.COLUMN_USHER_A_BROTHER_ID),
                    BrotherRepository.getBrother(table.COLUMN_USHER_B_BROTHER_ID),
                    BrotherRepository.getBrother(table.COLUMN_MIC_A_BROTHER_ID),
                    BrotherRepository.getBrother(table.COLUMN_MIC_B_BROTHER_ID),
                    BrotherRepository.getBrother(table.COLUMN_COMPUTER_BROTHER_ID),
                    BrotherRepository.getBrother(table.COLUMN_SOUND_SYSTEM_BROTHER_ID),
                    getInt(getColumnIndexOrThrow(table.COLUMN_CLEANING_GROUP_ID)),
                    getLong(getColumnIndexOrThrow(BaseColumns._ID)))
                meetings.add(meeting)
            }
        }
        cursor.close()
        return meetings
    }



    fun saveAll(days: List<MeetingDay>) {
        if(days.isNotEmpty()){
            val monthYear = days.first().monthYearSheet
//            list.removeAll(list.filter{ meetingDay -> monthYear.equals(meetingDay.monthYearSheet) }.toSet())
//            list.addAll(days)

            deleteAll(monthYear)
            days.forEach {
                insert(it)
            }
        }
    }

    private fun deleteAll(monthYear: String?) {
        val db = dbHelper.writableDatabase
        val selection = "${table.COLUMN_MONTH_YEAR_SHEET} LIKE ?"
        val selectionArgs = arrayOf(monthYear)
        db.delete(table.TABLE_NAME, selection, selectionArgs)
    }

    private fun insert(meeting: MeetingDay) {
        val db = dbHelper.writableDatabase
        meeting.id = db?.insert(table.TABLE_NAME, null, buildDbValues(meeting))!!
    }

    private fun buildDbValues(meeting: MeetingDay) = ContentValues().apply {
        put(table.COLUMN_DATE, meeting.date.asTimeStamp())
        put(table.COLUMN_MONTH_YEAR_SHEET, meeting.monthYearSheet)
        put(table.COLUMN_USHER_A_BROTHER_ID, meeting.usherA?.id)
        put(table.COLUMN_USHER_B_BROTHER_ID, meeting.usherB?.id)
        put(table.COLUMN_MIC_A_BROTHER_ID, meeting.microphoneA?.id)
        put(table.COLUMN_MIC_B_BROTHER_ID, meeting.microphoneB?.id)
        put(table.COLUMN_COMPUTER_BROTHER_ID, meeting.computer?.id)
        put(table.COLUMN_SOUND_SYSTEM_BROTHER_ID, meeting.soundSystem?.id)
        put(table.COLUMN_CLEANING_GROUP_ID, meeting.cleanGroupId)
    }

    fun update(meeting: MeetingDay) {
//        val position = list.indexOf(list.first { x-> x.monthYearSheet == meeting.monthYearSheet && x.day == meeting.day && x.month == meeting.month })
//        if(position >= 0) list[position] = meeting
        val db = dbHelper.writableDatabase
        val selection = "${BaseColumns._ID} LIKE ?"
        val selectionArgs = arrayOf(meeting.id.toString())
        var count = db.update(
            table.TABLE_NAME,
            buildDbValues(meeting),
            selection,
            selectionArgs)
    }

    companion object {
//        private val list = ArrayList<MeetingDay>()
    }
}
package org.cadnusdevs.sandroid.jwcongregationasignment.repositories

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns

object DbContracts {
    // Table contents are grouped together in an anonymous object.
    object ColumnType {
        const val INT_PK = "INTEGER PRIMARY KEY"
        const val INT = "INTEGER"
        const val STRING = "TEXT"
    }

    object BrotherEntry : BaseColumns {
        const val TABLE_NAME = "brothers"
        const val COLUMN_NAME= "name"
        const val COLUMN_USHER = "usher"
        const val COLUMN_MICROPHONE = "microphone"
        const val COLUMN_COMPUTER = "computer"
        const val COLUMN_SOUND_SYSTEM = "sound_system"
    }

    object MeetingDayEntry: BaseColumns {
        const val TABLE_NAME = "meeting_days"
        const val COLUMN_DATE = "date"
        const val COLUMN_MONTH_YEAR_SHEET = "month_year_sheet"
        const val COLUMN_USHER_A_BROTHER_ID = "usher_a_brother_id"
        const val COLUMN_USHER_B_BROTHER_ID = "usher_b_brother_id"
        const val COLUMN_MIC_A_BROTHER_ID = "mic_a_brother_id"
        const val COLUMN_MIC_B_BROTHER_ID = "mic_b_brother_id"
        const val COLUMN_COMPUTER_BROTHER_ID = "computer_brother_id"
        const val COLUMN_SOUND_SYSTEM_BROTHER_ID = "sound_system_brother_id"
        const val COLUMN_CLEANING_GROUP_ID = "cleaning_group_id"
        val columnsList = arrayOf(
            BaseColumns._ID,
            COLUMN_DATE,
            COLUMN_MONTH_YEAR_SHEET,
            COLUMN_USHER_A_BROTHER_ID,
            COLUMN_USHER_B_BROTHER_ID,
            COLUMN_MIC_A_BROTHER_ID,
            COLUMN_MIC_B_BROTHER_ID,
            COLUMN_COMPUTER_BROTHER_ID,
            COLUMN_SOUND_SYSTEM_BROTHER_ID,
            COLUMN_CLEANING_GROUP_ID
        )
    }

    const val CREATE_BROTHER_ENTRY =
        "CREATE TABLE ${BrotherEntry.TABLE_NAME} (" +
            "${BaseColumns._ID} ${ColumnType.INT_PK}," +
            "${BrotherEntry.COLUMN_NAME} ${ColumnType.STRING}, " +
            "${BrotherEntry.COLUMN_USHER} ${ColumnType.INT}, " +
            "${BrotherEntry.COLUMN_MICROPHONE} ${ColumnType.INT}," +
            "${BrotherEntry.COLUMN_COMPUTER} ${ColumnType.INT}," +
            "${BrotherEntry.COLUMN_SOUND_SYSTEM} ${ColumnType.INT})"

    const val CREATE_MEETING_DAY_ENTRY =
        "CREATE TABLE ${MeetingDayEntry.TABLE_NAME} (" +
            "${BaseColumns._ID} ${ColumnType.INT_PK},"+
            "${MeetingDayEntry.COLUMN_DATE} ${ColumnType.INT}," +
            "${MeetingDayEntry.COLUMN_MONTH_YEAR_SHEET} ${ColumnType.STRING}," +
            "${MeetingDayEntry.COLUMN_USHER_A_BROTHER_ID} ${ColumnType.INT}," +
            "${MeetingDayEntry.COLUMN_USHER_B_BROTHER_ID} ${ColumnType.INT}," +
            "${MeetingDayEntry.COLUMN_MIC_A_BROTHER_ID} ${ColumnType.INT}," +
            "${MeetingDayEntry.COLUMN_MIC_B_BROTHER_ID} ${ColumnType.INT}," +
            "${MeetingDayEntry.COLUMN_COMPUTER_BROTHER_ID} ${ColumnType.INT}," +
            "${MeetingDayEntry.COLUMN_SOUND_SYSTEM_BROTHER_ID} ${ColumnType.INT}," +
            "${MeetingDayEntry.COLUMN_CLEANING_GROUP_ID} ${ColumnType.INT})"

    const val SQL_CREATE_ENTRIES = "$CREATE_BROTHER_ENTRY $CREATE_MEETING_DAY_ENTRY"


    const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${BrotherEntry.TABLE_NAME}"
}

class AppDbHelper (context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(DbContracts.SQL_CREATE_ENTRIES)
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        //db.execSQL(DbContracts.SQL_DELETE_ENTRIES)
        //onCreate(db)
        if(needCreateMeetingDayEntry(oldVersion,newVersion)){
            db.execSQL(DbContracts.CREATE_MEETING_DAY_ENTRY)
        }
    }

    private fun needCreateMeetingDayEntry(oldVersion: Int, newVersion: Int) =
        oldVersion == 1 && newVersion == 2

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }
    companion object {
        // If you change the database schema, you must increment the database version.
        const val DATABASE_VERSION = 2
        const val DATABASE_NAME = "JwCongregationAsignments.db"
    }
}
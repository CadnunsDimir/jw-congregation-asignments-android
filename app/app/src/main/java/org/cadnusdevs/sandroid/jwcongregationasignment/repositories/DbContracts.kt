package org.cadnusdevs.sandroid.jwcongregationasignment.repositories

import android.content.ContentValues
import android.provider.BaseColumns
import org.cadnusdevs.sandroid.jwcongregationasignment.DateUtils
import org.cadnusdevs.sandroid.jwcongregationasignment.models.SpeechesArrangement
import org.cadnusdevs.sandroid.jwcongregationasignment.models.Weekend
import org.cadnusdevs.sandroid.jwcongregationasignment.repositories.DatabaseTable.Column

object DbContracts {
    // Table contents are grouped together in an anonymous object.
    object ColumnType {
        const val INT_PK = "INTEGER PRIMARY KEY"
        const val INT = "INTEGER"
        const val STRING = "TEXT"
    }

    interface EntryWithTable<T>: BaseColumns{
        fun values(entity: T): ContentValues

        val table: DatabaseTable
    }

    object SpeechesArrangementEntry: EntryWithTable<SpeechesArrangement> {
        const val YEAR = "year"
        const val MONTH = "month"
        const val INVITED_CONGREGATION = "invited_congregation"

        override val table = DatabaseTable("speeches_arrangement",
            Column(YEAR, ColumnType.INT),
            Column(MONTH, ColumnType.INT),
            Column(INVITED_CONGREGATION, ColumnType.STRING),
        )

        override fun values(entity: SpeechesArrangement) = ContentValues().apply {
            put(YEAR, entity.year)
            put(MONTH, entity.month)
            put(INVITED_CONGREGATION, entity.invitedCongregation)
        }
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

    object WeekendEntry: EntryWithTable<Weekend> {
        const val YEAR = "year"
        const val MONTH = "month"
        const val DAY = "day"
        const val READER_BROTHER_ID = "reader_brother_id"
        const val PRESIDENT_BROTHER_ID = "president_brother_id"
        const val SPEECH_TITLE = "speech_title"
        const val SPEECH_SPEAKER_NAME = "speech_speaker_name"

        override val table = DatabaseTable("weekend",
            Column(BaseColumns._ID, ColumnType.INT_PK),
            Column(YEAR, ColumnType.INT),
            Column(MONTH, ColumnType.INT),
            Column(DAY, ColumnType.INT),
            Column(READER_BROTHER_ID, ColumnType.INT),
            Column(PRESIDENT_BROTHER_ID, ColumnType.INT),
            Column(SPEECH_TITLE, ColumnType.STRING),
            Column(SPEECH_SPEAKER_NAME, ColumnType.STRING),
        )

        override fun values(weekend: Weekend) = ContentValues().apply {
            put(YEAR, weekend.date.year)
            put(MONTH, weekend.date.monthZeroBased)
            put(DAY, weekend.date.dayOfMonth)
            put(READER_BROTHER_ID, weekend.watchTowerReader?.id)
            put(PRESIDENT_BROTHER_ID, weekend.meetingPresident?.id)
            put(SPEECH_TITLE, weekend.speech.title)
            put(SPEECH_SPEAKER_NAME, weekend.speech.speaker)
        }

        fun whereDate(date: DateUtils.ZeroBasedDate): DatabaseTable.Where {
            return DatabaseTable.Where(YEAR, date.year)
                .and(MONTH, date.monthZeroBased)
                .and(DAY, date.dayOfMonth)
        }

        fun whereBetweenDates(
            start: DateUtils.ZeroBasedDate,
            finish: DateUtils.ZeroBasedDate
        ): DatabaseTable.Where {

            return DatabaseTable.Where.getInstance(
                "($YEAR * 365 + ($MONTH + 1) * 45 + $DAY) >= ( ? * 365 + (? + 1) * 45 + ?) AND " +
                        "($YEAR * 365 + ($MONTH + 1) * 45 + $DAY) < ( ? * 365 + (? + 1) * 45 + ?)",
                arrayOf(
                    start.year, start.monthZeroBased,start.dayOfMonth,
                    finish.year, finish.monthZeroBased,finish.dayOfMonth)
                    .map { it.toString() }.toTypedArray()
            )
        }
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

    fun sqlCreateAllTables() =
        "$CREATE_BROTHER_ENTRY $CREATE_MEETING_DAY_ENTRY ${WeekendEntry.table.sqlCreateTable()} ${SpeechesArrangementEntry.table.sqlCreateTable()}"

    const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${BrotherEntry.TABLE_NAME}"
}
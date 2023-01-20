package org.cadnusdevs.sandroid.jwcongregationasignment.repositories

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class AppDbHelper (context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(DbContracts.sqlCreateAllTables())
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        //db.execSQL(DbContracts.SQL_DELETE_ENTRIES)
        //onCreate(db)
        if(needCreateMeetingDayEntry(oldVersion,newVersion)){
            db.execSQL(DbContracts.CREATE_MEETING_DAY_ENTRY)
        }
        if(needCreateWeekendEntry(oldVersion,newVersion)){
            db.execSQL(DbContracts.WeekendEntry.table.sqlCreateTable())
        }
        if(needCreateSpeechesEntry(oldVersion,newVersion)){
            db.execSQL(DbContracts.SpeechesArrangementEntry.table.sqlCreateTable())
        }
        if(needCreateTerritoryEntries(oldVersion,newVersion)){
            db.execSQL(DbContracts.territoryCardRelationship.joinToString(" ") { it.table.sqlCreateTable() })
        }
    }

    private fun needCreateTerritoryEntries(oldVersion: Int, newVersion: Int) = oldVersion == 4 && newVersion == 5

    private fun needCreateSpeechesEntry(oldVersion: Int, newVersion: Int) =
        oldVersion == 3 && newVersion == 4

    private fun needCreateWeekendEntry(oldVersion: Int, newVersion: Int) =
        oldVersion == 2 && newVersion == 3

    private fun needCreateMeetingDayEntry(oldVersion: Int, newVersion: Int) =
        oldVersion == 1 && newVersion == 2 || oldVersion == 1 && newVersion == 3

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }
    companion object {
        // If you change the database schema, you must increment the database version.
        /*
        squema versions:
        * 1 = brothers
        * 2 = meetings
        * 3 = weekend
        * 4 = SpeechesArrangement
          5 = Territory Card and Direction
        * */
        const val DATABASE_VERSION = 4
        const val DATABASE_NAME = "JwCongregationAsignments.db"
    }
}
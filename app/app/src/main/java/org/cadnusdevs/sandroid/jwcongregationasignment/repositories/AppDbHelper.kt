package org.cadnusdevs.sandroid.jwcongregationasignment.repositories

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns

object DbContracts {
    // Table contents are grouped together in an anonymous object.
    object BrotherEntry : BaseColumns {
        const val TABLE_NAME = "brothers"
        const val COLUMN_NAME= "name"
        const val COLUMN_USHER = "usher"
        const val COLUMN_MICROPHONE = "microphone"
        const val COLUMN_COMPUTER = "computer"
        const val COLUMN_SOUND_SYSTEM = "sound_system"
    }

    const val SQL_CREATE_ENTRIES =
        "CREATE TABLE ${BrotherEntry.TABLE_NAME} (" +
                "${BaseColumns._ID} INTEGER PRIMARY KEY," +
                "${BrotherEntry.COLUMN_NAME} TEXT, " +
                "${BrotherEntry.COLUMN_USHER} INTEGER, " +
                "${BrotherEntry.COLUMN_MICROPHONE} INTEGER," +
                "${BrotherEntry.COLUMN_COMPUTER} INTEGER," +
                "${BrotherEntry.COLUMN_SOUND_SYSTEM} INTEGER)"

    const val SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS ${BrotherEntry.TABLE_NAME}"
}

class AppDbHelper (context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(DbContracts.SQL_CREATE_ENTRIES)
    }
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(DbContracts.SQL_DELETE_ENTRIES)
        onCreate(db)
    }
    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }
    companion object {
        // If you change the database schema, you must increment the database version.
        const val DATABASE_VERSION = 1
        const val DATABASE_NAME = "JwCongregationAsignments.db"
    }
}
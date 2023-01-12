package org.cadnusdevs.sandroid.jwcongregationasignment.repositories

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase

open class BaseRepository(ctx: Context){
    private var dbHelper = AppDbHelper(ctx)

    fun db(writeMode: Boolean = false): SQLiteDatabase {
        return if(writeMode) dbHelper.writableDatabase else dbHelper.readableDatabase
    }

    fun query(db: SQLiteDatabase, where: DatabaseTable.Where): Cursor = db.query(
        DbContracts.WeekendEntry.table.name,
        DbContracts.WeekendEntry.table.columnsAsArray(),
        where.selection, where.args,
        null,null,null)

}
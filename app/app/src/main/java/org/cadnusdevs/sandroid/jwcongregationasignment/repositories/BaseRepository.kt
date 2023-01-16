package org.cadnusdevs.sandroid.jwcongregationasignment.repositories

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase

abstract class BaseRepository<TEntry>(ctx: Context, val entry: TEntry) where TEntry: DbContracts.EntryWithTable<*>{

    private var dbHelper = AppDbHelper(ctx)

    fun db(writeMode: Boolean = false): SQLiteDatabase {
        return if(writeMode) dbHelper.writableDatabase else dbHelper.readableDatabase
    }

    fun query(db: SQLiteDatabase, where: DatabaseTable.Where): Cursor = db.query(
        entry.table.name,
        entry.table.columnsAsArray(),
        where.selection, where.args,
        null,null,null)

    fun table() = entry.table.name
}
package org.cadnusdevs.sandroid.jwcongregationasignment.repositories

import android.content.ContentValues
import android.content.Context
import android.provider.BaseColumns
import org.cadnusdevs.sandroid.jwcongregationasignment.dbMock
import org.cadnusdevs.sandroid.jwcongregationasignment.models.Brother

class BrotherRepository (ctx: Context){

    var dbHelper = AppDbHelper(ctx)
    var brotherEntry = DbContracts.BrotherEntry;

    fun insert(brother: Brother) {
        //        dbMock.brothers.add(brother)
        // Gets the data repository in write mode
        val db = dbHelper.writableDatabase

        // Insert the new row, returning the primary key value of the new row
        brother.id = db?.insert(brotherEntry.TABLE_NAME, null, buildDbValues(brother))!!
    }

    fun select(predicate: (Brother) -> Boolean): Brother? {
        return this.selectAll().find(predicate)
    }

    fun update(brother: Brother) {
//        var brotherFromDb = this.select { x-> x.name == brother.name }
//        brotherFromDb?.let {
//            it.canBeUsher = brother.canBeUsher
//            it.canBeMicrophone = brother.canBeMicrophone
//            it.canBeComputer = brother.canBeComputer
//            it.canBeSoundSystem = brother.canBeSoundSystem
//        }

        val db = dbHelper.writableDatabase

        // Which row to update, based on the title
        val selection = "${BaseColumns._ID} LIKE ?"
        val selectionArgs = arrayOf(brother.id.toString())
        var count = db.update(
            brotherEntry.TABLE_NAME,
            buildDbValues(brother),
            selection,
            selectionArgs)
    }

    fun delete(brother: Brother) {
        dbMock.brothers.remove(this.select { x -> x.name == brother.name })
    }

    fun selectAll(): List<Brother> {
//        return dbMock.brothers;

        val db = dbHelper.readableDatabase

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        val projection = arrayOf(
            BaseColumns._ID,
            brotherEntry.COLUMN_NAME,
            brotherEntry.COLUMN_USHER,
            brotherEntry.COLUMN_MICROPHONE,
            brotherEntry.COLUMN_COMPUTER,
            brotherEntry.COLUMN_SOUND_SYSTEM)

        // How you want the results sorted in the resulting Cursor
        val sortOrder = "${brotherEntry.COLUMN_NAME} DESC"

        val cursor = db.query(
            brotherEntry.TABLE_NAME,   // The table to query
            projection,             // The array of columns to return (pass null to get all)
            null,              // The columns for the WHERE clause
            null,          // The values for the WHERE clause
            null,                   // don't group the rows
            null,                   // don't filter by row groups
            sortOrder               // The sort order
        )
        var brothers = ArrayList<Brother>()
        with(cursor) {
            while (moveToNext()) {
                val brother = Brother(
                    getLong(getColumnIndexOrThrow(BaseColumns._ID)),
                    getString(getColumnIndexOrThrow(brotherEntry.COLUMN_NAME)),
                    getInt(getColumnIndexOrThrow(brotherEntry.COLUMN_USHER)) == 1,
                    getInt(getColumnIndexOrThrow(brotherEntry.COLUMN_MICROPHONE)) == 1,
                    getInt(getColumnIndexOrThrow(brotherEntry.COLUMN_COMPUTER)) == 1,
                    getInt(getColumnIndexOrThrow(brotherEntry.COLUMN_SOUND_SYSTEM)) == 1
                )
                brothers.add(brother)
            }
        }
        cursor.close()
        return brothers
    }

    private fun buildDbValues(brother: Brother)  = ContentValues().apply {
        put(brotherEntry.COLUMN_NAME, brother.name)
        put(brotherEntry.COLUMN_USHER, dbInt(brother.canBeUsher))
        put(brotherEntry.COLUMN_MICROPHONE, dbInt(brother.canBeMicrophone))
        put(brotherEntry.COLUMN_COMPUTER, dbInt(brother.canBeComputer))
        put(brotherEntry.COLUMN_SOUND_SYSTEM, dbInt(brother.canBeSoundSystem))
    }

    private fun dbInt(value: Boolean): Int? {
        return if(value) 1 else 0;
    }
}
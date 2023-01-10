package org.cadnusdevs.sandroid.jwcongregationasignment.repositories

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.provider.BaseColumns
import org.cadnusdevs.sandroid.jwcongregationasignment.models.Brother

class BrotherRepository (ctx: Context){

    private var dbHelper = AppDbHelper(ctx)
    private var brotherEntry = DbContracts.BrotherEntry;

    fun insert(brother: Brother) {
        val db = dbHelper.writableDatabase
        brother.id = db?.insert(brotherEntry.TABLE_NAME, null, buildDbValues(brother))!!
    }

    fun select(predicate: (Brother) -> Boolean): Brother? {
        return this.selectAll().find(predicate)
    }

    fun update(brother: Brother) {

        val db = dbHelper.writableDatabase
        val selection = "${BaseColumns._ID} LIKE ?"
        val selectionArgs = arrayOf(brother.id.toString())
        db.update(
            brotherEntry.TABLE_NAME,
            buildDbValues(brother),
            selection,
            selectionArgs)
    }

    fun delete(brother: Brother) {
        val db = dbHelper.writableDatabase
        val selection = "${BaseColumns._ID} LIKE ?"
        val selectionArgs = arrayOf(brother.id.toString())
        db.delete(brotherEntry.TABLE_NAME, selection, selectionArgs)
    }

    fun selectAll(): List<Brother> {

        val db = dbHelper.readableDatabase
        val projection = arrayOf(
            BaseColumns._ID,
            brotherEntry.COLUMN_NAME,
            brotherEntry.COLUMN_USHER,
            brotherEntry.COLUMN_MICROPHONE,
            brotherEntry.COLUMN_COMPUTER,
            brotherEntry.COLUMN_SOUND_SYSTEM)

        val sortOrder = "${brotherEntry.COLUMN_NAME} ASC"

        val cursor = db.query(
            brotherEntry.TABLE_NAME,
            projection,
            null,
            null,
            null,
            null,
            sortOrder
        )
        var brothers = ArrayList<Brother>()
        with(cursor) {
            while (moveToNext()) {
                val brother = Brother(
                    getLong(getColumnIndexOrThrow(BaseColumns._ID)),
                    getString(getColumnIndexOrThrow(brotherEntry.COLUMN_NAME)),
                    intToBool(getInt(getColumnIndexOrThrow(brotherEntry.COLUMN_USHER))),
                    intToBool(getInt(getColumnIndexOrThrow(brotherEntry.COLUMN_MICROPHONE))),
                    intToBool(getInt(getColumnIndexOrThrow(brotherEntry.COLUMN_COMPUTER))),
                    intToBool(getInt(getColumnIndexOrThrow(brotherEntry.COLUMN_SOUND_SYSTEM)))
                )
                brothers.add(brother)
            }
        }
        cursor.close()
        return brothers
    }

    private fun buildDbValues(brother: Brother)  = ContentValues().apply {
        put(brotherEntry.COLUMN_NAME, brother.name)
        put(brotherEntry.COLUMN_USHER, boolToInt(brother.canBeUsher))
        put(brotherEntry.COLUMN_MICROPHONE, boolToInt(brother.canBeMicrophone))
        put(brotherEntry.COLUMN_COMPUTER, boolToInt(brother.canBeComputer))
        put(brotherEntry.COLUMN_SOUND_SYSTEM, boolToInt(brother.canBeSoundSystem))
    }

    private fun boolToInt(value: Boolean): Int? {
        return if(value) 1 else 0;
    }

    private fun intToBool(value: Int): Boolean {
        return value == 1
    }

    companion object{
        private var brotherTemp: List<Brother>? = null
        private var cursorTemp: Cursor? = null

        fun setDbInfo(cursor: Cursor, brothers: List<Brother>){
            cursorTemp = cursor
            brotherTemp = brothers
        }

        fun getBrother(columnName: String): Brother? {
            if(cursorTemp == null || brotherTemp == null || brotherTemp?.isEmpty() == true)
                throw Exception("Should set cursor and brothers using BrotherRepository.setDbInfo(cursor: Cursor, brothers: List<Brother>)")
            cursorTemp?.let{
                var brotherId = it.getLong(it.getColumnIndexOrThrow(columnName))
                return brotherTemp?.firstOrNull { x-> x.id == brotherId  }
            }
            return null
        }
    }
}
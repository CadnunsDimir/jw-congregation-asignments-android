package org.cadnusdevs.sandroid.jwcongregationasignment.repositories

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import org.cadnusdevs.sandroid.jwcongregationasignment.DateUtils
import org.cadnusdevs.sandroid.jwcongregationasignment.models.Brother
import org.cadnusdevs.sandroid.jwcongregationasignment.models.Weekend

class WeekendRepository(ctx: Context, private val brothers: List<Brother>): BaseRepository(ctx) {
    fun listBetweenDates(
        month: DateUtils.ZeroBasedDate,
        nextMonth: DateUtils.ZeroBasedDate
    ): List<Weekend> {
        val where = DbContracts.WeekendEntry.whereBetweenDates(month,nextMonth)
        val cursor = query(db(), where)

        val weekends = ArrayList<Weekend>()

        with(cursor) {
            val c = DatabaseTable.PoweredCursor(this)
            val entry = DbContracts.WeekendEntry
            while (moveToNext()) {
                BrotherRepository.setDbInfo(this, brothers)
                val weekend = mapEntity(c,entry)
                weekends.add(weekend)
            }
        }
        cursor.close()
        return weekends
    }

    private fun mapEntity(c: DatabaseTable.PoweredCursor, entry: DbContracts.WeekendEntry) = Weekend(
        DateUtils.ZeroBasedDate(
            c.number(entry.YEAR),
            c.number(entry.MONTH),
            c.number(entry.DAY)),
        BrotherRepository.getBrother(entry.PRESIDENT_BROTHER_ID),
        BrotherRepository.getBrother(entry.READER_BROTHER_ID),
        Weekend.Speech(
            c.text(entry.SPEECH_TITLE),
            c.text(entry.SPEECH_SPEAKER_NAME)))

    fun saveAll(weekends: List<Weekend>) {
        weekends.forEach {
            insert(it)
        }
        virtualDB.addAll(weekends)
    }

     fun insert(weekend: Weekend){
        val weekendId = db(true).insert(DbContracts.WeekendEntry.table.name, null, DbContracts.WeekendEntry.values(weekend))
        Log.println(Log.INFO,"WeekendRepository",
            if(weekendId > 0) "entity saved under id=$weekendId"
            else "error on save entity ${weekend.toString()}")
    }

    fun getFromDate(date: DateUtils.ZeroBasedDate): Weekend? {
        val where = DbContracts.WeekendEntry.whereDate(date)
        val cursor = query(db(), where)

        var weekend: Weekend? = null

        with(cursor) {
            val c = DatabaseTable.PoweredCursor(this)
            val entry = DbContracts.WeekendEntry
            while (moveToNext()) {
                BrotherRepository.setDbInfo(this, brothers)
                weekend = mapEntity(c, entry)
            }
        }
        cursor.close()
        return weekend
    }

    fun update(weekend: Weekend) {
        val where = DbContracts.WeekendEntry.whereDate(weekend.date)
        val entitiesUpdated = db(true).update(
            DbContracts.WeekendEntry.table.name,
            DbContracts.WeekendEntry.values(weekend),
            where.selection,
            where.args
        )
        Log.println(Log.INFO,"WeekendRepository",
            if(entitiesUpdated > 0) "$entitiesUpdated Weekend entities updated"
            else "error on update entity")
    }

    fun recreateTable() {
        val db = db(true)
        db.execSQL(DbContracts.WeekendEntry.table.sqlDropTable())
        db.execSQL(DbContracts.WeekendEntry.table.sqlCreateTable())
    }

    companion object{
        private val virtualDB = ArrayList<Weekend>()
    }
}
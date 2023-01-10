package org.cadnusdevs.sandroid.jwcongregationasignment.repositories

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import org.cadnusdevs.sandroid.jwcongregationasignment.DateUtils
import org.cadnusdevs.sandroid.jwcongregationasignment.models.Brother
import org.cadnusdevs.sandroid.jwcongregationasignment.models.Weekend

class WeekendRepository(ctx: Context, private val brothers: List<Brother>) {
    private var dbHelper = AppDbHelper(ctx)
    fun listBetweenDates(
        month: DateUtils.ZeroBasedDate,
        nextMonth: DateUtils.ZeroBasedDate
    ): List<Weekend> {
//        return virtualDB.filter {
//            val date = it.date.asTimeStamp()
//            return@filter date >= month.asTimeStamp() && date < nextMonth.asTimeStamp()
//        }

        val where = DbContracts.WeekendEntry.whereBetweenDates(month,nextMonth)
        val cursor = query(dbHelper.readableDatabase, where)

        val weekends = ArrayList<Weekend>()

        with(cursor) {
            val c = DatabaseTable.Cursor(this)
            val entry = DbContracts.WeekendEntry
            while (moveToNext()) {
                BrotherRepository.setDbInfo(this, brothers)
                val weekend = Weekend(
                    DateUtils.ZeroBasedDate(
                        c.number(entry.YEAR),
                        c.number(entry.MONTH),
                        c.number(entry.DAY)),
                    BrotherRepository.getBrother(entry.PRESIDENT_BROTHER_ID),
                    BrotherRepository.getBrother(entry.READER_BROTHER_ID),
                    Weekend.Speech(
                        c.text(entry.SPEECH_TITLE),
                        c.text(entry.SPEECH_TITLE)))
                weekends.add(weekend)
            }
        }
        return weekends
    }

    fun saveAll(weekends: List<Weekend>) {
        weekends.forEach {
            insert(it)
        }
        virtualDB.addAll(weekends)
    }

     fun insert(weekend: Weekend){
        val db = dbHelper.writableDatabase
        val weekendId = db?.insert(DbContracts.WeekendEntry.table.name, null, DbContracts.WeekendEntry.values(weekend))
        Log.println(Log.INFO,"WeekendRepository",
            if(weekendId != null && weekendId > 0 ) "entity saved under id=$weekendId"
            else "error on save entity ${weekend.toString()}")
    }

    fun getFromDate(date: DateUtils.ZeroBasedDate): Weekend? {
        val where = DbContracts.WeekendEntry.whereDate(date)
        val cursor = query(dbHelper.readableDatabase, where)

        var weekend: Weekend? = null

        with(cursor) {
            val c = DatabaseTable.Cursor(this)
            val entry = DbContracts.WeekendEntry
            while (moveToNext()) {
                BrotherRepository.setDbInfo(this, brothers)
                weekend = Weekend(
                    DateUtils.ZeroBasedDate(
                        c.number(entry.YEAR),
                        c.number(entry.MONTH),
                        c.number(entry.DAY)),
                    BrotherRepository.getBrother(entry.PRESIDENT_BROTHER_ID),
                    BrotherRepository.getBrother(entry.READER_BROTHER_ID),
                    Weekend.Speech(
                        c.text(entry.SPEECH_TITLE),
                        c.text(entry.SPEECH_TITLE))
                )
            }
        }

        return weekend
    }

    private fun query(db: SQLiteDatabase, where: DatabaseTable.Where) = db.query(
        DbContracts.WeekendEntry.table.name,
        DbContracts.WeekendEntry.table.columnsAsArray(),
        where.selection, where.args,
        null,null,null)

    fun update(weekend: Weekend) {
        val db = dbHelper.writableDatabase
        val where = DbContracts.WeekendEntry.whereDate(weekend.date)
        val entitiesUpdated = db?.update(
            DbContracts.WeekendEntry.table.name,
            DbContracts.WeekendEntry.values(weekend),
            where.selection,
            where.args
        )
        Log.println(Log.INFO,"WeekendRepository",
            if(entitiesUpdated != null && entitiesUpdated > 0 ) "$entitiesUpdated Weekend entities updated"
            else "error on update entity")

//        val position = virtualDB.indexOf(virtualDB.first { it.date.asTimeStamp() == weekend.date.asTimeStamp() })
//        virtualDB[position] = weekend
    }

    companion object{
        private val virtualDB = ArrayList<Weekend>()
    }
}
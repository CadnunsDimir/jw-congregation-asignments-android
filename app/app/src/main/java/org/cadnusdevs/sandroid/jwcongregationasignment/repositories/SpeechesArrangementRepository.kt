package org.cadnusdevs.sandroid.jwcongregationasignment.repositories

import android.content.Context
import android.util.Log
import org.cadnusdevs.sandroid.jwcongregationasignment.DateUtils
import org.cadnusdevs.sandroid.jwcongregationasignment.models.SpeechesArrangement

class SpeechesArrangementRepository(ctx: Context) : BaseRepository(ctx){
    private val entry = DbContracts.SpeechesArrangementEntry
    fun getFromMonth(date: DateUtils.ZeroBasedDate):  SpeechesArrangement? {
        val where = DatabaseTable
            .Where(entry.YEAR, date.year)
            .and(entry.MONTH, date.monthZeroBased)
        val cursor = query(db(), where)

        var entity: SpeechesArrangement? = null

        with(cursor) {
            val c = DatabaseTable.PoweredCursor(this)
            while (moveToNext()) {
                entity = mapEntity(c, entry)
            }
        }
        cursor.close()
        return entity
    }

    private fun mapEntity(
        c: DatabaseTable.PoweredCursor,
        entry: DbContracts.SpeechesArrangementEntry
    ) = SpeechesArrangement(c.number(entry.YEAR), c.number(entry.MONTH), c.text(entry.INVITED_CONGREGATION))

    fun insert(arrangement: SpeechesArrangement){
        val id = db(true).insert(entry.table.name, null, entry.values(arrangement))
        Log.println(
            Log.INFO,"WeekendRepository",
            if(id > 0) "entity saved under id=$id"
            else "error on save entity $arrangement")
    }

    fun update(arrangement: SpeechesArrangement) {
        val where = DatabaseTable
            .Where(entry.YEAR, arrangement.year)
            .and(entry.MONTH, arrangement.month)
        val entitiesUpdated = db(true).update(
            entry.table.name,
            entry.values(arrangement),
            where.selection,
            where.args
        )
        Log.println(Log.INFO,"WeekendRepository",
            if(entitiesUpdated > 0) "$entitiesUpdated Weekend entities updated"
            else "error on update entity")
    }
}
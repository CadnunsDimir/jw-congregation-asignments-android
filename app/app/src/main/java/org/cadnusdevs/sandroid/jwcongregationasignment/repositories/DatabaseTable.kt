package org.cadnusdevs.sandroid.jwcongregationasignment.repositories

class DatabaseTable(val name: String, vararg val columns: Column) {
    class Column(val name: String, val dataTypeAndMetadata: String = DbContracts.ColumnType.STRING)
    class Where(column: String, value: Any){
        var selection = "$column = ?"
        var args = arrayOf(value.toString())

        fun and(column: String, value: Any): Where {
            selection = "$selection AND $column = ?"
            args = arrayOf(*args, value.toString())
            return this
        }

        fun between(column: String, minValue: Int, finishValue: Int): Where {
            selection = "$selection AND $column >= ? AND $column < ?"
            args = arrayOf(*args, minValue.toString(), finishValue.toString())
            return this
        }
        companion object{
            fun getInstance(selection:String, args: Array<String>): Where {
                val where = Where("","")
                where.selection = selection
                where.args = args
                return where
            }
        }
    }

    class PoweredCursor(private val innerCursor: android.database.Cursor?) {
        fun number(columnName: String): Int {
            with(innerCursor!!) {
                return getInt(getColumnIndexOrThrow(columnName))
            }
        }
        fun text(columnName: String): String {
            with(innerCursor!!) {
                return getString(getColumnIndexOrThrow(columnName))
            }
        }
    }

    fun sqlCreateTable(): String {
        var sql = "CREATE TABLE $name ("
        sql += columns.joinToString { "${it.name} ${it.dataTypeAndMetadata}" }
        sql += ")"
        return sql
    }

    fun sqlDropTable() = "DROP TABLE IF EXISTS $name"

    fun columnsAsArray() = columns.map { it.name }.toTypedArray()
}
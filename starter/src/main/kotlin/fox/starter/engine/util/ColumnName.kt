package fox.starter.engine.util

import fox.starter.engine.enums.ProcessStatus
import java.sql.ResultSet
import java.time.LocalDateTime
import java.util.UUID

interface ColumnName {
    val columnName: String
}

fun ResultSet.getLong(column: ColumnName): Long = this.getLong(column.columnName)
fun ResultSet.getLocalDateTime(column: ColumnName): LocalDateTime =
    this.getTimestamp(column.columnName).toLocalDateTime()

fun ResultSet.getInt(column: ColumnName): Int = this.getInt(column.columnName)
fun ResultSet.getString(column: ColumnName): String = this.getString(column.columnName)
fun ResultSet.getUUID(column: ColumnName): UUID = this.getObject(column.columnName, UUID::class.java)
fun ResultSet.getBoolean(column: ColumnName): Boolean = this.getBoolean(column.columnName)
fun ResultSet.getProcessStatus(column: ColumnName): ProcessStatus =
    this.getObject(column.columnName, ProcessStatus::class.java)

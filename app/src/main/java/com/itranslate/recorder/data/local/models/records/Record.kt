package com.itranslate.recorder.data.local.models.records

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Entity representing a record item
 *
 * @param recordId is auto generated primary key and record id
 * @param recordName is the record title
 *
 * NOTE: [recordName] is also considered as indices in the records table. This is to make faster
 * queries possible when we need to use queries such as search by [recordName]s.
 */
@Entity(
    tableName = "records",
    indices = [Index(value = ["record_name"])]
)
data class Record(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "record_id")
    val recordId: Int? = null,

    @ColumnInfo(name = "record_name") val recordName: String,
    @ColumnInfo(name = "record_path") val recordPath: String,
    @ColumnInfo(name = "record_duration") val recordDuration: String
)

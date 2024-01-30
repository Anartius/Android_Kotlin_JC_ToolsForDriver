package com.example.toolsfordriver.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(tableName = "trips_table")
data class TripDBModel(
    @PrimaryKey
    val id: UUID = UUID.randomUUID(),

    @ColumnInfo(name = "user_id")
    val userId: String,

    @ColumnInfo(name = "start_time")
    val startTime: Long? = null,

    @ColumnInfo(name = "end_time")
    val endTime: Long? = null
)

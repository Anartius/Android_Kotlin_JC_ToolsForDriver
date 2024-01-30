package com.example.toolsfordriver.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.toolsfordriver.utils.ListStringConverter
import com.example.toolsfordriver.utils.MapConverter
import com.example.toolsfordriver.utils.UUIDConverter

@Database(
    entities = [TripDBModel::class, FreightDBModel::class],
    version = 8,
    exportSchema = false
)
@TypeConverters(
    UUIDConverter::class,
    MapConverter::class,
    ListStringConverter::class
)
abstract class TFDRoomDB : RoomDatabase() {

    abstract fun tfdRoomDao(): TFDRoomDAO
}
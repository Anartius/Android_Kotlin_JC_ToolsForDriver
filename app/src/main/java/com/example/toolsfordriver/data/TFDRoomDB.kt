package com.example.toolsfordriver.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.toolsfordriver.common.ListStringConverter
import com.example.toolsfordriver.common.MapConverter
import com.example.toolsfordriver.common.UUIDConverter

@Database(
    entities = [FreightDBModel::class],
    version = 10,
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
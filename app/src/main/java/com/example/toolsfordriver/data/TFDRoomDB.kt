package com.example.toolsfordriver.data

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.toolsfordriver.utils.UUIDConverter

@Database(entities = [TripDBModel::class], version = 3, exportSchema = false)
@TypeConverters(UUIDConverter::class)
abstract class TFDRoomDB : RoomDatabase() {

    abstract fun tfdRoomDao(): TFDRoomDAO
}
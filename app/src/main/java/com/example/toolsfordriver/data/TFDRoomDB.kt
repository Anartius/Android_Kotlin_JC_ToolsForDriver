package com.example.toolsfordriver.data

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.toolsfordriver.utils.UUIDConverter

@Database(entities = [TripDBModel::class], version = 2, exportSchema = false)
@TypeConverters(UUIDConverter::class)
abstract class TFDRoomDB : RoomDatabase() {

    abstract fun tfdRoomDao(): TFDRoomDAO

//    companion object {
//        private var INSTANCE: TFDRoomDB? = null
//        private val LOCK = Any()
//        private const val DB_NAME = "tfd_room.db"
//
//        fun getInstance(application: Application): TFDRoomDB {
//            INSTANCE?.let { return it }
//
//            synchronized(LOCK) {
//                INSTANCE?.let { return it }
//            }
//
//            val db = Room.databaseBuilder(
//                application,
//                TFDRoomDB::class.java,
//                DB_NAME
//            ).build()
//
//            INSTANCE = db
//
//            return db
//        }
//    }
}
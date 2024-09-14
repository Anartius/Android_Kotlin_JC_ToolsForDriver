package com.example.toolsfordriver.common

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MapConverter {
    private var gson = Gson()
    @TypeConverter
    fun fromMap(list: Map<Long, String>): String = gson.toJson(list)

    @TypeConverter
    fun toMap(value: String): Map<Long, String> {
        val listType = object : TypeToken<Map<Long, String>>() {}.type
        return gson.fromJson(value, listType)
    }
}
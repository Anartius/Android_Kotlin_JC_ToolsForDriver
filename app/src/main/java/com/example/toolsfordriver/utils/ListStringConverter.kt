package com.example.toolsfordriver.utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class ListStringConverter {
    private var gson = Gson()
    @TypeConverter
    fun fromMap(list: List<String>): String = gson.toJson(list)

    @TypeConverter
    fun toMap(value: String): List<String> {
        val listType = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(value, listType)
    }
}
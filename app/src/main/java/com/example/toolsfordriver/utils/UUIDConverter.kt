package com.example.toolsfordriver.utils

import androidx.room.TypeConverter
import java.util.UUID

class UUIDConverter {
    @TypeConverter
    fun fromUUID(uuid: UUID?): String = uuid.toString()

    @TypeConverter
    fun toUUID(string: String?): UUID? = UUID.fromString(string)
}
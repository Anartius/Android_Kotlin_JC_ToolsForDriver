package com.example.toolsfordriver.data

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import java.util.UUID

@Entity(tableName = "freights_table")
@Parcelize
data class FreightDBModel(

//    @PrimaryKey(autoGenerate = true)
//    val id: Int = 0,
    @PrimaryKey
    val id: UUID = UUID.randomUUID(),

    @ColumnInfo(name = "user_id")
    val userId: String,

    @ColumnInfo(name = "loads")
    val loads: Map<Long, String> = emptyMap(),

    @ColumnInfo(name = "unloads")
    val unloads: Map<Long, String> = emptyMap(),

    @ColumnInfo(name = "distance")
    val distance: Int? = null,

    @ColumnInfo(name = "picture_uri")
    val pictureUri: List<String> = emptyList(),

    @ColumnInfo(name = "notes")
    val notes: String? = null
): Parcelable

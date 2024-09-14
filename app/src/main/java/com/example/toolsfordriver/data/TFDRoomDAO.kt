package com.example.toolsfordriver.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TFDRoomDAO {

    @Query("SELECT * FROM freights_table WHERE user_id = :userId")
    fun getFreightsByUserId(userId: String): Flow<List<FreightDBModel>>

    @Query("SELECT * FROM freights_table WHERE id = :freightId LIMIT 1")
    suspend fun getFreightById(freightId: String): FreightDBModel

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFreight(freight: FreightDBModel)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateFreight(freight: FreightDBModel)

    @Delete
    suspend fun deleteFreight(freight: FreightDBModel)

    @Query("DELETE FROM freights_table")
    suspend fun deleteAllFreights()
}
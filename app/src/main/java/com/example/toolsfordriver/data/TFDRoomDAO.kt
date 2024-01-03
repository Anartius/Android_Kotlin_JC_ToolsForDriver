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
    @Query("SELECT * FROM trips_table")
    fun getTrips(): Flow<List<TripDBModel>>

    @Query("SELECT * FROM trips_table WHERE id = :tripId LIMIT 1")
    suspend fun getTripById(tripId: String): TripDBModel

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrip(trip: TripDBModel)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateTrip(trip: TripDBModel)

    @Delete
    suspend fun deleteTrip(trip: TripDBModel)

    @Query("DELETE FROM trips_table")
    suspend fun deleteAllTrips()
}
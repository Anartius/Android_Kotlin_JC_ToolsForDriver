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

    // trips_table
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


    // freights_table
    @Query("SELECT * FROM freights_table")
    fun getFreights(): Flow<List<FreightDBModel>>

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
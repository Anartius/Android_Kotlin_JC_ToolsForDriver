package com.example.toolsfordriver.repository

import com.example.toolsfordriver.data.TFDRoomDAO
import com.example.toolsfordriver.data.TripDBModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class Repository @Inject constructor(
    private val tfdRoomDAO: TFDRoomDAO
) {
    fun getAllTrips(): Flow<List<TripDBModel>> =
        tfdRoomDAO.getTrips().flowOn(Dispatchers.IO).conflate()

    suspend fun addTrip(trip: TripDBModel) = tfdRoomDAO.insertTrip(trip)

    suspend fun updateTrip(trip: TripDBModel) = tfdRoomDAO.updateTrip(trip)

    suspend fun deleteTrip(trip: TripDBModel) = tfdRoomDAO.deleteTrip(trip)

    suspend fun deleteAllTrips() = tfdRoomDAO.deleteAllTrips()
}
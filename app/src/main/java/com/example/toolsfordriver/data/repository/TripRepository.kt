package com.example.toolsfordriver.data.repository

import com.example.toolsfordriver.data.TFDRoomDAO
import com.example.toolsfordriver.data.TripDBModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class TripRepository @Inject constructor(
    private val tfdRoomDAO: TFDRoomDAO
) {
    fun getAllTripsByUserId(userId: String) =
        tfdRoomDAO.getTripsByUserId(userId).flowOn(Dispatchers.IO).conflate()

    suspend fun getTripById(tripId: String) = tfdRoomDAO.getTripById(tripId)

    suspend fun addTrip(trip: TripDBModel) = tfdRoomDAO.insertTrip(trip)

    suspend fun updateTrip(trip: TripDBModel) = tfdRoomDAO.updateTrip(trip)

    suspend fun deleteTrip(trip: TripDBModel) = tfdRoomDAO.deleteTrip(trip)

    suspend fun deleteAllTrips() = tfdRoomDAO.deleteAllTrips()

}
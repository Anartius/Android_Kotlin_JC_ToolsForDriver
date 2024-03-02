package com.example.toolsfordriver.repository

import com.example.toolsfordriver.data.FreightDBModel
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

    // trip
    fun getAllTripsByUserId(userId: String) =
        tfdRoomDAO.getTripsByUserId(userId).flowOn(Dispatchers.IO).conflate()

    suspend fun getTripById(tripId: String) = tfdRoomDAO.getTripById(tripId)

    suspend fun addTrip(trip: TripDBModel) = tfdRoomDAO.insertTrip(trip)

    suspend fun updateTrip(trip: TripDBModel) = tfdRoomDAO.updateTrip(trip)

    suspend fun deleteTrip(trip: TripDBModel) = tfdRoomDAO.deleteTrip(trip)

    suspend fun deleteAllTrips() = tfdRoomDAO.deleteAllTrips()


    // freight
    fun getAllFreightsByUserId(userId: String): Flow<List<FreightDBModel>> =
        tfdRoomDAO.getFreightsByUserId(userId).flowOn(Dispatchers.IO).conflate()

    suspend fun getFreightById(freightId: String) = tfdRoomDAO.getFreightById(freightId)

    suspend fun addFreight(freight: FreightDBModel) = tfdRoomDAO.insertFreight(freight)

    suspend fun updateFreight(freight: FreightDBModel) = tfdRoomDAO.updateFreight(freight)

    suspend fun deleteFreight(freight: FreightDBModel) = tfdRoomDAO.deleteFreight(freight)

    suspend fun deleteAllFreights() = tfdRoomDAO.deleteAllFreights()
}
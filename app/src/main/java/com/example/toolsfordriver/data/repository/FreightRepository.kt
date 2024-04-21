package com.example.toolsfordriver.data.repository

import com.example.toolsfordriver.data.FreightDBModel
import com.example.toolsfordriver.data.TFDRoomDAO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class FreightRepository @Inject constructor(
    private val tfdRoomDAO: TFDRoomDAO
) {
    fun getAllFreightsByUserId(userId: String): Flow<List<FreightDBModel>> =
        tfdRoomDAO.getFreightsByUserId(userId).flowOn(Dispatchers.IO).conflate()

    suspend fun getFreightById(freightId: String) = tfdRoomDAO.getFreightById(freightId)

    suspend fun addFreight(freight: FreightDBModel) = tfdRoomDAO.insertFreight(freight)

    suspend fun updateFreight(freight: FreightDBModel) = tfdRoomDAO.updateFreight(freight)

    suspend fun deleteFreight(freight: FreightDBModel) = tfdRoomDAO.deleteFreight(freight)

    suspend fun deleteAllFreights() = tfdRoomDAO.deleteAllFreights()
}
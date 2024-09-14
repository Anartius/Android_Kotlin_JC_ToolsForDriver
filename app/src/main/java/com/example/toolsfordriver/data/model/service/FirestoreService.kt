package com.example.toolsfordriver.data.model.service

import com.example.toolsfordriver.data.model.Freight
import com.example.toolsfordriver.data.model.Trip
import com.example.toolsfordriver.data.model.User
import kotlinx.coroutines.flow.Flow

interface FirestoreService {

    // Users Collection
    val users: Flow<List<User>>
//    suspend fun getUser(userId: String): User?
    suspend fun saveUser(user: User)
    suspend fun updateUser(user: User)
    suspend fun deleteUser(userId: String)

    // Trips collection
    val trips: Flow<List<Trip>>
    suspend fun getTrip(tripId: String): Trip?
    suspend fun saveTrip(trip: Trip)
    suspend fun updateTrip(trip: Trip)
    suspend fun deleteTrip(tripId: String)

    // Freights collection
    val freights: Flow<List<Freight>>
    suspend fun getFreight(freightId: String): Freight?
    suspend fun saveFreight(freight: Freight)
    suspend fun updateFreight(freight: Freight)
    suspend fun deleteFreight(freightId: String)
}
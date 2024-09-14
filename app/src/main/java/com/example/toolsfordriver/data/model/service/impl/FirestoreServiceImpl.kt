package com.example.toolsfordriver.data.model.service.impl

import com.example.toolsfordriver.data.model.Freight
import com.example.toolsfordriver.data.model.Trip
import com.example.toolsfordriver.data.model.User
import com.example.toolsfordriver.data.model.service.AccountService
import com.example.toolsfordriver.data.model.service.FirestoreService
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.dataObjects
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirestoreServiceImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val auth: AccountService
) : FirestoreService {

    // Users collection
    @OptIn(ExperimentalCoroutinesApi::class)
    override val users: Flow<List<User>>
        get() = auth.currentUser.flatMapLatest { user ->
            firestore
                .collection(USER_COLLECTION)
                .whereEqualTo(USER_ID_FIELD, user.id)
                .dataObjects()
        }

    override suspend fun saveUser(user: User) {
        val updatedUser = user.copy(id = auth.currentUserId)
        firestore.collection(USER_COLLECTION).add(updatedUser).await()
    }

    override suspend fun updateUser(user: User) {
        firestore.collection(USER_COLLECTION).document(user.id).set(user).await()
    }

    override suspend fun deleteUser(userId: String) {
        firestore.collection(USER_COLLECTION).document().delete().await()
    }

    // Trips collection
    @OptIn(ExperimentalCoroutinesApi::class)
    override val trips: Flow<List<Trip>>
        get() = auth.currentUser.flatMapLatest { user ->
            firestore
                .collection(TRIP_COLLECTION)
                .whereEqualTo(USER_ID_FIELD, user.id)
                .orderBy(START_TIME_FIELD, Query.Direction.ASCENDING)
                .dataObjects()
        }

    override suspend fun getTrip(tripId: String): Trip? =
        firestore.collection(TRIP_COLLECTION).document(tripId).get().await().toObject()

    override suspend fun saveTrip(trip: Trip) {
        val updatedTrip = trip.copy(id = auth.currentUserId)
        firestore.collection(TRIP_COLLECTION).add(updatedTrip).await()
    }

    override suspend fun updateTrip(trip: Trip) {
        firestore.collection(TRIP_COLLECTION).document(trip.id).set(trip).await()
    }

    override suspend fun deleteTrip(tripId: String) {
        firestore.collection(TRIP_COLLECTION).document(tripId).delete().await()
    }

    // Freights collection
    @OptIn(ExperimentalCoroutinesApi::class)
    override val freights: Flow<List<Freight>>
        get() = auth.currentUser.flatMapLatest { user ->
            firestore
                .collection(FREIGHT_COLLECTION)
                .whereEqualTo(USER_ID_FIELD, user.id)
                .dataObjects()
        }

    override suspend fun getFreight(freightId: String): Freight? =
        firestore.collection(FREIGHT_COLLECTION).document(freightId).get().await().toObject()

    override suspend fun saveFreight(freight: Freight) {
        val updatedFreight = freight.copy(id = auth.currentUserId)
        firestore.collection(FREIGHT_COLLECTION).add(updatedFreight).await()
    }

    override suspend fun updateFreight(freight: Freight) {
        firestore.collection(FREIGHT_COLLECTION).document(freight.id).set(freight).await()
    }

    override suspend fun deleteFreight(freightId: String) {
        firestore.collection(FREIGHT_COLLECTION).document(freightId).delete().await()
    }

    companion object {
        private const val USER_ID_FIELD = "userId"
        private const val START_TIME_FIELD = "startTime"
        private const val USER_COLLECTION = "users"
        private const val TRIP_COLLECTION = "trips"
        private const val FREIGHT_COLLECTION = "freights"
    }
}
package com.example.eventshop.repository

import com.example.eventshop.model.Booking
import com.example.eventshop.model.Hall
import com.example.eventshop.utils.Constants
import com.example.eventshop.utils.Resource
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class BookingRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val bookingsRef = firestore.collection(Constants.BOOKINGS_COLLECTION)
    private val hallsRef = firestore.collection(Constants.HALLS_COLLECTION)

    suspend fun addBooking(booking: Booking): Resource<Unit> {
        return try {
            val docRef = bookingsRef.document()
            val bookingWithId = booking.copy(id = docRef.id)
            docRef.set(bookingWithId).await()
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to create booking")
        }
    }

    fun getUserBookings(userId: String): Flow<Resource<List<Booking>>> = callbackFlow {
        trySend(Resource.Loading)
        val listener = bookingsRef
            .whereEqualTo("userId", userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(Resource.Error(error.message ?: "Failed to fetch bookings"))
                    return@addSnapshotListener
                }
                val bookings = snapshot?.toObjects(Booking::class.java)
                    ?.sortedByDescending { it.createdAt }
                    ?: emptyList()
                trySend(Resource.Success(bookings))
            }
        awaitClose { listener.remove() }
    }

    fun getHalls(): Flow<Resource<List<Hall>>> = callbackFlow {
        trySend(Resource.Loading)
        val listener = hallsRef
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(Resource.Error(error.message ?: "Failed to fetch halls"))
                    return@addSnapshotListener
                }
                val halls = snapshot?.toObjects(Hall::class.java) ?: emptyList()
                trySend(Resource.Success(halls))
            }
        awaitClose { listener.remove() }
    }

    suspend fun addHall(hall: Hall): Resource<Unit> {
        return try {
            val docRef = hallsRef.document()
            val hallWithId = hall.copy(id = docRef.id)
            docRef.set(hallWithId).await()
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to add hall")
        }
    }
}

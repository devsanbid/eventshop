package com.example.eventshop.repository

import com.example.eventshop.model.Order
import com.example.eventshop.utils.Constants
import com.example.eventshop.utils.Resource
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class OrderRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val ordersRef = firestore.collection(Constants.ORDERS_COLLECTION)

    suspend fun placeOrder(order: Order): Resource<Unit> {
        return try {
            val docRef = ordersRef.document()
            val orderWithId = order.copy(id = docRef.id)
            docRef.set(orderWithId).await()
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to place order")
        }
    }

    fun getUserOrders(userId: String): Flow<Resource<List<Order>>> = callbackFlow {
        trySend(Resource.Loading)
        val listener = ordersRef
            .whereEqualTo("userId", userId)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(Resource.Error(error.message ?: "Failed to fetch orders"))
                    return@addSnapshotListener
                }
                val orders = snapshot?.toObjects(Order::class.java)
                    ?.sortedByDescending { it.createdAt }
                    ?: emptyList()
                trySend(Resource.Success(orders))
            }
        awaitClose { listener.remove() }
    }
}

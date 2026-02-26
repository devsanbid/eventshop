package com.example.eventshop.repository

import com.example.eventshop.model.User
import com.example.eventshop.utils.Constants
import com.example.eventshop.utils.Resource
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class UserRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val usersRef = firestore.collection(Constants.USERS_COLLECTION)

    fun getAllUsers(): Flow<Resource<List<User>>> = callbackFlow {
        trySend(Resource.Loading)
        val listener = usersRef.addSnapshotListener { snapshot, error ->
            if (error != null) {
                trySend(Resource.Error(error.message ?: "Failed to fetch users"))
                return@addSnapshotListener
            }
            val users = snapshot?.toObjects(User::class.java) ?: emptyList()
            trySend(Resource.Success(users))
        }
        awaitClose { listener.remove() }
    }
}

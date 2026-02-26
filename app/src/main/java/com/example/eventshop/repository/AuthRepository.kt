package com.example.eventshop.repository

import com.example.eventshop.model.User
import com.example.eventshop.utils.Constants
import com.example.eventshop.utils.Resource
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class AuthRepository {

    private val auth = FirebaseAuth.getInstance()
    private val firestore = FirebaseFirestore.getInstance()

    val currentUser get() = auth.currentUser

    suspend fun login(email: String, password: String): Resource<User> {
        return try {
            val result = auth.signInWithEmailAndPassword(email, password).await()
            val uid = result.user?.uid ?: return Resource.Error("Login failed")
            val doc = firestore.collection(Constants.USERS_COLLECTION).document(uid).get().await()
            val user = doc.toObject(User::class.java) ?: return Resource.Error("User data not found")
            Resource.Success(user)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Login failed")
        }
    }

    suspend fun register(name: String, email: String, password: String): Resource<User> {
        return try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            val uid = result.user?.uid ?: return Resource.Error("Registration failed")
            val user = User(uid = uid, name = name, email = email, role = Constants.ROLE_USER)
            firestore.collection(Constants.USERS_COLLECTION).document(uid).set(user).await()
            Resource.Success(user)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Registration failed")
        }
    }

    suspend fun getCurrentUserData(): Resource<User> {
        return try {
            val uid = auth.currentUser?.uid ?: return Resource.Error("Not logged in")
            val doc = firestore.collection(Constants.USERS_COLLECTION).document(uid).get().await()
            val user = doc.toObject(User::class.java) ?: return Resource.Error("User data not found")
            Resource.Success(user)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to fetch user data")
        }
    }

    suspend fun updateName(newName: String): Resource<Unit> {
        return try {
            val uid = auth.currentUser?.uid ?: return Resource.Error("Not logged in")
            firestore.collection(Constants.USERS_COLLECTION).document(uid)
                .update("name", newName).await()
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to update name")
        }
    }

    suspend fun changePassword(currentPassword: String, newPassword: String): Resource<Unit> {
        return try {
            val user = auth.currentUser ?: return Resource.Error("Not logged in")
            val credential = EmailAuthProvider.getCredential(user.email!!, currentPassword)
            user.reauthenticate(credential).await()
            user.updatePassword(newPassword).await()
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to change password")
        }
    }

    fun logout() {
        auth.signOut()
    }
}

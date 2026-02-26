package com.example.eventshop.repository

import com.example.eventshop.model.Product
import com.example.eventshop.utils.Constants
import com.example.eventshop.utils.Resource
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class ProductRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val productsRef = firestore.collection(Constants.PRODUCTS_COLLECTION)

    suspend fun addProduct(product: Product): Resource<Unit> {
        return try {
            val docRef = productsRef.document()
            val productWithId = product.copy(id = docRef.id)
            docRef.set(productWithId).await()
            Resource.Success(Unit)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to add product")
        }
    }

    fun getProducts(): Flow<Resource<List<Product>>> = callbackFlow {
        trySend(Resource.Loading)
        val listener = productsRef
            .orderBy("createdAt", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    trySend(Resource.Error(error.message ?: "Failed to fetch products"))
                    return@addSnapshotListener
                }
                val products = snapshot?.toObjects(Product::class.java) ?: emptyList()
                trySend(Resource.Success(products))
            }
        awaitClose { listener.remove() }
    }

    suspend fun getProductById(productId: String): Resource<Product> {
        return try {
            val doc = productsRef.document(productId).get().await()
            val product = doc.toObject(Product::class.java)
                ?: return Resource.Error("Product not found")
            Resource.Success(product)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to fetch product")
        }
    }
}

package com.example.eventshop.utils

import android.content.Context
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import java.util.concurrent.TimeUnit

object CloudinaryUploader {

    private val client = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    suspend fun uploadImage(context: Context, imageUri: Uri): Resource<String> {
        return withContext(Dispatchers.IO) {
            try {
                val inputStream = context.contentResolver.openInputStream(imageUri)
                    ?: return@withContext Resource.Error("Failed to read image")

                val bytes = inputStream.readBytes()
                inputStream.close()

                val requestBody = MultipartBody.Builder()
                    .setType(MultipartBody.FORM)
                    .addFormDataPart(
                        "file",
                        "image.jpg",
                        bytes.toRequestBody("image/*".toMediaType())
                    )
                    .addFormDataPart("upload_preset", Constants.CLOUDINARY_UPLOAD_PRESET)
                    .build()

                val request = Request.Builder()
                    .url(Constants.CLOUDINARY_UPLOAD_URL)
                    .post(requestBody)
                    .build()

                val response = client.newCall(request).execute()

                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    val json = JSONObject(responseBody ?: "")
                    val imageUrl = json.getString("secure_url")
                    Resource.Success(imageUrl)
                } else {
                    Resource.Error("Upload failed: ${response.message}")
                }
            } catch (e: Exception) {
                Resource.Error("Upload error: ${e.message}")
            }
        }
    }
}

package com.app.sampleproject.data.repository

import android.util.Log
import com.app.sampleproject.core.utils.Resource
import com.app.sampleproject.data.mapper.toDomain
import com.app.sampleproject.data.remote.ApiService
import com.app.sampleproject.domain.model.ContactsDomain
import com.app.sampleproject.domain.repository.ContactRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ContactRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : ContactRepository {

    override suspend fun getContacts(userId: String): Flow<Resource<ContactsDomain>> = flow {
        try {
            emit(Resource.Loading)
            Log.d("ContactRepository", "Fetching contacts for userId: $userId")

            val response = apiService.getContacts(userId)

            Log.d("ContactRepository", "Response code: ${response.code()}")
            Log.d("ContactRepository", "Response body: ${response.body()}")

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    val contactsDomain = body.toDomain()
                    Log.d("ContactRepository", "Contacts loaded: ${contactsDomain.reps.size} reps")
                    emit(Resource.Success(contactsDomain))
                } else {
                    Log.e("ContactRepository", "Response body is null")
                    emit(Resource.Error("Failed to load contacts: Empty response"))
                }
            } else {
                val errorMsg = "Network error: ${response.code()} - ${response.message()}"
                Log.e("ContactRepository", errorMsg)
                emit(Resource.Error(errorMsg))
            }
        } catch (e: Exception) {
            Log.e("ContactRepository", "Exception in getContacts: ${e.message}", e)
            emit(Resource.Error(e.message ?: "Unknown error occurred"))
        }
    }

    suspend fun getWhatsAppNumber(): Flow<Resource<String>> = flow {
        try {
            emit(Resource.Loading)
            Log.d("ContactRepository", "Fetching WhatsApp business number")

            val response = apiService.getWhatsAppBusinessNumber()

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    val number = body.whatsappNumber ?: body.displayNumber ?: ""
                    Log.d("ContactRepository", "WhatsApp number loaded: $number")
                    emit(Resource.Success(number))
                } else {
                    emit(Resource.Error("Failed to load WhatsApp number"))
                }
            } else {
                emit(Resource.Error("Network error: ${response.code()}"))
            }
        } catch (e: Exception) {
            Log.e("ContactRepository", "Exception in getWhatsAppNumber: ${e.message}", e)
            emit(Resource.Error(e.message ?: "Unknown error occurred"))
        }
    }
}
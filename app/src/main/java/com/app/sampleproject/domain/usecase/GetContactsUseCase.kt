package com.app.sampleproject.domain.usecase

import android.util.Log
import com.app.sampleproject.core.utils.Resource
import com.app.sampleproject.data.mapper.toDomain
import com.app.sampleproject.domain.model.ContactsDomain
import com.app.sampleproject.domain.repository.AuthRepository
import com.app.sampleproject.domain.repository.ContactRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetContactsUseCase @Inject constructor(
    private val contactRepository: ContactRepository,
    private val authRepository: AuthRepository
) {
    operator fun invoke(): Flow<Resource<ContactsDomain>> = flow {
        try {
            emit(Resource.Loading)

            val userData = authRepository.getUserData()
            if (userData == null) {
                Log.e("GetContactsUseCase", "User not logged in")
                emit(Resource.Error(message = "User not logged in. Please login again."))
                return@flow
            }

            Log.d("GetContactsUseCase", "Fetching contacts for userId: ${userData.userId}")

            val response = contactRepository.getContacts(userData.userId)

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    val contactsDomain = body.toDomain()
                    Log.d("GetContactsUseCase", "Contacts loaded: ${contactsDomain.reps.size} reps")
                    emit(Resource.Success(data = contactsDomain))
                } else {
                    Log.e("GetContactsUseCase", "Response body is null")
                    emit(Resource.Error(message = "Failed to load contacts: Empty response"))
                }
            } else {
                val errorMsg = "Network error: ${response.code()} - ${response.message()}"
                Log.e("GetContactsUseCase", errorMsg)
                emit(Resource.Error(message = errorMsg))
            }
        } catch (e: Exception) {
            Log.e("GetContactsUseCase", "Exception in getContacts: ${e.message}", e)
            emit(Resource.Error(message = e.localizedMessage ?: "Unknown error occurred"))
        }
    }
}
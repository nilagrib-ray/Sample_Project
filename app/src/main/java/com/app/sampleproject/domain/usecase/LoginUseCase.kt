package com.app.sampleproject.domain.usecase

import android.util.Log
import com.app.sampleproject.core.utils.Resource
import com.app.sampleproject.data.mapper.toDomain
import com.app.sampleproject.domain.model.UserDomain
import com.app.sampleproject.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    operator fun invoke(mPin: String, deviceId: String): Flow<Resource<Boolean>> = flow {
        try {
            if (mPin.length < 6) {
                emit(Resource.Error(message = "PIN must be at least 6 characters long."))
                return@flow
            }

            emit(Resource.Loading)

            val response = repository.login(mPin, deviceId)

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    val userDomain = body.toDomain()

                    repository.saveUserData(userDomain)

                    Log.d("LoginUseCase", "Login successful for user: ${userDomain.userEmail}")
                    emit(Resource.Success(data = true))
                } else {
                    Log.e("LoginUseCase", "Response body is null")
                    emit(Resource.Error(message = "Failed to retrieve data."))
                }
            } else {
                val errorMessage = when (response.code()) {
                    401 -> "Incorrect email or password. Check your details & try again."
                    404 -> "Service not found"
                    500 -> "Server error. Please try again later"
                    else -> "Network error: ${response.code()}"
                }
                Log.e("LoginUseCase", "Login failed: ${response.code()} - ${response.message()}")
                emit(Resource.Error(message = errorMessage))
            }
        } catch (e: Exception) {
            Log.e("LoginUseCase", "Exception during login: ${e.message}", e)
            emit(Resource.Error(message = e.localizedMessage ?: "Failed to retrieve data."))
        }
    }
}
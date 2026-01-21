package com.app.sampleproject.data.repository

import com.app.sampleproject.core.utils.Resource
import com.app.sampleproject.data.local.PreferencesManager
import com.app.sampleproject.data.mapper.toDomain
import com.app.sampleproject.data.mapper.toDto
import com.app.sampleproject.data.remote.ApiService
import com.app.sampleproject.data.remote.dto.LoginRequest
import com.app.sampleproject.domain.model.UserDomain
import com.app.sampleproject.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val preferencesManager: PreferencesManager
) : AuthRepository {

    override suspend fun login(email: String, password: String): Flow<Resource<UserDomain>> = flow {
        try {
            emit(Resource.Loading)
            val response = apiService.login(LoginRequest(email, password))

            if (response.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    val userDomain = body.toDomain()

                    saveUserData(userDomain)

                    emit(Resource.Success(userDomain))
                } else {
                    emit(Resource.Error("Login failed: Empty response"))
                }
            } else {
                val errorMessage = when (response.code()) {
                    401 -> "Incorrect email or password. Check your details & try again."
                    404 -> "Service not found"
                    500 -> "Server error. Please try again later"
                    else -> "Network error: ${response.code()}"
                }
                emit(Resource.Error(errorMessage))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Unknown error occurred"))
        }
    }

    override suspend fun saveUserData(user: UserDomain) {
        preferencesManager.saveUserData(user.toDto())
    }

    override suspend fun getUserData(): UserDomain? {
        return preferencesManager.getUserData()?.toDomain()
    }

    override suspend fun isLoggedIn(): Boolean {
        return preferencesManager.isLoggedIn()
    }

    override suspend fun clearUserData() {
        preferencesManager.clearUserData()
    }
}
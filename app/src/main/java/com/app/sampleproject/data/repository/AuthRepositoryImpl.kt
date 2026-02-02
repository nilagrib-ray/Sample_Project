package com.app.sampleproject.data.repository

import com.app.sampleproject.data.local.PreferencesManager
import com.app.sampleproject.data.mapper.toDomain
import com.app.sampleproject.data.mapper.toDto
import com.app.sampleproject.data.remote.ApiService
import com.app.sampleproject.data.remote.dto.LoginRequest
import com.app.sampleproject.data.remote.dto.LoginResponse
import com.app.sampleproject.domain.model.UserDomain
import com.app.sampleproject.domain.repository.AuthRepository
import retrofit2.Response
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val preferencesManager: PreferencesManager
) : AuthRepository {

    override suspend fun login(email: String, password: String): Response<LoginResponse> {
        return apiService.login(LoginRequest(email, password))
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
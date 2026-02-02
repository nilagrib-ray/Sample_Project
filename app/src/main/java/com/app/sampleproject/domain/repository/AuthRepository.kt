package com.app.sampleproject.domain.repository

import com.app.sampleproject.data.remote.dto.LoginResponse
import com.app.sampleproject.domain.model.UserDomain
import retrofit2.Response

interface AuthRepository {
    suspend fun login(email: String, password: String): Response<LoginResponse>
    suspend fun saveUserData(user: UserDomain)
    suspend fun getUserData(): UserDomain?
    suspend fun isLoggedIn(): Boolean
    suspend fun clearUserData()
}
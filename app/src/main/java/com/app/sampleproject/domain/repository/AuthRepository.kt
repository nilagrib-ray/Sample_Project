package com.app.sampleproject.domain.repository

import com.app.sampleproject.domain.model.UserDomain
import com.app.sampleproject.core.utils.Resource
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun login(email: String, password: String): Flow<Resource<UserDomain>>
    suspend fun saveUserData(user: UserDomain)
    suspend fun getUserData(): UserDomain?
    suspend fun isLoggedIn(): Boolean
    suspend fun clearUserData()
}
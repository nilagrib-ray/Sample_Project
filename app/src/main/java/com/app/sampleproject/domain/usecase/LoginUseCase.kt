package com.app.sampleproject.domain.usecase

import com.app.sampleproject.core.utils.Resource
import com.app.sampleproject.domain.model.UserDomain
import com.app.sampleproject.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): Flow<Resource<UserDomain>> {
        return authRepository.login(email, password)
    }
}
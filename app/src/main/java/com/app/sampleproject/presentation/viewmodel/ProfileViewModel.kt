package com.app.sampleproject.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.sampleproject.data.remote.ApiService
import com.app.sampleproject.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileUiState(
    val isLoading: Boolean = false,
    val userId: String = "",
    val userName: String? = null,
    val userEmail: String = "",
    val firstName: String? = null,
    val lastName: String? = null,
    val profileImage: String? = null,
    val userType: String? = null,
    val errorMessage: String? = null
)

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val apiService: ApiService
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadUserData()
    }

    private fun loadUserData() {
        viewModelScope.launch {
            val localUserData = authRepository.getUserData()
            if (localUserData != null) {
                _uiState.value = ProfileUiState(
                    userId = localUserData.userId,
                    userName = localUserData.userName ?: localUserData.firstName ?: "User",
                    userEmail = localUserData.userEmail,
                    firstName = localUserData.firstName,
                    lastName = localUserData.lastName,
                    profileImage = localUserData.profileImage,
                    userType = localUserData.userType
                )

                fetchUserInfoFromApi(localUserData.userId)
            } else {
                _uiState.value = _uiState.value.copy(
                    errorMessage = "User data not found. Please login again."
                )
            }
        }
    }

    private fun fetchUserInfoFromApi(userId: String) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(isLoading = true)
                Log.d("ProfileViewModel", "Fetching user info from API for userId: $userId")

                val response = apiService.getUserInfo(userId)

                if (response.isSuccessful) {
                    val userInfo = response.body()
                    if (userInfo != null) {
                        Log.d("ProfileViewModel", "User info loaded from API: ${userInfo.email}")
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            userId = userInfo.userId ?: userId,
                            userName = userInfo.username ?: userInfo.firstName ?: "User",
                            userEmail = userInfo.email ?: _uiState.value.userEmail,
                            firstName = userInfo.firstName,
                            lastName = userInfo.lastName,
                            profileImage = userInfo.profileImage,
                            userType = userInfo.userType,
                            errorMessage = null
                        )
                    } else {
                        _uiState.value = _uiState.value.copy(isLoading = false)
                    }
                } else {
                    Log.e("ProfileViewModel", "Failed to load user info: ${response.code()}")
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = "Failed to refresh profile data"
                    )
                }
            } catch (e: Exception) {
                Log.e("ProfileViewModel", "Exception loading user info: ${e.message}", e)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Error loading profile data"
                )
            }
        }
    }

    fun refreshProfile() {
        val userId = _uiState.value.userId
        if (userId.isNotEmpty()) {
            fetchUserInfoFromApi(userId)
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.clearUserData()
        }
    }
}
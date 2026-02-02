package com.app.sampleproject.presentation.profile

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
package com.app.sampleproject.domain.model

data class UserDomain(
    val userId: String,
    val userType: String,
    val userEmail: String,
    val userName: String?,
    val token: String? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val profileImage: String? = null
)
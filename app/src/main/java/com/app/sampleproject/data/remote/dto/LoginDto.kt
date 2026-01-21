package com.app.sampleproject.data.remote.dto

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("user_email") val userEmail: String,
    @SerializedName("password") val password: String
)

data class LoginResponse(
    @SerializedName("user_id") val userId: Int,
    @SerializedName("username") val username: String,
    @SerializedName("first_name") val firstName: String?,
    @SerializedName("last_name") val lastName: String?,
    @SerializedName("email") val email: String,
    @SerializedName("token") val token: String,
    @SerializedName("profile_image") val profileImage: String?,
    @SerializedName("guest_bookings") val guestBookings: String?,
    @SerializedName("user_type") val userType: String
)

data class UserDataDto(
    @SerializedName("user_id") val userId: String,
    @SerializedName("user_type") val userType: String,
    @SerializedName("user_email") val userEmail: String,
    @SerializedName("user_name") val userName: String?,
    @SerializedName("token") val token: String? = null,
    @SerializedName("first_name") val firstName: String? = null,
    @SerializedName("last_name") val lastName: String? = null,
    @SerializedName("profile_image") val profileImage: String? = null
)
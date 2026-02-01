package com.app.sampleproject.data.remote.dto

import com.google.gson.annotations.SerializedName

data class UserInfoResponse(
    @SerializedName("user_id") val userId: String?,
    @SerializedName("username") val username: String?,
    @SerializedName("first_name") val firstName: String?,
    @SerializedName("last_name") val lastName: String?,
    @SerializedName("email") val email: String?,
    @SerializedName("profile_image") val profileImage: String?,
    @SerializedName("user_type") val userType: String?
)
package com.app.sampleproject.data.mapper

import com.app.sampleproject.data.remote.dto.LoginResponse
import com.app.sampleproject.data.remote.dto.UserDataDto
import com.app.sampleproject.domain.model.UserDomain

fun LoginResponse.toDomain(): UserDomain {
    return UserDomain(
        userId = userId.toString(),
        userType = userType,
        userEmail = email,
        userName = username,
        token = token,
        firstName = firstName,
        lastName = lastName,
        profileImage = profileImage
    )
}

fun UserDataDto.toDomain(): UserDomain {
    return UserDomain(
        userId = userId,
        userType = userType,
        userEmail = userEmail,
        userName = userName,
        token = token,
        firstName = firstName,
        lastName = lastName,
        profileImage = profileImage
    )
}

fun UserDomain.toDto(): UserDataDto {
    return UserDataDto(
        userId = userId,
        userType = userType,
        userEmail = userEmail,
        userName = userName,
        token = token,
        firstName = firstName,
        lastName = lastName,
        profileImage = profileImage
    )
}
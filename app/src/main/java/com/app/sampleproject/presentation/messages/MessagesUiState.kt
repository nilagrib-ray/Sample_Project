package com.app.sampleproject.presentation.messages

import com.app.sampleproject.domain.model.ContactDomain

data class MessagesUiState(
    val isLoading: Boolean = false,
    val destinationName: String = "",
    val reps: List<ContactDomain> = emptyList(),
    val emergencyNumber: String = "",
    val globalEmergencyNumber: String = "447984290932",
    val phtContactPhone: String = "0203 627 4443",
    val errorMessage: String? = null
)

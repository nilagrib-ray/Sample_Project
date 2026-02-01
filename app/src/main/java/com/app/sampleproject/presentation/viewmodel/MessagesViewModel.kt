package com.app.sampleproject.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.sampleproject.core.utils.Resource
import com.app.sampleproject.domain.model.ContactDomain
import com.app.sampleproject.domain.repository.AuthRepository
import com.app.sampleproject.domain.repository.ContactRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MessagesUiState(
    val isLoading: Boolean = false,
    val destinationName: String = "",
    val reps: List<ContactDomain> = emptyList(),
    val emergencyNumber: String = "",
    val globalEmergencyNumber: String = "447984290932",
    val phtContactPhone: String = "0203 627 4443",
    val errorMessage: String? = null
)

@HiltViewModel
class MessagesViewModel @Inject constructor(
    private val contactRepository: ContactRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MessagesUiState())
    val uiState: StateFlow<MessagesUiState> = _uiState.asStateFlow()

    init {
        loadContacts()
    }

    fun loadContacts() {
        viewModelScope.launch {
            val userData = authRepository.getUserData()

            Log.d("MessagesViewModel", "User data: $userData")

            if (userData == null) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "User data not found. Please login again."
                )
                return@launch
            }

            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            contactRepository.getContacts(userData.userId).collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        Log.d("MessagesViewModel", "Loading contacts...")
                    }
                    is Resource.Success -> {
                        Log.d("MessagesViewModel", "Contacts loaded successfully: ${resource.data.reps.size} reps")

                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            destinationName = resource.data.destinationName,
                            reps = resource.data.reps,
                            emergencyNumber = resource.data.emergencyNumber ?: "0203 627 4443",
                            globalEmergencyNumber = resource.data.globalEmergencyNumber ?: "447984290932",
                            phtContactPhone = resource.data.phtContactPhone ?: "0203 627 4443"
                        )
                    }
                    is Resource.Error -> {
                        Log.e("MessagesViewModel", "Error loading contacts: ${resource.message}")
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            errorMessage = resource.message
                        )
                    }
                }
            }
        }
    }
}
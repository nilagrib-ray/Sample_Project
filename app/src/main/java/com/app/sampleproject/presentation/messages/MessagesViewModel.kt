package com.app.sampleproject.presentation.messages

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.sampleproject.core.utils.Resource
import com.app.sampleproject.domain.model.ContactDomain
import com.app.sampleproject.domain.usecase.GetContactsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MessagesViewModel @Inject constructor(
    private val getContactsUseCase: GetContactsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(MessagesUiState())
    val uiState: StateFlow<MessagesUiState> = _uiState.asStateFlow()

    init {
        loadContacts()
    }

    fun loadContacts() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            getContactsUseCase().collect { resource ->
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
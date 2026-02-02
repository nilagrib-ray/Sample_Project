package com.app.sampleproject.presentation.trips

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.sampleproject.core.utils.Resource
import com.app.sampleproject.domain.repository.AuthRepository
import com.app.sampleproject.domain.usecase.GetDestinationsUseCase
import com.app.sampleproject.domain.usecase.GetTripsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TripsViewModel @Inject constructor(
    private val getTripsUseCase: GetTripsUseCase,
    private val getDestinationsUseCase: GetDestinationsUseCase,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TripsUiState())
    val uiState: StateFlow<TripsUiState> = _uiState.asStateFlow()

    init {
        loadAllTrips()
        loadDestinations()
    }

    fun loadAllTrips() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            getTripsUseCase().collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        Log.d("TripsViewModel", "Loading trips...")
                    }
                    is Resource.Success -> {
                        Log.d("TripsViewModel", "Trips loaded successfully: ${resource.data.size}")
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            upcomingTrips = resource.data.filter { isUpcoming(it.startDate) },
                            previousTrips = resource.data.filter { !isUpcoming(it.startDate) }
                        )
                    }
                    is Resource.Error -> {
                        Log.e("TripsViewModel", "Error loading trips: ${resource.message}")
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            errorMessage = resource.message
                        )
                    }
                }
            }
        }
    }

    private fun loadDestinations() {
        viewModelScope.launch {
            getDestinationsUseCase().collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        Log.d("TripsViewModel", "Loading destinations...")
                    }
                    is Resource.Success -> {
                        Log.d("TripsViewModel", "Destinations loaded: ${resource.data.size}")
                        _uiState.value = _uiState.value.copy(
                            destinations = resource.data
                        )
                    }
                    is Resource.Error -> {
                        Log.e("TripsViewModel", "Error loading destinations: ${resource.message}")
                    }
                }
            }
        }
    }

    fun logout() {
        viewModelScope.launch {
            authRepository.clearUserData()
            Log.d("TripsViewModel", "User logged out successfully")
        }
    }

    private fun isUpcoming(dateString: String): Boolean {
        return try {
            val tripDate = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
                .parse(dateString)
            val currentDate = java.util.Date()
            tripDate?.after(currentDate) ?: false
        } catch (e: Exception) {
            Log.e("TripsViewModel", "Error parsing date: $dateString", e)
            false
        }
    }
}
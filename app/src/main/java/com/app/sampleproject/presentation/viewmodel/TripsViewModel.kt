package com.app.sampleproject.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.sampleproject.core.utils.Resource
import com.app.sampleproject.domain.model.TripDomain
import com.app.sampleproject.domain.repository.AuthRepository
import com.app.sampleproject.domain.repository.TripRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class TripsUiState(
    val isLoading: Boolean = false,
    val upcomingTrips: List<TripDomain> = emptyList(),
    val nextTrips: List<TripDomain> = emptyList(),
    val previousTrips: List<TripDomain> = emptyList(),
    val errorMessage: String? = null
)

@HiltViewModel
class TripsViewModel @Inject constructor(
    private val tripRepository: TripRepository,
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TripsUiState())
    val uiState: StateFlow<TripsUiState> = _uiState.asStateFlow()

    init {
        loadAllTrips()
    }

    fun loadAllTrips() {
        viewModelScope.launch {
            val userData = authRepository.getUserData()

            Log.d("TripsViewModel", "User data: $userData")

            if (userData == null) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "User data not found. Please login again."
                )
                return@launch
            }

            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)

            tripRepository.getTrips(userData.userId).collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        Log.d("TripsViewModel", "Loading trips...")
                    }
                    is Resource.Success -> {
                        Log.d("TripsViewModel", "Trips loaded successfully: ${resource.data.size}")

                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            upcomingTrips = resource.data.filter { isUpcoming(it.startDate) },
                            previousTrips = resource.data.filter { !isUpcoming(it.startDate) },
                            nextTrips = resource.data.filter { isUpcoming(it.startDate) }.take(3)
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
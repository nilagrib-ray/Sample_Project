package com.app.sampleproject.presentation.tripdetails

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.sampleproject.core.utils.Resource
import com.app.sampleproject.domain.usecase.GetItineraryUseCase
import com.app.sampleproject.domain.usecase.GetTripDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class TripDetailsViewModel @Inject constructor(
    private val getTripDetailsUseCase: GetTripDetailsUseCase,
    private val getItineraryUseCase: GetItineraryUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(TripDetailsUiState())
    val uiState: StateFlow<TripDetailsUiState> = _uiState.asStateFlow()

    fun loadTripDetails(packageId: Int?, bookingId: Int?, orderId: String?) {
        viewModelScope.launch {
            getTripDetailsUseCase(packageId, bookingId, orderId).collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
                        Log.d("TripDetailsViewModel", "Loading trip details...")
                    }
                    is Resource.Success -> {
                        val tripDetails = resource.data
                        Log.d("TripDetailsViewModel", "Trip details loaded: ${tripDetails.tripName}")
                        Log.d("TripDetailsViewModel", "Arrival date: ${tripDetails.arrivalDate}")
                        Log.d("TripDetailsViewModel", "Travellers count: ${tripDetails.travellers.size}")

                        val (days, hours, minutes) = calculateTimeToGo(tripDetails.arrivalDate)
                        Log.d("TripDetailsViewModel", "Calculated time to go: $days days, $hours hours, $minutes minutes")

                        val bookingNumDisplay = when {
                            tripDetails.bookingId != null && tripDetails.bookingId != 0 ->
                                tripDetails.bookingId.toString()
                            !tripDetails.orderId.isNullOrEmpty() && tripDetails.orderId != "null" ->
                                tripDetails.orderId
                            else -> "N/A"
                        }
                        Log.d("TripDetailsViewModel", "Booking number: $bookingNumDisplay")

                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            tripName = tripDetails.tripName,
                            description = tripDetails.description,
                            featuredImage = tripDetails.featuredImage ?: tripDetails.image,
                            arrivalDate = tripDetails.arrivalDate,
                            arrivalTime = tripDetails.arrivalTime,
                            departureDate = tripDetails.departureDate,
                            departureTime = tripDetails.departureTime,
                            hotel = tripDetails.hotel,
                            bookingNumber = bookingNumDisplay,
                            daysToGo = days,
                            hoursToGo = hours,
                            minutesToGo = minutes,
                            destinationName = tripDetails.destinationName,
                            travellers = tripDetails.travellers,
                            actionsRequired = tripDetails.actionsRequired,
                            bookingTotal = tripDetails.bookingTotal,
                            bookingBalance = tripDetails.bookingBalance,
                            currencySymbol = "£",
                            destinationLatitude = tripDetails.destinationLatitude,
                            destinationLongitude = tripDetails.destinationLongitude,
                            meetingPointDetails = tripDetails.meetingPointDetails
                        )

                        tripDetails.bookingId?.let { bId ->
                            if (bId > 0) {
                                loadItinerary(bId)
                            }
                        }
                    }
                    is Resource.Error -> {
                        Log.e("TripDetailsViewModel", "Error loading trip details: ${resource.message}")
                        _uiState.value = _uiState.value.copy(
                            isLoading = false,
                            errorMessage = resource.message
                        )
                    }
                }
            }
        }
    }

    private fun loadItinerary(bookingId: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoadingItinerary = true)

            val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
            Log.d("TripDetailsViewModel", "Loading itinerary for booking $bookingId on $today")

            getItineraryUseCase(bookingId, today).collect { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        Log.d("TripDetailsViewModel", "Loading itinerary...")
                    }
                    is Resource.Success -> {
                        Log.d("TripDetailsViewModel", "Itinerary loaded: ${resource.data.events.size} events")
                        _uiState.value = _uiState.value.copy(
                            isLoadingItinerary = false,
                            todayEvents = resource.data.events
                        )
                    }
                    is Resource.Error -> {
                        Log.e("TripDetailsViewModel", "Error loading itinerary: ${resource.message}")
                        _uiState.value = _uiState.value.copy(
                            isLoadingItinerary = false
                        )
                    }
                }
            }
        }
    }

    private fun calculateTimeToGo(dateString: String): Triple<Int, Int, Int> {
        return try {
            if (dateString.isEmpty()) {
                Log.w("TripDetailsViewModel", "Empty date string for countdown")
                return Triple(0, 0, 0)
            }

            Log.d("TripDetailsViewModel", "Calculating time to go for: $dateString")

            val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val tripDate = format.parse(dateString)
            val currentDate = Date()

            Log.d("TripDetailsViewModel", "Trip date: $tripDate")
            Log.d("TripDetailsViewModel", "Current date: $currentDate")

            if (tripDate != null && tripDate.after(currentDate)) {
                val diff = tripDate.time - currentDate.time
                Log.d("TripDetailsViewModel", "Time difference in milliseconds: $diff")

                val days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS).toInt()

                val remainingAfterDays = diff - TimeUnit.DAYS.toMillis(days.toLong())

                val hours = TimeUnit.HOURS.convert(remainingAfterDays, TimeUnit.MILLISECONDS).toInt()

                val remainingAfterHours = remainingAfterDays - TimeUnit.HOURS.toMillis(hours.toLong())

                val minutes = TimeUnit.MINUTES.convert(remainingAfterHours, TimeUnit.MILLISECONDS).toInt()

                Log.d("TripDetailsViewModel", "Countdown calculated: $days days, $hours hours, $minutes minutes")
                Triple(days, hours, minutes)
            } else {
                Log.w("TripDetailsViewModel", "Trip date is in the past or null")
                Triple(0, 0, 0)
            }
        } catch (e: Exception) {
            Log.e("TripDetailsViewModel", "Error calculating time to go: ${e.message}", e)
            Triple(0, 0, 0)
        }
    }
}
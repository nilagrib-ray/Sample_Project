package com.app.sampleproject.data.mapper

import android.util.Log
import com.app.sampleproject.data.remote.dto.ActionRequiredDto
import com.app.sampleproject.data.remote.dto.EventDto
import com.app.sampleproject.data.remote.dto.ItineraryResponse
import com.app.sampleproject.data.remote.dto.TravellerDto
import com.app.sampleproject.data.remote.dto.TripDetailsResponse
import com.app.sampleproject.domain.model.ActionRequired
import com.app.sampleproject.domain.model.Event
import com.app.sampleproject.domain.model.ItineraryDomain
import com.app.sampleproject.domain.model.Traveller
import com.app.sampleproject.domain.model.TripDetailsDomain
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

fun TripDetailsResponse.toDomain(): TripDetailsDomain {
    val destinationName = destination?.firstOrNull()?.name ?: ""
    val destinationImageUrl = destination?.firstOrNull()?.descriptionFeaturedImageUrl

    val daysToGo = calculateDaysToGo(arrivalDate ?: "")

    val latitude = meetingPoint?.lat ?: getDefaultLatitudeForDestination(destinationName)
    val longitude = meetingPoint?.lng ?: getDefaultLongitudeForDestination(destinationName)

    Log.d("TripDetailsMapper", "Coordinates: lat=$latitude, lng=$longitude for $destinationName")

    val travellersList = try {
        when {
            travellers == null -> {
                Log.d("TripDetailsMapper", "Travellers is null")
                emptyList()
            }
            travellers.isJsonArray -> {
                Log.d("TripDetailsMapper", "Travellers is JSON Array")
                val type = object : TypeToken<List<TravellerDto>>() {}.type
                val parsed = Gson().fromJson<List<TravellerDto>>(travellers, type)
                Log.d("TripDetailsMapper", "Parsed ${parsed?.size ?: 0} travellers from array")
                parsed ?: emptyList()
            }
            travellers.isJsonPrimitive && travellers.asJsonPrimitive.isString -> {
                val countStr = travellers.asString
                Log.d("TripDetailsMapper", "Travellers is String: $countStr")
                val count = countStr.toIntOrNull() ?: 0
                Log.d("TripDetailsMapper", "Creating $count placeholder travellers")
                List(count) { index ->
                    TravellerDto(
                        id = "$index",
                        firstName = "Traveller",
                        lastName = "${index + 1}",
                        email = null,
                        isLeadBooker = index == 0
                    )
                }
            }
            travellers.isJsonPrimitive && travellers.asJsonPrimitive.isNumber -> {
                val count = travellers.asInt
                Log.d("TripDetailsMapper", "Travellers is Number: $count")
                List(count) { index ->
                    TravellerDto(
                        id = "$index",
                        firstName = "Traveller",
                        lastName = "${index + 1}",
                        email = null,
                        isLeadBooker = index == 0
                    )
                }
            }
            else -> {
                Log.w("TripDetailsMapper", "Unknown travellers format: ${travellers.javaClass.simpleName}")
                emptyList()
            }
        }
    } catch (e: Exception) {
        Log.e("TripDetailsMapper", "Error parsing travellers: ${e.message}", e)
        Log.e("TripDetailsMapper", "Travellers JSON: $travellers")
        emptyList()
    }

    Log.d("TripDetailsMapper", "Final travellers list size: ${travellersList.size}")

    return TripDetailsDomain(
        id = id ?: 0,
        packageId = packageId,
        bookingId = bookingId,
        orderId = orderId,
        tripName = "Ayia Napa", // packageTitle ?: bookingTitle ?:
        description = description,
        arrivalDate = arrivalDate ?: "",
        arrivalTime = arrivalTime,
        departureDate = departureDate ?: "",
        departureTime = departureTime,
        hotel = hotel,
        featuredImage = featuredImage,
        image = image,
        squareImage = squareImage,
        travellers = travellersList.map { it.toDomain() },
        bookingTotal = bookingTotal,
        bookingBalance = bookingBalance,
        currencySymbol = currencySymbol,
        destinationName = destinationName,
        destinationImage = destinationImageUrl,
        actionsRequired = actionsRequired?.map { it.toDomain() } ?: emptyList(),
        daysToGo = daysToGo,
        destinationLatitude = latitude,
        destinationLongitude = longitude,
        meetingPointDetails = meetingPointDetails
    )
}

fun TravellerDto.toDomain(): Traveller {
    val first = firstName ?: ""
    val last = lastName ?: ""
    return Traveller(
        id = id ?: "",
        firstName = first,
        lastName = last,
        fullName = if (first.isNotEmpty() || last.isNotEmpty()) {
            "$first $last".trim()
        } else {
            "Unnamed Traveller"
        },
        email = email,
        isLeadBooker = isLeadBooker ?: false
    )
}

fun ActionRequiredDto.toDomain(): ActionRequired {
    return ActionRequired(
        id = id ?: "",
        title = title ?: "",
        description = description ?: "",
        actionType = actionType ?: ""
    )
}

fun ItineraryResponse.toDomain(): ItineraryDomain {
    return ItineraryDomain(
        events = events?.map { it.toDomain() } ?: emptyList(),
        eventDate = eventDate ?: ""
    )
}

fun EventDto.toDomain(): Event {
    return Event(
        id = id ?: 0,
        title = title ?: "",
        description = description,
        startTime = startTime,
        endTime = endTime,
        location = location,
        eventType = eventType ?: "",
        image = image
    )
}

private fun getDefaultLatitudeForDestination(destinationName: String): Double {
    return when {
        destinationName.contains("Ayia Napa", ignoreCase = true) -> 34.9823
        destinationName.contains("Ibiza", ignoreCase = true) -> 38.9067
        destinationName.contains("Magaluf", ignoreCase = true) -> 39.5107
        destinationName.contains("Zante", ignoreCase = true) ||
                destinationName.contains("Zakynthos", ignoreCase = true) -> 37.7870
        destinationName.contains("Malia", ignoreCase = true) -> 35.2886
        destinationName.contains("Kavos", ignoreCase = true) -> 39.4147
        destinationName.contains("Albufeira", ignoreCase = true) -> 37.0893
        destinationName.contains("Marbella", ignoreCase = true) -> 36.5100
        destinationName.contains("Benidorm", ignoreCase = true) -> 38.5382
        else -> {
            Log.w("TripDetailsMapper", "Unknown destination: $destinationName, using default coordinates")
            35.0 // Default Mediterranean location
        }
    }
}

private fun getDefaultLongitudeForDestination(destinationName: String): Double {
    return when {
        destinationName.contains("Ayia Napa", ignoreCase = true) -> 34.0053
        destinationName.contains("Ibiza", ignoreCase = true) -> 1.4206
        destinationName.contains("Magaluf", ignoreCase = true) -> 2.5347
        destinationName.contains("Zante", ignoreCase = true) ||
                destinationName.contains("Zakynthos", ignoreCase = true) -> 20.8984
        destinationName.contains("Malia", ignoreCase = true) -> 25.4748
        destinationName.contains("Kavos", ignoreCase = true) -> 20.0761
        destinationName.contains("Albufeira", ignoreCase = true) -> -8.2458
        destinationName.contains("Marbella", ignoreCase = true) -> -4.8824
        destinationName.contains("Benidorm", ignoreCase = true) -> -0.1312
        else -> {
            25.0 // Default Mediterranean location
        }
    }
}

private fun calculateDaysToGo(dateString: String): Int {
    return try {
        if (dateString.isEmpty()) {
            Log.w("TripDetailsMapper", "Empty date string for days calculation")
            return 0
        }

        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val tripDate = format.parse(dateString)
        val currentDate = Date()

        if (tripDate != null && tripDate.after(currentDate)) {
            val diff = tripDate.time - currentDate.time
            val days = TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS).toInt()
            Log.d("TripDetailsMapper", "Days to go: $days")
            days
        } else {
            Log.w("TripDetailsMapper", "Trip date is in the past or null")
            0
        }
    } catch (e: Exception) {
        Log.e("TripDetailsMapper", "Error calculating days: ${e.message}", e)
        0
    }
}
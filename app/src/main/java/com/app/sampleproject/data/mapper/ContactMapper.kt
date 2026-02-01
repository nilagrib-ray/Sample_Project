package com.app.sampleproject.data.mapper

import com.app.sampleproject.data.remote.dto.ContactsResponse
import com.app.sampleproject.domain.model.ContactDomain
import com.app.sampleproject.domain.model.ContactsDomain

fun ContactsResponse.toDomain(): ContactsDomain {
    val repsList = mutableListOf<ContactDomain>()

    reps?.let { repsData ->
        val firstNames = repsData.repsFirstName ?: emptyMap()
        val lastNames = repsData.repsLastName ?: emptyMap()
        val phones = repsData.repsPhone ?: emptyMap()
        val pronouns = repsData.repsPronouns ?: emptyMap()
        val images = repsData.repsImage ?: emptyMap()

        val repKeys = firstNames.keys.map { key ->
            key.substringAfter("contact_details_reps_")
                .substringBefore("_")
                .toIntOrNull() ?: -1
        }.filter { it >= 0 }.distinct().sorted()

        repKeys.forEach { index ->
            val firstNameKey = "contact_details_reps_${index}_first_name"
            val lastNameKey = "contact_details_reps_${index}_last_name"
            val phoneKey = "contact_details_reps_${index}_phone"
            val pronounKey = "contact_details_reps_${index}_pronouns"
            val imageKey = "contact_details_reps_${index}_image"

            val firstName = firstNames[firstNameKey] ?: ""
            val lastName = lastNames[lastNameKey] ?: ""
            val phone = phones[phoneKey] ?: ""
            val pronoun = pronouns[pronounKey] ?: ""
            val image = images[imageKey]

            if (firstName.isNotBlank() && lastName.isNotBlank()) {
                repsList.add(
                    ContactDomain(
                        id = index.toString(),
                        firstName = firstName.trim(),
                        lastName = lastName.trim(),
                        fullName = "${firstName.trim()} ${lastName.trim()}",
                        phone = phone.trim(),
                        pronouns = pronoun.trim(),
                        imageUrl = image
                    )
                )
            }
        }
    }

    return ContactsDomain(
        destinationName = name ?: "Destination",
        destinationImage = featuredImage,
        reps = repsList,
        emergencyNumber = emergencyNumber,
        globalEmergencyNumber = globalEmergencyContact,
        phtContactPhone = phtContactPhone,
        phtContactEmail = phtContactEmail,
        meetingPointDetails = meetingPointDetails
    )
}
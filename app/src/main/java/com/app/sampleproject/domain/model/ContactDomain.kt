package com.app.sampleproject.domain.model

data class ContactDomain(
    val id: String,
    val firstName: String,
    val lastName: String,
    val fullName: String,
    val phone: String,
    val pronouns: String,
    val imageUrl: String?
)

data class ContactsDomain(
    val destinationName: String,
    val destinationImage: String?,
    val reps: List<ContactDomain>,
    val emergencyNumber: String?,
    val globalEmergencyNumber: String?,
    val phtContactPhone: String?,
    val phtContactEmail: String?,
    val meetingPointDetails: String?
)

data class KeyContact(
    val name: String,
    val subtitle: String,
    val contactInfo: String? = null,
    val isWhatsApp: Boolean = false
)
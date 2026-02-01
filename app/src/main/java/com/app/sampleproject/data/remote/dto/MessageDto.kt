package com.app.sampleproject.data.remote.dto

import com.google.gson.annotations.SerializedName

data class ContactsResponse(
    @SerializedName("id") val id: Int?,
    @SerializedName("booking_id") val bookingId: Int?,
    @SerializedName("name") val name: String?,
    @SerializedName("slug") val slug: String?,
    @SerializedName("featured_image") val featuredImage: String?,
    @SerializedName("meeting_point") val meetingPoint: MeetingPoint?,
    @SerializedName("contact_details_emergency_number") val emergencyNumber: String?,
    @SerializedName("contact_details_pht_contact_phone") val phtContactPhone: String?,
    @SerializedName("global_emergency_contact") val globalEmergencyContact: String?,
    @SerializedName("contact_details_pht_contact_email") val phtContactEmail: String?,
    @SerializedName("meeting_point_details") val meetingPointDetails: String?,
    @SerializedName("faq") val faq: String?,
    @SerializedName("reps") val reps: RepsData?
)

data class MeetingPoint(
    @SerializedName("address") val address: String?,
    @SerializedName("lat") val lat: Double?,
    @SerializedName("lng") val lng: Double?,
    @SerializedName("zoom") val zoom: Int?,
    @SerializedName("place_id") val placeId: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("street_number") val streetNumber: String?,
    @SerializedName("street_name") val streetName: String?,
    @SerializedName("city") val city: String?,
    @SerializedName("state") val state: String?,
    @SerializedName("post_code") val postCode: String?,
    @SerializedName("country") val country: String?,
    @SerializedName("country_short") val countryShort: String?
)

data class RepsData(
    @SerializedName("reps_first_name") val repsFirstName: Map<String, String>?,
    @SerializedName("reps_last_name") val repsLastName: Map<String, String>?,
    @SerializedName("reps_phone") val repsPhone: Map<String, String>?,
    @SerializedName("reps_pronouns") val repsPronouns: Map<String, String>?,
    @SerializedName("reps_image") val repsImage: Map<String, String>?
)

data class WhatsAppNumberResponse(
    @SerializedName("whatsapp_number") val whatsappNumber: String?,
    @SerializedName("country_code") val countryCode: String?,
    @SerializedName("display_number") val displayNumber: String?
)
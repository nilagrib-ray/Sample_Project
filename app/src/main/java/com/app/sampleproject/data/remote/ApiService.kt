package com.app.sampleproject.data.remote

import com.app.sampleproject.data.remote.dto.*
import retrofit2.Response
import retrofit2.http.*

interface ApiService {
    @POST("mobileapp/v1/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>

    @GET("mobileapp/v1/trips")
    suspend fun getTrips(
        @Query("user_id") userId: String
    ): Response<TripsResponse>

    @GET("mobileapp/v1/upcoming-trips")
    suspend fun getUpcomingTrips(): Response<List<CategoryDto>>

    @GET("mobileapp/v1/user-info/{id}")
    suspend fun getUserInfo(
        @Path("id") userId: String
    ): Response<UserInfoResponse>

    @GET("mobileapp/v1/get-contacts")
    suspend fun getContacts(
        @Query("user_id") userId: String
    ): Response<ContactsResponse>

    @GET("mobileapp/v1/whatsapp-business-number")
    suspend fun getWhatsAppBusinessNumber(): Response<WhatsAppNumberResponse>
}
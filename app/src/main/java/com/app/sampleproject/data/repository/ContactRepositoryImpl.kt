package com.app.sampleproject.data.repository

import com.app.sampleproject.data.remote.ApiService
import com.app.sampleproject.data.remote.dto.ContactsResponse
import com.app.sampleproject.data.remote.dto.WhatsAppNumberResponse
import com.app.sampleproject.domain.repository.ContactRepository
import retrofit2.Response
import javax.inject.Inject

class ContactRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : ContactRepository {

    override suspend fun getContacts(userId: String): Response<ContactsResponse> {
        return apiService.getContacts(userId)
    }

    override suspend fun getWhatsAppNumber(): Response<WhatsAppNumberResponse> {
        return apiService.getWhatsAppBusinessNumber()
    }
}
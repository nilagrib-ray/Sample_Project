package com.app.sampleproject.domain.repository

import com.app.sampleproject.data.remote.dto.ContactsResponse
import com.app.sampleproject.data.remote.dto.WhatsAppNumberResponse
import retrofit2.Response

interface ContactRepository {
    suspend fun getContacts(userId: String): Response<ContactsResponse>
    suspend fun getWhatsAppNumber(): Response<WhatsAppNumberResponse>
}
package com.app.sampleproject.domain.repository

import com.app.sampleproject.core.utils.Resource
import com.app.sampleproject.domain.model.ContactsDomain
import kotlinx.coroutines.flow.Flow

interface ContactRepository {
    suspend fun getContacts(userId: String): Flow<Resource<ContactsDomain>>
}
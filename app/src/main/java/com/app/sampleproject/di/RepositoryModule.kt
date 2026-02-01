package com.app.sampleproject.di

import com.app.sampleproject.data.repository.AuthRepositoryImpl
import com.app.sampleproject.data.repository.ContactRepositoryImpl
import com.app.sampleproject.data.repository.TripRepositoryImpl
import com.app.sampleproject.domain.repository.AuthRepository
import com.app.sampleproject.domain.repository.ContactRepository
import com.app.sampleproject.domain.repository.TripRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        authRepositoryImpl: AuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindTripRepository(
        tripRepositoryImpl: TripRepositoryImpl
    ): TripRepository

    @Binds
    @Singleton
    abstract fun bindContactRepository(
        contactRepositoryImpl: ContactRepositoryImpl
    ): ContactRepository
}
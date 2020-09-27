package com.gpillaca.counters.di

import com.gpillaca.counters.data.server.ApiClient
import com.gpillaca.counters.data.server.CounterDbService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
class AppModule {

    @Singleton
    @Provides
    fun counterDbServiceProvider(): CounterDbService = ApiClient.counterDbService
}
package com.gpillaca.counters.di

import android.content.Context
import com.gpillaca.counters.data.server.ApiClient
import com.gpillaca.counters.data.server.CounterDbService
import com.gpillaca.counters.util.AndroidHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
class AppModule {

    @Singleton
    @Provides
    fun counterDbServiceProvider(): CounterDbService = ApiClient.counterDbService

    @Singleton
    @Provides
    fun androidHelperProvider(@ApplicationContext context: Context): AndroidHelper = AndroidHelper(context)
}
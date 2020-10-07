package com.gpillaca.counters.di

import com.gpillaca.counters.usecases.AddCounter
import com.gpillaca.counters.usecases.AddCounterImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@InstallIn(ActivityComponent::class)
@Module
class AddCounterUseCasesModule {

    @Provides
    fun addCounterProvider(addCounterImpl: AddCounterImpl): AddCounter = addCounterImpl
}
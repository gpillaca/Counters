package com.gpillaca.counters.di

import com.gpillaca.counters.usecases.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@InstallIn(ActivityComponent::class)
@Module
class MainUseCasesModule {

    @Provides
    fun getCountersProvider(getCountersImpl: GetCountersImpl): GetCounters = getCountersImpl

    @Provides
    fun deleteCounterProvider(deleteCounterImpl: DeleteCounterImpl): DeleteCounter =
        deleteCounterImpl

    @Provides
    fun incrementCounterProvider(incrementCounterImpl: IncrementCounterImpl): IncrementCounter =
        incrementCounterImpl

    @Provides
    fun decrementCounterProvider(decrementCounterImpl: DecrementCounterImpl): DecrementCounter =
        decrementCounterImpl
}
package com.gpillaca.counters.di

import com.gpillaca.counters.usecases.DecrementCounter
import com.gpillaca.counters.usecases.DecrementCounterImpl
import com.gpillaca.counters.usecases.DeleteCounter
import com.gpillaca.counters.usecases.DeleteCounterImpl
import com.gpillaca.counters.usecases.GetCounters
import com.gpillaca.counters.usecases.GetCountersImpl
import com.gpillaca.counters.usecases.IncrementCounter
import com.gpillaca.counters.usecases.IncrementCounterImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
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
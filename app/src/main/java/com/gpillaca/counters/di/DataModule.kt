package com.gpillaca.counters.di

import com.gpillaca.counters.data.detasource.RemoteDataSource
import com.gpillaca.counters.data.detasource.RetrofitDataSource
import com.gpillaca.counters.data.repository.CounterRepository
import com.gpillaca.counters.data.repository.CounterRepositoryImpl
import com.gpillaca.counters.data.server.CounterDbService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent

@Module
@InstallIn(ApplicationComponent::class)
class DataModule {

    @Provides
    fun retrofitDataSourceProvider(
        counterDbService: CounterDbService
    ) : RemoteDataSource = RetrofitDataSource(counterDbService)

    @Provides
    fun counterRepositoryProvider(
        remoteDataSource: RemoteDataSource
    ) : CounterRepository = CounterRepositoryImpl(remoteDataSource)
}
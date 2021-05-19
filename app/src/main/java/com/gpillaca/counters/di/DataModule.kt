package com.gpillaca.counters.di

import com.gpillaca.counters.data.database.AppDatabase
import com.gpillaca.counters.data.datasource.LocalDataSource
import com.gpillaca.counters.data.datasource.RemoteDataSource
import com.gpillaca.counters.data.datasource.RetrofitDataSource
import com.gpillaca.counters.data.datasource.RoomDataSource
import com.gpillaca.counters.data.repository.CounterRepository
import com.gpillaca.counters.data.repository.CounterRepositoryImpl
import com.gpillaca.counters.data.server.CounterDbService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    fun roomDataSourceProvider(
        appDatabase: AppDatabase
    ): LocalDataSource = RoomDataSource(appDatabase)

    @Provides
    fun retrofitDataSourceProvider(
        counterDbService: CounterDbService
    ): RemoteDataSource = RetrofitDataSource(counterDbService)

    @Provides
    fun counterRepositoryProvider(
        remoteDataSource: RemoteDataSource,
        localDataSource: LocalDataSource
    ): CounterRepository = CounterRepositoryImpl(remoteDataSource, localDataSource)
}
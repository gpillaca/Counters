package com.gpillaca.counters.data.repository

import com.gpillaca.counters.data.detasource.RemoteDataSource
import com.gpillaca.counters.domain.Counter
import com.gpillaca.counters.ui.common.OperationResults
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CounterRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource
) : CounterRepository {
    override suspend fun listCounters(): OperationResults<Counter> = withContext(Dispatchers.IO) {
        remoteDataSource.listCounters()
    }
}
package com.gpillaca.counters.data.repository

import com.gpillaca.counters.data.datasource.LocalDataSource
import com.gpillaca.counters.data.datasource.RemoteDataSource
import com.gpillaca.counters.domain.Counter
import com.gpillaca.counters.ui.common.OperationResults
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CounterRepositoryImpl @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
) : CounterRepository {

    override suspend fun listCounters(forceUpdate: Boolean): OperationResults<Counter> = withContext(Dispatchers.IO) {
        if (localDataSource.isEmpty() || forceUpdate) {
            when (val result = remoteDataSource.listCounters()) {
                is OperationResults.Success -> {

                    if (forceUpdate && !localDataSource.isEmpty()) {
                        localDataSource.deleteAll()
                    }

                    localDataSource.saveCounters(result.data)
                }
                is OperationResults.Error -> {
                    OperationResults.Error(result.exception)
                }
            }
        }

        OperationResults.Success(localDataSource.getAllCounters())
    }

    override suspend fun addCounter(title: String): OperationResults<Counter> = withContext(Dispatchers.IO) {
        when (val result = remoteDataSource.addCounter(title)) {
            is OperationResults.Success -> {
                localDataSource.deleteAll()
                localDataSource.saveCounters(result.data)
                OperationResults.Success(localDataSource.getAllCounters())
            }
            is OperationResults.Error -> {
                OperationResults.Error(result.exception)
            }
        }
    }

    override suspend fun increment(id: String): OperationResults<Counter> = withContext(Dispatchers.IO) {
        remoteDataSource.incrementCounter(id)
    }

    override suspend fun decrement(id: String): OperationResults<Counter> = withContext(Dispatchers.IO) {
        remoteDataSource.decrementCounter(id)
    }

    override suspend fun deleteCounter(counter: Counter): OperationResults<Counter> = withContext(Dispatchers.IO) {
        when (val result = remoteDataSource.deleteCounter(counter.id)) {
            is OperationResults.Success -> {
                localDataSource.deleteCounter(counter)
                OperationResults.Success(result.data)
            }
            is OperationResults.Error -> {
                OperationResults.Error(result.exception)
            }
        }
    }
}
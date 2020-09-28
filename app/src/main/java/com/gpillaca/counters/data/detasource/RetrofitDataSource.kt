package com.gpillaca.counters.data.detasource

import com.gpillaca.counters.data.mappers.toDomainCounter
import com.gpillaca.counters.data.server.CounterDbService
import com.gpillaca.counters.domain.Counter
import com.gpillaca.counters.ui.common.OperationResults

private const val KEY_ID = "id"
private const val KEY_TITLE = "title"

class RetrofitDataSource(private val counterDbService: CounterDbService) : RemoteDataSource {
    override suspend fun listCounters(): OperationResults<Counter> {
        return try {
            val counters: List<Counter> = counterDbService.listCounters().map {
                it.toDomainCounter()
            }

            OperationResults.Success(counters)
        } catch (e: Exception) {
            OperationResults.Error(e)
        }
    }

    override suspend fun addCounter(title: String): OperationResults<Counter> {
        return try {
            val request = mutableMapOf(KEY_TITLE to title)
            val counters: List<Counter> = counterDbService.addCounter(request).map {
                it.toDomainCounter()
            }

            OperationResults.Success(counters)
        } catch (e: Exception) {
            OperationResults.Error(e)
        }
    }

    override suspend fun incrementCounter(id: String): OperationResults<Counter> {
        return try {
            val request = mutableMapOf(KEY_ID to id)
            val counters: List<Counter> = counterDbService.incrementCounter(request).map {
                it.toDomainCounter()
            }

            OperationResults.Success(counters)
        } catch (e: Exception) {
            OperationResults.Error(e)
        }
    }

    override suspend fun decrementCounter(id: String): OperationResults<Counter> {
        return try {
            val request = mutableMapOf(KEY_ID to id)
            val counters: List<Counter> = counterDbService.decrementCounter(request).map {
                it.toDomainCounter()
            }

            OperationResults.Success(counters)
        } catch (e: Exception) {
            OperationResults.Error(e)
        }
    }

    override suspend fun deleteCounter(id: String): OperationResults<Counter> {
        return try {
            val request = mutableMapOf(KEY_ID to id)
            val counters: List<Counter> = counterDbService.deleteCounter(request).map {
                it.toDomainCounter()
            }

            OperationResults.Success(counters)
        } catch (e: Exception) {
            OperationResults.Error(e)
        }
    }
}
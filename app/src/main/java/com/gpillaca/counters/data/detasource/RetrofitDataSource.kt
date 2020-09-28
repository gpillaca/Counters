package com.gpillaca.counters.data.detasource

import com.gpillaca.counters.data.mappers.toDomainCounter
import com.gpillaca.counters.data.server.CounterDbService
import com.gpillaca.counters.domain.Counter
import com.gpillaca.counters.ui.common.OperationResults

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
            val request = mutableMapOf("title" to title)
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
            val request = mutableMapOf("id" to id)
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
            val request = mutableMapOf("id" to id)
            val counters: List<Counter> = counterDbService.decrementCounter(request).map {
                it.toDomainCounter()
            }

            OperationResults.Success(counters)
        } catch (e: Exception) {
            OperationResults.Error(e)
        }
    }
}
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
}
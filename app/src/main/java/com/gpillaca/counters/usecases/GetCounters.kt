package com.gpillaca.counters.usecases

import com.gpillaca.counters.data.repository.CounterRepository
import com.gpillaca.counters.domain.Counter
import com.gpillaca.counters.ui.common.OperationResults
import javax.inject.Inject

interface GetCounters {
    suspend fun invoke(): OperationResults<Counter>
}

class GetCountersImpl @Inject constructor(
    private val counterRepository: CounterRepository
) : GetCounters {
    override suspend fun invoke(): OperationResults<Counter> = counterRepository.listCounters()
}
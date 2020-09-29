package com.gpillaca.counters.usecases

import com.gpillaca.counters.data.repository.CounterRepository
import com.gpillaca.counters.domain.Counter
import com.gpillaca.counters.ui.common.OperationResults
import javax.inject.Inject

interface DecrementCounter {
    suspend fun invoke(id: String): OperationResults<Counter>
}

class DecrementCounterImpl @Inject constructor(
    private val counterRepository: CounterRepository
) : DecrementCounter {
    override suspend fun invoke(id: String): OperationResults<Counter> =
        counterRepository.deleteCounter(id)
}
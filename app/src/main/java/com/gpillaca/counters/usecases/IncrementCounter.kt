package com.gpillaca.counters.usecases

import com.gpillaca.counters.data.repository.CounterRepository
import com.gpillaca.counters.domain.Counter
import com.gpillaca.counters.ui.common.OperationResults
import javax.inject.Inject

interface IncrementCounter {
    suspend fun invoke(counter: Counter): OperationResults<Counter>
}

class IncrementCounterImpl @Inject constructor(
    private val counterRepository: CounterRepository
) : IncrementCounter {
    override suspend fun invoke(counter: Counter): OperationResults<Counter> =
        counterRepository.increment(counter)
}
package com.gpillaca.counters.usecases

import com.gpillaca.counters.data.repository.CounterRepository
import com.gpillaca.counters.domain.Counter
import com.gpillaca.counters.ui.common.OperationResults
import javax.inject.Inject

interface DeleteCounter {
    suspend fun invoke(counter: Counter): OperationResults<Counter>
}

class DeleteCounterImpl @Inject constructor(
    private val counterRepository: CounterRepository
) : DeleteCounter {
    override suspend fun invoke(counter: Counter): OperationResults<Counter> =
        counterRepository.deleteCounter(counter)
}
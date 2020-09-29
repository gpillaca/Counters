package com.gpillaca.counters.usecases

import com.gpillaca.counters.data.repository.CounterRepository
import com.gpillaca.counters.domain.Counter
import com.gpillaca.counters.ui.common.OperationResults
import javax.inject.Inject

interface AddCounter {
    suspend fun invoke(title: String): OperationResults<Counter>
}

class AddCounterImpl @Inject constructor(
    private val counterRepository: CounterRepository
) : AddCounter {
    override suspend fun invoke(title: String): OperationResults<Counter> =
        counterRepository.addCounter(title)
}
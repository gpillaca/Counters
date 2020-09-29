package com.gpillaca.counters.usecases

import com.gpillaca.counters.data.repository.CounterRepository
import com.gpillaca.counters.domain.Counter
import com.gpillaca.counters.ui.common.OperationResults
import javax.inject.Inject

class GetCounters @Inject constructor(
    private val counterRepository: CounterRepository
) {
    suspend fun invoke(): OperationResults<Counter> = counterRepository.listCounters()
}
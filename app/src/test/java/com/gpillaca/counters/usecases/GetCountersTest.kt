package com.gpillaca.counters.usecases

import com.gpillaca.counters.data.repository.CounterRepository
import com.gpillaca.counters.domain.Counter
import com.gpillaca.counters.mockedCounter
import com.gpillaca.counters.ui.common.OperationResults
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GetCountersTest {

    @Mock
    lateinit var counterRepository: CounterRepository

    lateinit var getCounters: GetCounters

    @Before
    fun setUp() {
        getCounters = GetCountersImpl(counterRepository)
    }

    @Test
    fun `invoke calls counter repository`() = runBlocking {
        val counters = listOf<Counter>(mockedCounter.copy(id = "qwerty"))

        whenever(counterRepository.listCounters(forceUpdate = true)).thenReturn(
            OperationResults.Success(counters)
        )

        val result = getCounters.invoke(forceUpdate = true)

        Assert.assertEquals(OperationResults.Success(counters), result)
    }
}
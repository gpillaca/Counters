package com.gpillaca.counters.usecases

import com.gpillaca.counters.data.repository.CounterRepository
import com.gpillaca.counters.mockedCounter
import com.gpillaca.counters.ui.common.OperationResults
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.whenever

@RunWith(MockitoJUnitRunner::class)
class IncrementCounterTest {

    @Mock
    lateinit var counterRepository: CounterRepository

    lateinit var incrementCounter: IncrementCounter

    @Before
    fun setUp() {
        incrementCounter = IncrementCounterImpl(counterRepository)
    }

    @Test
    fun `invoke calls counter repository`() = runBlocking {
        val counter = mockedCounter
        val counters = listOf(mockedCounter.copy(id = "qwerty"))

        whenever(counterRepository.increment(counter)).thenReturn(
            OperationResults.Success(counters)
        )

        val result = incrementCounter.invoke(counter)

        Assert.assertEquals(OperationResults.Success(counters), result)
    }
}
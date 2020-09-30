package com.gpillaca.counters.usecases

import com.gpillaca.counters.data.repository.CounterRepository
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
class DecrementCounterTest {

    @Mock
    lateinit var counterRepository: CounterRepository

    lateinit var decrementCounter: DecrementCounter

    @Before
    fun setUp() {
        decrementCounter = DecrementCounterImpl(counterRepository)
    }

    @Test
    fun `invoke calls counter repository`() = runBlocking {
        val counter = mockedCounter
        val counters = listOf(mockedCounter.copy(id = "qwerty"))

        whenever(counterRepository.decrement(counter)).thenReturn(
            OperationResults.Success(counters)
        )

        val result =  decrementCounter.invoke(counter)

        Assert.assertEquals(OperationResults.Success(counters), result)
    }
}
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
class AddCounterTest {

    @Mock
    lateinit var counterRepository: CounterRepository

    lateinit var addCounter: AddCounter

    @Before
    fun setUp() {
        addCounter = AddCounterImpl(counterRepository)
    }

    @Test
    fun `invoke calls counter repository`() = runBlocking {
        val title = "Chicken sandwich"
        val counters = listOf<Counter>(mockedCounter.copy(id = "qwerty"))

        whenever(counterRepository.addCounter(title)).thenReturn(OperationResults.Success(counters))

        val result = addCounter.invoke(title)

        Assert.assertEquals(OperationResults.Success(counters), result)
    }
}
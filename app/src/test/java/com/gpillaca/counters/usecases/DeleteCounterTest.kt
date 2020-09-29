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
class DeleteCounterTest {

    @Mock
    lateinit var counterRepository: CounterRepository

    lateinit var deleteCounter: DeleteCounter

    @Before
    fun setUp() {
        deleteCounter = DeleteCounterImpl(counterRepository)
    }

    @Test
    fun `invoke calls counter repository`() = runBlocking {
        val id = "qwerty"
        val counters = listOf(mockedCounter.copy(id = "qwerty"))

        whenever(counterRepository.deleteCounter(id)).thenReturn(
            OperationResults.Success(counters)
        )

        val result = deleteCounter.invoke(id)

        Assert.assertEquals(OperationResults.Success(counters), result)
    }
}
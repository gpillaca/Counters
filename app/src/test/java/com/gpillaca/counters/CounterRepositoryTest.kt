package com.gpillaca.counters

import com.gpillaca.counters.data.detasource.RemoteDataSource
import com.gpillaca.counters.data.repository.CounterRepository
import com.gpillaca.counters.data.repository.CounterRepositoryImpl
import com.gpillaca.counters.ui.common.OperationResults
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class CounterRepositoryTest {

    @Mock
    lateinit var remoteDataSource: RemoteDataSource

    lateinit var counterRepository: CounterRepository

    @Before
    fun setUp() {
        counterRepository = CounterRepositoryImpl(remoteDataSource)
    }

    @Test
    fun `Counters gets from remote database`() = runBlocking {
        val databaseCounter = listOf(mockedCounter.copy(id = "c5"))

        whenever(remoteDataSource.listCounters()).thenReturn(
            OperationResults.Success(
                databaseCounter
            )
        )

        val result = counterRepository.listCounters()
        assertEquals(OperationResults.Success(databaseCounter), result)
    }

    @Test
    fun `Counter saves data to remote database`() = runBlocking {
        val title = "Cups of coffee"
        val databaseCounters = mockedCounters

        whenever(remoteDataSource.addCounter(title)).thenReturn(
            OperationResults.Success(
                mockedCounters
            )
        )
        val result = remoteDataSource.addCounter(title)

        assertEquals(OperationResults.Success(databaseCounters), result)
    }

    @Test
    fun `Delete counter of remote database`() = runBlocking {
        val id = "c1"
        val databaseCounters = mockedCounters

        whenever(remoteDataSource.deleteCounter(id)).thenReturn(
            OperationResults.Success(
                mockedCounters
            )
        )

        val result = remoteDataSource.deleteCounter(id)

        assertEquals(OperationResults.Success(databaseCounters), result)
    }

    @Test
    fun `Increment value of counter`() = runBlocking {
        val id = "c2"
        val databaseCounters = mockedCounters

        whenever(remoteDataSource.incrementCounter(id)).thenReturn(
            OperationResults.Success(
                mockedCounters
            )
        )

        val result = remoteDataSource.incrementCounter(id)

        assertEquals(OperationResults.Success(databaseCounters), result)
    }

    @Test
    fun `Decrement value of counter`() = runBlocking {
        val id = "c3"
        val databaseCounters = mockedCounters

        whenever(remoteDataSource.decrementCounter(id)).thenReturn(
            OperationResults.Success(
                mockedCounters
            )
        )

        val result = remoteDataSource.decrementCounter(id)

        assertEquals(OperationResults.Success(databaseCounters), result)
    }
}
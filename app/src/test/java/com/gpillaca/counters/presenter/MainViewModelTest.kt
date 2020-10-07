package com.gpillaca.counters.presenter

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.gpillaca.counters.mockedCounters
import com.gpillaca.counters.ui.common.OperationResults
import com.gpillaca.counters.ui.main.CounterUiModel
import com.gpillaca.counters.ui.main.MainViewModel
import com.gpillaca.counters.usecases.DecrementCounter
import com.gpillaca.counters.usecases.DeleteCounter
import com.gpillaca.counters.usecases.GetCounters
import com.gpillaca.counters.usecases.IncrementCounter
import com.gpillaca.counters.util.AndroidHelper
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.setMain
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    lateinit var androidHelper: AndroidHelper

    @Mock
    lateinit var getCounters: GetCounters

    @Mock
    lateinit var deleteCounter: DeleteCounter

    @Mock
    lateinit var incrementCounter: IncrementCounter

    @Mock
    lateinit var decrementCounter: DecrementCounter

    @Mock
    lateinit var observer: Observer<CounterUiModel>

    lateinit var viewModel: MainViewModel

    @Before
    fun setUp() {
        viewModel = MainViewModel(
            androidHelper,
            getCounters,
            deleteCounter,
            incrementCounter,
            decrementCounter
        )
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `Load counters`() = runBlocking {
        Dispatchers.setMain(Dispatchers.Unconfined)

        val counters = mockedCounters
        val items = counters.size
        val times = 9

        whenever(androidHelper.hasNetworkConnection()).thenReturn(true)
        whenever(getCounters.invoke(forceUpdate = true)).thenReturn(OperationResults.Success(counters))

        viewModel.model.observeForever(observer)
        viewModel.loadCounters(forceUpdate = true)

        Mockito.verify(observer).onChanged(CounterUiModel.Loading)
        Mockito.verify(observer).onChanged(CounterUiModel.Success(counters, items, times))
    }
}
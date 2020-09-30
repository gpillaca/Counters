package com.gpillaca.counters.presenter

import com.gpillaca.counters.mockedCounters
import com.gpillaca.counters.ui.common.OperationResults
import com.gpillaca.counters.ui.main.CounterUiModel
import com.gpillaca.counters.ui.main.MainContract
import com.gpillaca.counters.ui.main.MainPresenter
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
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class MainPresenterTest {
    @Mock
    lateinit var androidHelper: AndroidHelper

    @Mock
    lateinit var view: MainContract.View

    @Mock
    lateinit var getCounters: GetCounters

    @Mock
    lateinit var deleteCounter: DeleteCounter

    @Mock
    lateinit var incrementCounter: IncrementCounter

    @Mock
    lateinit var decrementCounter: DecrementCounter

    lateinit var presenter: MainContract.Presenter

    @Before
    fun setUp() {
        presenter = MainPresenter(
            view,
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
        whenever(getCounters.invoke()).thenReturn(OperationResults.Success(counters))

        presenter.loadCounters()

        Mockito.verify(view).show(CounterUiModel.Loading)
        Mockito.verify(view).show(CounterUiModel.Success(counters, items, times))
    }
}
package com.gpillaca.counters.presenter

import com.gpillaca.counters.mockedCounters
import com.gpillaca.counters.ui.addcounter.AddCounterContract
import com.gpillaca.counters.ui.addcounter.AddCounterPresenter
import com.gpillaca.counters.ui.addcounter.AddCounterUiModel
import com.gpillaca.counters.ui.common.OperationResults
import com.gpillaca.counters.usecases.AddCounter
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
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class AddCounterPresenterTest {

    @Mock
    lateinit var androidHelper: AndroidHelper

    @Mock
    lateinit var view: AddCounterContract.View

    @Mock
    lateinit var addCounter: AddCounter

    lateinit var presenter: AddCounterContract.Presenter

    @Before
    fun setUp() {
        presenter = AddCounterPresenter(view, androidHelper, addCounter)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `add Counter`() = runBlocking {
        Dispatchers.setMain(Dispatchers.Unconfined)

        val title = "Chicken sandwich"
        val counters = mockedCounters

        whenever(androidHelper.hasNetworkConnection()).thenReturn(true)
        whenever(addCounter.invoke(title)).thenReturn(OperationResults.Success(counters))

        presenter.createCounter(title)

        verify(view).show(AddCounterUiModel.Loading)
        verify(view).show(AddCounterUiModel.Success)
    }
}
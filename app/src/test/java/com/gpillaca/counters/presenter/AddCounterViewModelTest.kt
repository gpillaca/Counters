package com.gpillaca.counters.presenter

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.gpillaca.counters.mockedCounters
import com.gpillaca.counters.ui.addcounter.AddCounterUiModel
import com.gpillaca.counters.ui.addcounter.AddCounterViewModel
import com.gpillaca.counters.ui.common.OperationResults
import com.gpillaca.counters.usecases.AddCounter
import com.gpillaca.counters.util.AndroidHelper
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
import org.mockito.kotlin.whenever

@RunWith(MockitoJUnitRunner::class)
class AddCounterViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @Mock
    lateinit var androidHelper: AndroidHelper

    @Mock
    lateinit var addCounter: AddCounter

    @Mock
    lateinit var observer: Observer<AddCounterUiModel>

    lateinit var viewModel: AddCounterViewModel

    @Before
    fun setUp() {
        viewModel = AddCounterViewModel(androidHelper, addCounter)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun `add Counter`() = runBlocking {
        Dispatchers.setMain(Dispatchers.Unconfined)

        val title = "Chicken sandwich"
        val counters = mockedCounters

        whenever(androidHelper.hasNetworkConnection()).thenReturn(true)
        whenever(addCounter.invoke(title)).thenReturn(OperationResults.Success(counters))

        viewModel.model.observeForever(observer)
        viewModel.createCounter(title)

        Mockito.verify(observer).onChanged(AddCounterUiModel.Loading)
        Mockito.verify(observer).onChanged(AddCounterUiModel.Success)
    }
}
package com.gpillaca.counters.di

import android.app.Activity
import com.gpillaca.counters.ui.main.MainActivity
import com.gpillaca.counters.ui.main.MainContract
import com.gpillaca.counters.ui.main.MainPresenter
import com.gpillaca.counters.usecases.DeleteCounter
import com.gpillaca.counters.usecases.DeleteCounterImpl
import com.gpillaca.counters.usecases.GetCounters
import com.gpillaca.counters.usecases.GetCountersImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@InstallIn(ActivityComponent::class)
@Module
abstract class MainModule {

    @Binds
    abstract fun bindActivity(mainActivity: MainActivity): MainContract.View

    @Binds
    abstract fun bindPresenter(mainPresenter: MainPresenter): MainContract.Presenter
}

@InstallIn(ActivityComponent::class)
@Module
class MainUseCaseModule {

    @Provides
    fun getCountersProvider(getCountersImpl: GetCountersImpl): GetCounters = getCountersImpl

    @Provides
    fun deleteCountersProvider(deleteCounterImpl: DeleteCounterImpl): DeleteCounter =
        deleteCounterImpl
}

@InstallIn(ActivityComponent::class)
@Module
object MainActivityModule {

    @Provides
    fun bindActivity(activity: Activity): MainActivity {
        return activity as MainActivity
    }
}
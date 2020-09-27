package com.gpillaca.counters.di

import android.app.Activity
import com.gpillaca.counters.ui.addcounter.AddCounterActivity
import com.gpillaca.counters.ui.addcounter.AddCounterContract
import com.gpillaca.counters.ui.addcounter.AddCounterPresenter
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@InstallIn(ActivityComponent::class)
@Module
abstract class AddCounterModule {

    @Binds
    abstract fun bindActivity(addCounterActivity: AddCounterActivity): AddCounterContract.View

    @Binds
    abstract fun bindPresenter(addCounterPresenter: AddCounterPresenter): AddCounterContract.Presenter
}

@InstallIn(ActivityComponent::class)
@Module
object AddCounterActivityModule {

    @Provides
    fun bindActivity(activity: Activity): AddCounterActivity {
        return activity as AddCounterActivity
    }
}
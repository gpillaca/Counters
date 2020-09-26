package com.gpillaca.counters.data.server

import com.gpillaca.counters.BuildConfig
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {
    private fun retrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val counterDbService: CounterDbService by lazy { retrofit().create(CounterDbService::class.java) }
}
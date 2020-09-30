package com.gpillaca.counters.data.datasource

import com.gpillaca.counters.data.database.AppDatabase
import com.gpillaca.counters.data.mappers.toDatabaseCounter
import com.gpillaca.counters.data.mappers.toDomainCounter
import com.gpillaca.counters.domain.Counter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RoomDataSource @Inject constructor(
    private val appDatabase: AppDatabase
) : LocalDataSource {
    override suspend fun isEmpty(): Boolean = withContext(Dispatchers.IO) {
        (appDatabase.counterDao().counterCount()) <= 0
    }

    override suspend fun saveCounters(counters: List<Counter>) {
        val counterList = counters.map {
            it.toDatabaseCounter()
        }

        appDatabase.counterDao().insertCounters(counterList)
    }

    override suspend fun getAllCounters(): List<Counter> = withContext(Dispatchers.IO) {
        appDatabase.counterDao().getAll().map {
            it.toDomainCounter()
        }
    }

    override suspend fun deleteAll() = withContext(Dispatchers.IO) {
        appDatabase.counterDao().deleteAll()
    }

    override suspend fun deleteCounter(counter: Counter) = withContext(Dispatchers.IO) {
        appDatabase.counterDao().deleteCounter(counter.toDatabaseCounter())
    }
}
package com.gpillaca.counters.data.database

import androidx.room.*

@Dao
interface CounterDAO {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insertCounters(counters: List<Counter>)

    @Query("SELECT * FROM Counter")
    fun getAll(): List<Counter>

    @Query("SELECT COUNT(id) FROM Counter")
    fun counterCount(): Int

    @Update
    fun updateCounter(counter: Counter)

    @Query("DELETE FROM Counter")
    fun deleteAll()
}
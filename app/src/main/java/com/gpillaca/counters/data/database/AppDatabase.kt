package com.gpillaca.counters.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

private const val DATA_BASE = "counterDatabase.db"

@Database(entities = [Counter::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun counterDao(): CounterDAO

    companion object {
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(AppDatabase::class) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DATA_BASE
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
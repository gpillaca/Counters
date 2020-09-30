package com.gpillaca.counters.data.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Counter(
    @PrimaryKey(autoGenerate = false) val id: String,
    @ColumnInfo val title: String,
    @ColumnInfo var count: Int
) {
    @ColumnInfo var isSelected: Boolean = false
}
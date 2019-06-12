package com.example.rcademo.database

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "daily_sleep_quality_table")
data class SleepNight(
    @PrimaryKey(autoGenerate = true)
    var nightId:Long = 0L,

    @ColumnInfo(name = "start_time_milli")
    var startTimeMilli:Long = System.currentTimeMillis(),

    @ColumnInfo(name = "end_time_mill")
    var endTimeMilli:Long = startTimeMilli,

    @ColumnInfo(name = "quality_ratting")
    var sleepQuality:Int = -1
)
package com.example.rcademo.database

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update

@Dao
interface SleepDatabaseDao{
    @Insert
    fun insert(night: SleepNight)

    @Update
    fun update(night: SleepNight)

    @Query("select * from daily_sleep_quality_table Order By nightId Desc")
    fun getAllNight():LiveData<List<SleepNight>>

    @Query("Delete from daily_sleep_quality_table")
    fun clear()

    @Query("select * from daily_sleep_quality_table where nightId =:key")
    fun getOne(key:Long):SleepNight

    @Query("select * from daily_sleep_quality_table order by nightId desc limit  1")
    fun getLast():SleepNight?
}
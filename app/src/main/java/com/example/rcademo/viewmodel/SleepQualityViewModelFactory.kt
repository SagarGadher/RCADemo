package com.example.rcademo.viewmodel

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProvider
import android.arch.lifecycle.ViewModelProviders
import com.example.rcademo.database.SleepDatabaseDao

class SleepQualityViewModelFactory(private val sleepQualityKey:Long,private val dataSource:SleepDatabaseDao): ViewModelProvider.Factory{
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SleepQualityViewModel::class.java)) {
            return SleepQualityViewModel(sleepQualityKey,dataSource) as T
        }
        throw IllegalArgumentException("Unknown Model Class")
    }
}
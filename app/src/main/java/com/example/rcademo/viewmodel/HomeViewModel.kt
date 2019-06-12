package com.example.rcademo.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Transformations
import com.example.android.trackmysleepquality.formatNights
import com.example.rcademo.database.SleepDatabaseDao
import com.example.rcademo.database.SleepNight
import kotlinx.coroutines.*

class HomeViewModel(val database: SleepDatabaseDao, application: Application) : AndroidViewModel(application) {
    private var viewModelJob = Job()
    private var uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private var tonight = MutableLiveData<SleepNight?>()
    private var night = database.getAllNight()

    val nightString = Transformations.map(night){
        formatNights(it,application.resources)
    }

    private val _navigateToSleepQuality = MutableLiveData<SleepNight>()

    val navigateToSleepQuality :LiveData<SleepNight>
    get() = _navigateToSleepQuality

    fun doneNavigate(){
        _navigateToSleepQuality.value = null
    }

    private var _showSnackbarEvent = MutableLiveData<Boolean>()

    val showSnackBarEvent: LiveData<Boolean>
        get() = _showSnackbarEvent

    fun doneShowingSnackbar() {
        _showSnackbarEvent.value = false
    }

    init {
        initializeTonight()
    }

    private fun initializeTonight() {
        uiScope.launch {
            tonight.value = getTonightFromDatabase()
        }
    }

    private suspend fun getTonightFromDatabase(): SleepNight? {
        return withContext(Dispatchers.IO) {
            var night = database.getLast()
            if (night?.startTimeMilli != night?.endTimeMilli) {
                night = null
            }
            night
        }
    }

    fun onStartTracking() {
        uiScope.launch {
            val newNight = SleepNight()
            insert(newNight)
            tonight.value = getTonightFromDatabase()
        }
    }

    private suspend fun insert(newNight :SleepNight){
        withContext(Dispatchers.IO){
            database.insert(newNight)
        }
    }

    fun onStopTracking(){
        uiScope.launch {
            val oldNight = tonight.value ?: return@launch

            oldNight.endTimeMilli = System.currentTimeMillis()
            update(oldNight)
            _navigateToSleepQuality.value = oldNight
        }
    }
    private suspend fun update(oldNight:SleepNight){
        withContext(Dispatchers.IO){
            database.update(oldNight)
        }
    }

    fun onClear(){
        uiScope.launch {
            clear()
            tonight.value = null
            _showSnackbarEvent.value = true
        }
    }
    private suspend fun clear(){
        withContext(Dispatchers.IO){
            database.clear()
        }
    }
    var startbtnVisible = Transformations.map(tonight){ null == it }
    var stopbtnVisible = Transformations.map(tonight){null != it}
    var clearbtnVisible = Transformations.map(night){ !(it?.isEmpty())!! }

}
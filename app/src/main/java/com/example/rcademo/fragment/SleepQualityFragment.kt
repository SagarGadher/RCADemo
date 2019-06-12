package com.example.rcademo.fragment


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController

import com.example.rcademo.R
import com.example.rcademo.database.SleepDatabase
import com.example.rcademo.databinding.LayoutFragmentSleepQualityBinding
import com.example.rcademo.viewmodel.SleepQualityViewModel
import com.example.rcademo.viewmodel.SleepQualityViewModelFactory

class SleepQualityFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val binding: LayoutFragmentSleepQualityBinding =
            DataBindingUtil.inflate(inflater, R.layout.layout_fragment_sleep_quality, container, false)

        val application = requireNotNull(this.activity).application

        val arguments = SleepQualityFragmentArgs.fromBundle(arguments!!)

        val dataSource = SleepDatabase.getInstance(application).sleepDatabaseDao

        val viewModelFactory = SleepQualityViewModelFactory(arguments.sleepQualityKey, dataSource)

        val sleepQualityViewModel = ViewModelProviders.of(this, viewModelFactory).get(SleepQualityViewModel::class.java)
        binding.qualityModel = sleepQualityViewModel
        sleepQualityViewModel.navigateToSleepTracker.observe(this, Observer {
            if (it == true) {
                this.findNavController().navigate(SleepQualityFragmentDirections.actionSleepQualityFragmentToHomeFragment())
                sleepQualityViewModel.doneNavigating()
            }
        })
        return binding.root
    }
}

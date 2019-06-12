package com.example.rcademo.fragment


import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.databinding.DataBindingUtil
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.rcademo.R
import com.example.rcademo.database.SleepDatabase
import com.example.rcademo.databinding.LayoutFragmentHomeBinding
import com.example.rcademo.viewmodel.HomeViewModel
import com.example.rcademo.viewmodel.HomeViewModelFactory

class HomeFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val binding: LayoutFragmentHomeBinding =
            DataBindingUtil.inflate(inflater, R.layout.layout_fragment_home, container, false)

        val application = requireNotNull(this.activity).application
        val dataSource = SleepDatabase.getInstance(application).sleepDatabaseDao
        val viewModelFactory = HomeViewModelFactory(dataSource, application)

        val viewModel = ViewModelProviders.of(this, viewModelFactory).get(HomeViewModel::class.java)

        binding.homeViewModel = viewModel

        viewModel.navigateToSleepQuality.observe(this, Observer {
            it?.let {
                this.findNavController()
                    .navigate(HomeFragmentDirections.actionHomeFragmentToSleepQualityFragment(it.nightId))
                viewModel.doneNavigate()
            }
        })
        viewModel.showSnackBarEvent.observe(this, Observer {
            if (it == true) {
                Snackbar.make(activity!!.findViewById(android.R.id.content),"All History were cleard", Snackbar.LENGTH_SHORT).show()
                viewModel.doneShowingSnackbar()
            }
        })

        binding.lifecycleOwner = this
        return binding.root
    }
}

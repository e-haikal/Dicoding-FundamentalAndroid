package com.siaptekno.dicodingevent.ui.home

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.siaptekno.dicodingevent.R
import com.siaptekno.dicodingevent.databinding.FragmentHomeBinding
import com.siaptekno.dicodingevent.ui.detail.DetailActivity
import com.siaptekno.dicodingevent.ui.search.SearchActivity


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var upcomingAdapter: HomeUpcomingAdapter
    private lateinit var finishedAdapter: HomeFinishedAdapter
    private lateinit var viewModel: HomeViewModel

    private val autoSlideHandler = Handler(Looper.getMainLooper())
    private var currentPage = 0
    private val autoSlideInterval: Long = 3000 // 3 seconds

    // Add this runnable for auto-sliding
    private val autoSlideRunnable = object : Runnable {
        override fun run() {
            if (upcomingAdapter.itemCount > 0) {
                binding.viewPagerUpcoming.currentItem = currentPage
                currentPage = (currentPage + 1) % upcomingAdapter.itemCount
            }
            autoSlideHandler.postDelayed(this, autoSlideInterval)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this, HomeFactory.getInstance(requireContext())).get(HomeViewModel::class.java)
        upcomingAdapter = HomeUpcomingAdapter { event ->
            val intent = Intent(requireContext(), DetailActivity::class.java)
            intent.putExtra("EVENT_ID", event.id)
            startActivity(intent)
        }
        finishedAdapter = HomeFinishedAdapter{ event ->
            val intent = Intent(requireContext(), DetailActivity::class.java)
            intent.putExtra("EVENT_ID", event.id)
            startActivity(intent)
        }
        binding.viewPagerUpcoming.adapter = upcomingAdapter
        binding.rvFinishedHome.adapter = finishedAdapter
        binding.rvFinishedHome.layoutManager = LinearLayoutManager(requireContext())

        binding.searchBar.setOnClickListener {
            val intent = Intent(requireContext(), SearchActivity::class.java)
            startActivity(intent)
        }

        observeViewModel()


        viewModel.finishedEvents.observe(viewLifecycleOwner) { events ->
            finishedAdapter.submitList(events)
        }

        if (isNetworkAvailable()) {
            viewModel.loadUpcoming5()
            viewModel.loadFinished5()
        } else {
            showAlertDialog()
        }

    }

    private fun observeViewModel() {
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            showLoading(isLoading)
        }

        viewModel.upcomingEvents.observe(viewLifecycleOwner) { events ->
            upcomingAdapter.submitList(events)

            currentPage = 0
            autoSlideHandler.postDelayed(autoSlideRunnable, autoSlideInterval)
        }

        viewModel.finishedEvents.observe(viewLifecycleOwner) {events ->
            finishedAdapter.submitList(events)
        }

        viewModel.errorMessage.observe(viewLifecycleOwner) { error ->
            if (error != null) {
                Toast.makeText(requireContext(), error, Toast.LENGTH_SHORT).show()
            }
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        autoSlideHandler.removeCallbacks(autoSlideRunnable) // Stop auto-slide

        _binding = null
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showAlertDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(getString(R.string.no_internet))
        builder.setMessage(getString(R.string.check_internet))
        builder.setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
        val dialog = builder.create()
        dialog.show()
    }

    private fun isNetworkAvailable(): Boolean {
        val connectivityManager = requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = connectivityManager.activeNetwork?.let {
                connectivityManager.getNetworkCapabilities(it)
            }
            return networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
        } else {
            val activeNetworkInfo = connectivityManager.activeNetworkInfo
            return activeNetworkInfo != null && activeNetworkInfo.isConnected
        }
    }
}
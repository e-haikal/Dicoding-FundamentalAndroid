package com.siaptekno.dicodingevent.ui.upcoming

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.siaptekno.dicodingevent.R
import com.siaptekno.dicodingevent.data.response.Response
import com.siaptekno.dicodingevent.data.retrofit.ApiConfig
import com.siaptekno.dicodingevent.databinding.FragmentUpcomingBinding
import retrofit2.Call
import retrofit2.Callback


class UpcomingFragment : Fragment() {
    private  var _binding: FragmentUpcomingBinding? = null // Backing property
    private val binding get() = _binding!! // Non-null assertion for access
    private lateinit var adapter: UpcomingAdapter // Initialize in onCreateView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the binding
        _binding = FragmentUpcomingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        fetchUpcomingEvents()
    }


    private fun setupRecyclerView() {
        // Set up RecyclerView
        adapter = UpcomingAdapter()
        // Set the layout manager for the RecyclerView
        binding.rvUpcomingEvents.layoutManager = LinearLayoutManager(requireContext())
        // Attach the adapter to the RecyclerView
        binding.rvUpcomingEvents.adapter = adapter
        // Add item decoration for a divider
        val itemDecoration = DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL)
        binding.rvUpcomingEvents.addItemDecoration(itemDecoration)
    }

    private fun fetchUpcomingEvents() {
        showLoading(true)
        val client = ApiConfig.getApiService().getEvents(active = 1) // 1 for upcoming events
        client.enqueue(object : Callback<Response> {
            override fun onResponse(call: Call<Response>, response: retrofit2.Response<Response>) {
                showLoading(false)
                if (response.isSuccessful) {
                    val eventList = response.body()?.listEvents
                    if (eventList != null) {
                        adapter.submitList(eventList)
                    }
                } else {
                    // Handle unsuccessful response
                    showError("Failed to load events")
                }

            }

            override fun onFailure(call: Call<Response>, t: Throwable) {
                showLoading(false)
                showError("Error: ${t.message}")
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showError(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Step 3c: Clear the binding reference
    }

}
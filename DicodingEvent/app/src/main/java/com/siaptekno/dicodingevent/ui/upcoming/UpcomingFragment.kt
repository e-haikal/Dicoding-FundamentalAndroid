package com.siaptekno.dicodingevent.ui.upcoming

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.siaptekno.dicodingevent.R
import com.siaptekno.dicodingevent.data.response.EventResponse
import com.siaptekno.dicodingevent.data.response.ListEventsItem
import com.siaptekno.dicodingevent.databinding.FragmentUpcomingBinding

class UpcomingFragment : Fragment() {
    private var _binding: FragmentUpcomingBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: UpcomingViewModel
    private lateinit var adapter: UpcomingAdapter

    companion object {
        private const val TAG = "UpcomingFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentUpcomingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel() // Initialize ViewModel
        setupRecyclerView() // Initialize RecyclerView
        observeViewModel() // Observe ViewModel data
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(requireActivity()).get(UpcomingViewModel::class.java)
    }

    private fun setupRecyclerView() {
        adapter = UpcomingAdapter(requireContext()) // Pass the Fragment context
        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvUpcomingEvents.layoutManager = layoutManager
        binding.rvUpcomingEvents.adapter = adapter

        binding.rvUpcomingEvents.addItemDecoration(
            DividerItemDecoration(requireContext(), layoutManager.orientation)
        )
    }

    private fun observeViewModel() {
        viewModel.upcomingEvents.observe(viewLifecycleOwner) { events ->
            adapter.submitList(events)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

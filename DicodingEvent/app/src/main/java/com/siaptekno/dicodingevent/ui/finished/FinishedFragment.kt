package com.siaptekno.dicodingevent.ui.finished

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.siaptekno.dicodingevent.R
import com.siaptekno.dicodingevent.databinding.FragmentFinishedBinding
import com.siaptekno.dicodingevent.databinding.FragmentUpcomingBinding
import com.siaptekno.dicodingevent.ui.upcoming.UpcomingAdapter
import com.siaptekno.dicodingevent.ui.upcoming.UpcomingViewModel

class FinishedFragment : Fragment() {
    private lateinit var viewModel: FinishedViewModel
    private lateinit var adapter: FinishedAdapter
    private var _binding: FragmentFinishedBinding? = null
    private val binding get() = _binding!!

    companion object
    {
        private const val TAG = "FinishedFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentFinishedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel() // Initialize ViewModel
        setupRecyclerView() // Initialize RecyclerView
        observeViewModel() // Observe ViewModel data
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(requireActivity()).get(FinishedViewModel::class.java)
    }

    private fun setupRecyclerView() {
        adapter = FinishedAdapter()
        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvFinishedEvent.layoutManager = layoutManager
        binding.rvFinishedEvent.adapter = adapter

        binding.rvFinishedEvent.addItemDecoration(
            DividerItemDecoration(requireContext(), layoutManager.orientation)
        )
    }

    private fun observeViewModel() {
        viewModel.finishedEvents.observe(viewLifecycleOwner) { events ->
            adapter.submitList(events)
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBarFinished.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
package com.siaptekno.dicodingevent.ui.upcoming

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.siaptekno.dicodingevent.data.remote.response.ListEventsItem
import com.siaptekno.dicodingevent.databinding.FragmentUpcomingBinding

/*

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
*/

class UpcomingFragment : Fragment() {

    private var tabName: String? = null

    private var _binding: FragmentUpcomingBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: UpcomingViewModel
    private lateinit var adapter: UpcomingAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentUpcomingBinding.inflate(layoutInflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory = ViewModelFactory.getInstance(requireContext())
        viewModel = ViewModelProvider(this, factory)[UpcomingViewModel::class.java]

        setupRecyclerView()

        // Observe events and handle conversion here
        viewModel.getEvents().observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE  // Show loading indicator
                }
                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE  // Hide loading indicator

                    // Convert EventEntity to ListEventsItem
                    val events = result.data.map { entity ->
                        ListEventsItem(
                            id = entity.id,
                            name = entity.name,
                            summary = entity.summary,
                            description = entity.description,
                            imageLogo = entity.imageLogo,
                            mediaCover = entity.mediaCover,
                            category = entity.category,
                            ownerName = entity.ownerName,
                            cityName = entity.cityName,
                            quota = entity.quota,
                            registrants = entity.registrants,
                            beginTime = entity.beginTime,
                            endTime = entity.endTime,
                            link = entity.link
                        )
                    }
                    adapter.submitList(events)  // Populate RecyclerView
                }
                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE  // Hide loading indicator
                    // Show an error message (e.g., Toast or Snackbar)
                    Toast.makeText(requireContext(), "Error: ${result.error}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun setupRecyclerView() {
        adapter = UpcomingAdapter(requireContext())
        val layoutManager = LinearLayoutManager(requireContext())
        binding.rvUpcomingEvents.layoutManager = layoutManager
        binding.rvUpcomingEvents.adapter = adapter
        binding.rvUpcomingEvents.addItemDecoration(
            DividerItemDecoration(requireContext(), layoutManager.orientation)
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
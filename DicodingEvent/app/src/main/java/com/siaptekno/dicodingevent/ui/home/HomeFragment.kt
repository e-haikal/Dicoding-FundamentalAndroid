package com.siaptekno.dicodingevent.ui.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.siaptekno.dicodingevent.R
import com.siaptekno.dicodingevent.databinding.FragmentHomeBinding
import com.siaptekno.dicodingevent.ui.EventAdapter

class HomeFragment : Fragment() {
    // Binding object instance corresponding to the fragment_home.xml layout
    private  var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var upcomingEventAdapter : EventAdapter
    private lateinit var completedEventAdapter : EventAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        upcomingEventAdapter = EventAdapter()
        completedEventAdapter = EventAdapter()

        binding.rvUpcomingEvents.adapter = upcomingEventAdapter
        binding.rvCompletedEvents.adapter = completedEventAdapter
    }
}
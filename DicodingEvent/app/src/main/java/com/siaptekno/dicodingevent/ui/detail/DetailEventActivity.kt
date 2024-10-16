package com.siaptekno.dicodingevent.ui.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.siaptekno.dicodingevent.R
import com.siaptekno.dicodingevent.databinding.ActivityDetailEventBinding

class DetailEventActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailEventBinding
    private val viewModel: DetailEventViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailEventBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get the event ID from the intent
        val eventId = intent.getIntExtra("EVENT_ID", -1)
        Log.d("DetailEventActivity", "Received EVENT_ID: $eventId")

        // Fetch event details
        viewModel.fetchEventDetails(eventId)

        // Observe event detail data
        viewModel.eventDetail.observe(this) { event ->
            // Update UI with the event details
            binding.tvDetailEventName.text = event.name
            binding.tvDataEventOwner.text = event.ownerName
            binding.tvDataBeginTime.text = event.beginTime
            binding.tvDataQuota.text = "${event.quota - event.registrants} slots left"
            binding.tvDataDescription.text = event.description

            // Load images using Glide
            Glide.with(this)
                .load(event.mediaCover)
                .into(binding.ivDetailEventImage)
        }

        // Observe loading state to show/hide the loading indicator
        viewModel.isLoading.observe(this) { isLoading ->
            binding.loadingIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
//
//        // Set click listener for the register button
//        binding.actionRegister.setOnClickListener {
//            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(event.link))
//            startActivity(intent)
//        }
    }
}

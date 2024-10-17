package com.siaptekno.dicodingevent.ui.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge

import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.siaptekno.dicodingevent.R
import com.siaptekno.dicodingevent.databinding.ActivityDetailEventBinding

class DetailEventActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var detailEventViewModel: DetailEventViewModel
    private lateinit var binding: ActivityDetailEventBinding
    private var link: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailEventBinding.inflate(layoutInflater)
        setContentView(binding.root)
        enableEdgeToEdge()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_detail_event)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        detailEventViewModel = DetailEventViewModel() // Initialize ViewModel
        val eventId = intent.getIntExtra("EXTRA_EVENT_ID", -1) // Get event ID from intent

        if (eventId != -1) {
            detailEventViewModel.fetchDetailEvent(eventId) // Fetch event details
        }

        // Observe LiveData and update UI
        detailEventViewModel.detailEvents.observe(this) { event ->
            event?.let {
                binding.tvDetailEventName.text = event.name
    //            binding.tvEventOwner.text = event.ownerName
                val ownerValue = event.ownerName
                binding.tvDataEventOwner.text = "Diselenggarkaan oleh: $ownerValue"
    //            binding.tvBeginTime.text = event.beginTime
                binding.tvDataBeginTime.text = event.beginTime
    //            binding.tvQuota.text = event.quota.toString()
                val quotaValue = event.quota.toString()
                binding.tvDataQuota.text = "Sisa Quota $quotaValue"
                binding.tvDataDescription.text = HtmlCompat.fromHtml(event.description, HtmlCompat.FROM_HTML_MODE_LEGACY)
                Glide.with(this)
                    .load(event.mediaCover)
                    .into(binding.ivDetailEventBanner)
                Glide.with(this)
                    .load(event.imageLogo)
                    .into(binding.ivDetailEventBanner)
                link = event.link
            }
        }

        val registerButton = binding.actionRegister
        registerButton.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.action_register -> {
                // Handle register button click
                link?.let { url ->
                    val webPage: Uri = Uri.parse(url)
                    val registerIntent = Intent(Intent.ACTION_VIEW, webPage)
                    startActivity(registerIntent)
                }
            }
        }
    }

}

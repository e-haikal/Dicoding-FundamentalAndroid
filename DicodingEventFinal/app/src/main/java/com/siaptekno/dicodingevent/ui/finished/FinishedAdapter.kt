package com.siaptekno.dicodingevent.ui.finished

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.siaptekno.dicodingevent.data.remote.response.ListEventsItem
import com.siaptekno.dicodingevent.databinding.ItemFinishedBinding
import com.siaptekno.dicodingevent.ui.detail.DetailEventActivity

class FinishedAdapter(private val context: Context) : ListAdapter<ListEventsItem, FinishedAdapter.MyViewHolder>(DIFF_CALLBACK) {
    class MyViewHolder(val binding: ItemFinishedBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(event: ListEventsItem?) {
            binding.tvFinishedEventName.text = event?.name

            // Load image using Glide
            Glide.with(binding.ivFinishedEventImage.context)
                .load(event?.mediaCover)
                .into(binding.ivFinishedEventImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemFinishedBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val event = getItem(position)
        holder.bind(event)
        holder.itemView.setOnClickListener {
            val intent = Intent(context, DetailEventActivity::class.java).apply {
                putExtra("EXTRA_EVENT_ID", event.id)
            }
            context.startActivity(intent)
        }
        
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListEventsItem>() {
            override fun areItemsTheSame(oldItem: ListEventsItem, newItem: ListEventsItem): Boolean {
                return oldItem.id == newItem.id // Check ID for unique item comparison
            }

            override fun areContentsTheSame(oldItem: ListEventsItem, newItem: ListEventsItem): Boolean {
                return oldItem == newItem // Compare item content
            }
        }
    }
}

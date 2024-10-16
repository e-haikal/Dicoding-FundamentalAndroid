package com.siaptekno.dicodingevent.ui.finished

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.siaptekno.dicodingevent.data.response.ListEventsItem
import com.siaptekno.dicodingevent.databinding.ItemFinishedBinding
import com.siaptekno.dicodingevent.ui.detail.DetailEventActivity

class FinishedAdapter : ListAdapter<ListEventsItem, FinishedAdapter.MyViewHolder>(DIFF_CALLBACK) {
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
            if (event != null) {
                Log.d("FinishedAdapter", "Navigating to detail with EVENT_ID: ${event.id}")
                val intent = Intent(holder.itemView.context, DetailEventActivity::class.java).apply {
                    putExtra("EVENT_ID", event.id)
                }
                holder.itemView.context.startActivity(intent)
            } else {
                Log.e("FinishedAdapter", "Event is null, cannot navigate to detail.")
            }
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

package com.siaptekno.dicodingevent.ui.finished

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.siaptekno.dicodingevent.R
import com.siaptekno.dicodingevent.data.response.ListEventsItem
import com.siaptekno.dicodingevent.databinding.ItemFinishedBinding
import com.siaptekno.dicodingevent.databinding.ItemUpcomingBinding


class FinishedAdapter : ListAdapter<ListEventsItem, FinishedAdapter.MyViewHolder>(DIFF_CALLBACK) {
    class MyViewHolder(val binding: ItemFinishedBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(event: ListEventsItem?) {
            binding.tvFinishedEventName.text = event?.name
//            binding.tvUpcomingEventDescription.text = event?.description

            // load image using Glide or Picasso
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
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListEventsItem>() {
            override fun areItemsTheSame(oldItem: ListEventsItem, newItem: ListEventsItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ListEventsItem, newItem: ListEventsItem): Boolean {
                return oldItem == newItem
            }

        }
    }
}
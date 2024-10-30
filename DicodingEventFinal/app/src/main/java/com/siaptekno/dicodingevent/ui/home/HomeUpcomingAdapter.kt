package com.siaptekno.dicodingevent.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.siaptekno.dicodingevent.data.remote.response.ListEventsItem
import com.siaptekno.dicodingevent.databinding.ItemCarouselUpcomingBinding


class HomeUpcomingAdapter(private val onItemClick: (ListEventsItem) -> Unit ): ListAdapter<ListEventsItem, HomeUpcomingAdapter.MyViewHolder>(DIFF_CALLBACK) {

    class MyViewHolder(val binding: ItemCarouselUpcomingBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(event: ListEventsItem, onItemClick: (ListEventsItem) -> Unit ) {
            Glide.with(binding.imgHomeUpcoming.context)
                .load(event.mediaCover)
                .into(binding.imgHomeUpcoming)

            binding.root.setOnClickListener {
                onItemClick(event)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemCarouselUpcomingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val event = getItem(position)
        holder.bind(event, onItemClick)
    }


    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListEventsItem>() {
            override fun areItemsTheSame(
                oldItem: ListEventsItem,
                newItem: ListEventsItem
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: ListEventsItem,
                newItem: ListEventsItem
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}
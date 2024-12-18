package com.siaptekno.dicodingevent.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.siaptekno.dicodingevent.R
import com.siaptekno.dicodingevent.data.remote.response.ListEventsItem
import com.siaptekno.dicodingevent.databinding.ItemFinishedHomeBinding


class HomeFinishedAdapter(private val onItemClick: (ListEventsItem) -> Unit ): ListAdapter<ListEventsItem, HomeFinishedAdapter.MyViewHolder>(DIFF_CALLBACK) {
    class MyViewHolder(val binding: ItemFinishedHomeBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(event: ListEventsItem, onItemClick:(ListEventsItem) -> Unit ) {
            Glide.with(binding.imgFinishedHome.context)
                .load(event.imageLogo)
                .into(binding.imgFinishedHome)
            binding.nameFinishedHome.text = event.name
            binding.ownerFinishedHome.text = binding.root.context.getString(R.string.owner, event.ownerName)

            binding.root.setOnClickListener {
                onItemClick(event)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemFinishedHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
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
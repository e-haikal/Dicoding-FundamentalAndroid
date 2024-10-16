package com.siaptekno.dicodingevent.ui.event

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.siaptekno.dicodingevent.R
import com.siaptekno.dicodingevent.data.response.ListEventsItem

class EventAdapter(
    private val events: List<ListEventsItem>,
    private val itemClickListener: (ListEventsItem) -> Unit // Lambda function for click listener
) : RecyclerView.Adapter<EventAdapter.EventViewHolder>() {

    // ViewHolder for holding view references
    class EventViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val eventName: TextView = view.findViewById(R.id.tvEventName)
        private val eventImage: ImageView = view.findViewById(R.id.ivEventImage)

        // Bind event data to views
        fun bind(event: ListEventsItem, clickListener: (ListEventsItem) -> Unit) {
            eventName.text = event.name
            Glide.with(itemView.context)
                .load(event.imageLogo)
                .into(eventImage)

            // Set click listener
            itemView.setOnClickListener { clickListener(event) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EventViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_event, parent, false)
        return EventViewHolder(view)
    }

    override fun onBindViewHolder(holder: EventViewHolder, position: Int) {
        // Bind event data to ViewHolder
        holder.bind(events[position], itemClickListener)
    }

    override fun getItemCount(): Int = events.size // Return the number of events
}

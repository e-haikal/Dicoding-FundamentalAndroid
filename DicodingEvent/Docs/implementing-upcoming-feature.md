# Implementing Upcoming Events Feature in Kotlin Android  

This comprehensive guide will walk you through creating a feature that fetches and displays a list of upcoming events from an API using **Kotlin Android**. The implementation involves network calls using Retrofit, data management with ViewModel and LiveData, and displaying a list using RecyclerView. This guide is structured to help beginners understand each step thoroughly, with detailed explanations and inline comments for the code.

---

## Table of Contents  
1. [Objective](#objective)  
2. [Requirements](#requirements)  
3. [Why Use These Components](#why-use-these-components)  
4. [Basic Concepts](#basic-concepts)  
5. [Project Structure](#project-structure)  
6. [Step-by-Step Implementation](#step-by-step-implementation)  
7. [Code Walkthrough with Inline Comments](#code-walkthrough-with-inline-comments)  
8. [Conclusion](#conclusion)  

---

## Objective  
The goal is to build a fragment-based feature to display upcoming events fetched from the Dicoding Event API in an Android application. The feature will show a loading indicator during data fetching and handle potential errors. The steps include:  
- Integrating with the Dicoding Event API.  
- Using **Retrofit** for network calls.  
- Managing data with **ViewModel** and **LiveData**.  
- Displaying events in **RecyclerView**.  
- Handling errors and showing a loading indicator.

---

## Requirements  
1. **API Integration:** Use the Dicoding Event API for data fetching.  
2. **Network Handling:** Implement Retrofit for API requests and responses.  
3. **Data Handling:** Manage state with ViewModel and LiveData.  
4. **UI Components:** Use RecyclerView for displaying a list of events.  
5. **Loading Indicator:** Show a progress bar while data is being loaded.  
6. **Error Handling:** Manage and log API call failures for debugging.

---

## Why Use These Components  
- **Retrofit:** A widely used library for network requests in Android, making it easy to handle JSON-based API responses.  
- **ViewModel and LiveData:** Provides lifecycle-aware data management, ensuring data remains up-to-date and is retained across configuration changes.  
- **RecyclerView:** Efficiently displays large datasets with smooth scrolling and flexible layout options.  
- **Glide:** Efficient for loading and caching images from the web.

---

## Basic Concepts  
Before proceeding, let's cover some essential concepts that will be used in this guide:  

- **API Request:** A request to retrieve data from a server (in our case, the Dicoding Event API).  
- **ViewModel:** Stores and manages UI-related data in a lifecycle-conscious way, allowing data to survive configuration changes (like screen rotations).  
- **LiveData:** An observable data holder that updates the UI automatically when the data changes.  
- **RecyclerView Adapter:** Acts as a bridge between the data source and RecyclerView to display data in a list format.  
- **Data Binding:** Links UI components to data sources in a manner that keeps the UI in sync with the data.

---

## Project Structure  

Your project structure should look like this:  
```plaintext
com/siaptekno/dicodingevent
│
├── data
│   ├── response
│   │   └── EventResponse.kt
│   └── retrofit
│       ├── ApiConfig.kt
│       └── ApiService.kt
├── ui
│   └── upcoming
│       ├── UpcomingFragment.kt
│       ├── UpcomingViewModel.kt
│       ├── UpcomingAdapter.kt
│    
└── layout
    └── item_upcoming.xml
    └── fragment_upcoming.xml
```

---

## Step-by-Step Implementation  

### 1. Setting Up Retrofit for Network Requests  

**Retrofit** is a popular library for making HTTP requests in Android. In our implementation, it will be used to interact with the Dicoding Event API to fetch event data.

- **ApiConfig**: The configuration class to set up the Retrofit instance with a base URL and an interceptor for logging network requests and responses.  

### 2. Defining API Response Models  

We need data classes to map the JSON response to Kotlin objects. The primary model will represent the response structure from the API.  

### 3. Creating the ViewModel  

A `ViewModel` will be used to manage the data fetching process and update the UI accordingly. It will expose **LiveData** for observing changes in data.  

### 4. Implementing RecyclerView Adapter  

A custom **RecyclerView Adapter** will bind the event data to individual items in the list view. Each item will represent a single event with information such as the event name, date, and organizer.  

### 5. Creating the Fragment for Upcoming Events  

The `UpcomingFragment` will initialize the ViewModel, set up the RecyclerView, observe the data, and display the loading indicator when needed.

---

## Code Walkthrough with Inline Comments  

### 1. API Response Model (`EventResponse.kt`)  

Let's define the data model representing the response from the API.

```kotlin
package com.siaptekno.dicodingevent.data.response

import com.google.gson.annotations.SerializedName

// Data class representing the API response structure
data class EventResponse(
    @field:SerializedName("listEvents") val listEvents: List<ListEventsItem>,
    @field:SerializedName("error") val error: Boolean, // Indicates if there was an error
    @field:SerializedName("message") val message: String // Error or success message
)

// Data class representing an individual event item
data class ListEventsItem(
    @field:SerializedName("summary") val summary: String,
    @field:SerializedName("mediaCover") val mediaCover: String, // URL of the media cover
    @field:SerializedName("registrants") val registrants: Int, // Number of registrants
    @field:SerializedName("imageLogo") val imageLogo: String, // URL of the event logo
    @field:SerializedName("link") val link: String, // External link to the event page
    @field:SerializedName("description") val description: String, // Event description
    @field:SerializedName("ownerName") val ownerName: String, // Name of the event organizer
    @field:SerializedName("cityName") val cityName: String, // Location of the event
    @field:SerializedName("quota") val quota: Int, // Maximum number of participants
    @field:SerializedName("name") val name: String, // Event name
    @field:SerializedName("beginTime") val beginTime: String, // Event start time
    @field:SerializedName("endTime") val endTime: String, // Event end time
    @field:SerializedName("category") val category: String // Event category
)
```

### 2. API Configuration (`ApiConfig.kt`)  

Configure Retrofit to make network requests to the Dicoding Event API.

```kotlin
package com.siaptekno.dicodingevent.data.retrofit

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiConfig {
    companion object {
        // Provides a configured instance of ApiService for making API requests
        fun getApiService(): ApiService {
            // Create a logging interceptor to log the details of each HTTP request and response
            val loggingInterceptor = HttpLoggingInterceptor().apply {
                setLevel(HttpLoggingInterceptor.Level.BODY)
            }
            // Build an OkHttpClient with the logging interceptor
            val client = OkHttpClient.Builder().addInterceptor(loggingInterceptor).build()
            // Build and return the Retrofit instance
            return Retrofit.Builder()
                .baseUrl("https://event-api.dicoding.dev/") // Base URL of the API
                .addConverterFactory(GsonConverterFactory.create()) // Converter for parsing JSON
                .client(client)
                .build()
                .create(ApiService::class.java) // Create the ApiService interface
        }
    }
}
```

### 3. API Service Interface (`ApiService.kt`)  

Defines the endpoint to interact with the API.

```kotlin
package com.siaptekno.dicodingevent.data.retrofit

import com.siaptekno.dicodingevent.data.response.EventResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("events")
    // Function to get a list of events, with optional parameters for active status and limit
    fun getEvents(@Query("active") active: Int = 1, @Query("limit") limit: Int = 40): Call<EventResponse>
}
```

### 4. ViewModel for Upcoming Events (`UpcomingViewModel.kt`)  

Manages the data fetching and updates the UI through LiveData.

```kotlin
package com.siaptekno.dicodingevent.ui.upcoming

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.siaptekno.dicodingevent.data.response.ListEventsItem
import com.siaptekno.dicodingevent.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpcomingViewModel : ViewModel() {
    // LiveData for the list of upcoming events
    private val _upcomingEvents = MutableLiveData<List<ListEventsItem>>()
    val upcomingEvents: LiveData<List<ListEventsItem>> get() = _upcomingEvents

    // LiveData for the loading state
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    // Function to fetch upcoming events from the API
    fun fetchUpcomingEvents() {
        _isLoading.value = true // Set loading state to true
        val apiService = ApiConfig.getApiService() // Get the ApiService instance
        apiService.getEvents(1, 40).enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                // Check if the response is successful
                if (response.isSuccessful) {
                    _upcomingEvents.value = response.body()?.listEvents // Update upcoming events LiveData
                } else {
                    // Handle the error case
                    _upcomingEvents.value = emptyList() // Set an empty list if there's an error
                }
                _isLoading.value = false // Set loading state to false
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                // Handle the failure case
                _upcomingEvents.value = emptyList() // Set an empty list if there's a failure
                _isLoading.value = false // Set loading state to false
            }
        })
    }
}
```

### 5. RecyclerView Adapter for Upcoming Events (`UpcomingAdapter.kt`)  

Handles the binding of event data to the RecyclerView.

```kotlin
package com.siaptekno.dicodingevent.ui.upcoming

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.siaptekno.dicodingevent.R
import com.siaptekno.dicodingevent.data.response.ListEventsItem

class UpcomingAdapter(private val events: List<ListEventsItem>) : RecyclerView.Adapter<UpcomingAdapter.UpcomingViewHolder>() {

    // ViewHolder class for holding the views for each event item
    class UpcomingViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val eventName: TextView = view.findViewById(R.id.tv_event_name)
        val eventDate: TextView = view.findViewById(R.id.tv_event_date)
        val eventImage: ImageView = view.findViewById(R.id.img_event) // Image view for event logo
    }

    // Inflate the layout for each item in the RecyclerView
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UpcomingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_upcoming, parent, false)
        return UpcomingViewHolder(view)
    }

    // Bind data to the views for each event item
    override fun onBindViewHolder(holder: UpcomingViewHolder, position: Int) {
        val event = events[position]
        holder.eventName.text = event.name // Set event name
        holder.eventDate.text = event.beginTime // Set event date

        // Load the event logo image using Glide
        Glide.with(holder.eventImage.context)
            .load(event.imageLogo)
            .into(holder.eventImage)
    }

    // Return the total number of items in the list
    override fun getItemCount(): Int = events.size
}
```

### 6. Upcoming Fragment Layout (`fragment_upcoming.xml`)  

Define the layout for the Upcoming Fragment, which includes a RecyclerView and a loading indicator.

```xml
<LinearLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical">

    <ProgressBar
        android:id="@+id/progress_bar"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_gravity="center"
        android:visibility="gone"/> <!-- Initially hidden -->

    <androidx.recyclerview.widget.RecyclerView
        android:id="@+id/recycler_view_upcoming"
        android:layout_width="match_parent"
        android:layout_height="match_parent"/>
</LinearLayout>
```

### 7. Item Layout for Each Event (`item_upcoming.xml`)  

Define the layout for individual event items in the RecyclerView.

```xml
<LinearLayout
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:orientation="vertical"
    android:padding="16dp">

    <ImageView
        android:id="@+id/img_event"
        android:layout_width="match_parent"
        android:layout_height="200dp"
        android:scaleType="centerCrop"/>

    <TextView
        android:id="@+id/tv_event_name"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:textSize="16sp"
        android:textStyle="bold"
        android:layout_marginTop="8dp"/>

    <TextView
        android:id="@+id/tv_event_date"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:textSize="14sp"
        android:layout_marginTop="4dp"/>
</LinearLayout>
```

### 8. Upcoming Fragment Implementation (`UpcomingFragment.kt`)  

Connect all components in the fragment, observing the ViewModel and updating the UI based on data changes.

```kotlin
package com.siaptekno.dicodingevent.ui.upcoming

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.siaptekno.dicodingevent.R
import com.siaptekno.dicodingevent.data.response.ListEventsItem
import kotlinx.android.synthetic.main.fragment_upcoming.*

class UpcomingFragment : Fragment() {
    private lateinit var upcomingViewModel: UpcomingViewModel
    private lateinit var upcomingAdapter: UpcomingAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_upcoming, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // Initialize the ViewModel
        upcomingViewModel = UpcomingViewModel()
        
        // Set up the RecyclerView
        upcomingAdapter = UpcomingAdapter(emptyList()) // Initialize with an empty list
        recycler_view_upcoming.layoutManager = LinearLayoutManager(context)
        recycler_view_upcoming.adapter = upcomingAdapter

        // Observe the loading state
        upcomingViewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            progress_bar.visibility = if (isLoading) View.VISIBLE else View.GONE // Show or hide loading indicator
        })

        // Observe the list of upcoming events
        upcomingViewModel.upcomingEvents.observe(viewLifecycleOwner, Observer { events ->
            upcomingAdapter = UpcomingAdapter(events) // Update the adapter with new events
            recycler_view_upcoming.adapter = upcomingAdapter // Set the updated adapter
        })

        // Fetch upcoming events
        upcomingViewModel.fetchUpcomingEvents()
    }
}
```

---

## Conclusion  

In this article, we've covered the implementation of an Upcoming Events feature in a Kotlin Android application, utilizing the Dicoding Event API. We discussed the importance of components like Retrofit, ViewModel, LiveData, and RecyclerView, and walked through each step of the implementation in detail. 

### Key Takeaways:  
- **Retrofit** simplifies network requests and responses.  
- **ViewModel** and **LiveData** help manage UI-related data efficiently.  
- **RecyclerView** provides a flexible way to display lists of data.  
- Proper error handling and loading indicators enhance user experience.

By following this guide, you should now have a solid understanding of how to implement similar features in your Kotlin Android applications. Feel free to expand on this project by adding additional features or improving the UI! Happy coding!


---- 
#### Reference
1. Explained by GPT
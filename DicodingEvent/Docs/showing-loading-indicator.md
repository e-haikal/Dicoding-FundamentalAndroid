# How to Implement a Loading Indicator in a Kotlin Android App

In Android development, providing feedback to users while data is being fetched is crucial for a good user experience. This article will guide you through implementing a loading indicator that displays while your app fetches data from an API. We will use a ProgressBar and Kotlin's ViewModel architecture for this purpose.

## Overview of Components

1. **ViewModel**: Manages the UI-related data and handles API calls.
2. **Fragment**: Acts as the UI controller, displaying data and managing user interactions.
3. **Adapter**: Binds data to the RecyclerView for display.
4. **Layout XML**: Defines the user interface, including the ProgressBar and RecyclerView.

## Step-by-Step Implementation

### Step 1: Create the ViewModel

The `UpcomingViewModel` handles data fetching and maintains the loading state.

```kotlin
// UpcomingViewModel.kt
package com.siaptekno.dicodingevent.ui.upcoming

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.siaptekno.dicodingevent.data.response.EventResponse
import com.siaptekno.dicodingevent.data.response.ListEventsItem
import com.siaptekno.dicodingevent.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UpcomingViewModel : ViewModel() {
    private val _upcomingEvents = MutableLiveData<List<ListEventsItem>>() // Holds the upcoming events data
    val upcomingEvents: LiveData<List<ListEventsItem>> = _upcomingEvents

    private val _isLoading = MutableLiveData<Boolean>() // Holds the loading state
    val isLoading: LiveData<Boolean> = _isLoading

    companion object {
        private const val TAG = "UpcomingViewModel"
    }

    init {
        fetchUpcomingEvents() // Fetch events when ViewModel is initialized
    }

    private fun fetchUpcomingEvents() {
        _isLoading.value = true // Set loading state to true when starting data fetch
        val client = ApiConfig.getApiService().getEvents() // API call

        client.enqueue(object : Callback<EventResponse> {
            override fun onResponse(call: Call<EventResponse>, response: Response<EventResponse>) {
                _isLoading.value = false // Set loading state to false after receiving response
                if (response.isSuccessful) {
                    _upcomingEvents.value = response.body()?.listEvents // Update events data
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<EventResponse>, t: Throwable) {
                _isLoading.value = false // Set loading state to false if an error occurs
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }
}
```

### Step 2: Create the Adapter

The `UpcomingAdapter` binds the event data to the RecyclerView.

```kotlin
// UpcomingAdapter.kt
package com.siaptekno.dicodingevent.ui.upcoming

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.siaptekno.dicodingevent.data.response.ListEventsItem
import com.siaptekno.dicodingevent.databinding.ItemUpcomingBinding

class UpcomingAdapter : ListAdapter<ListEventsItem, UpcomingAdapter.MyViewHolder>(DIFF_CALLBACK) {
    class MyViewHolder(val binding: ItemUpcomingBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(event: ListEventsItem?) {
            binding.tvUpcomingEventName.text = event?.name // Bind event name
            // Load image using Glide
            Glide.with(binding.ivUpcomingEventImage.context)
                .load(event?.mediaCover)
                .into(binding.ivUpcomingEventImage) // Bind event image
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemUpcomingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val event = getItem(position)
        holder.bind(event) // Bind event data to ViewHolder
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
```

### Step 3: Create the Fragment

The `UpcomingFragment` sets up the ViewModel, RecyclerView, and observes the loading state.

```kotlin
// UpcomingFragment.kt
package com.siaptekno.dicodingevent.ui.upcoming

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.siaptekno.dicodingevent.databinding.FragmentUpcomingBinding

class UpcomingFragment : Fragment() {
    private var _binding: FragmentUpcomingBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: UpcomingViewModel
    private lateinit var adapter: UpcomingAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentUpcomingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViewModel() // Initialize ViewModel
        setupRecyclerView() // Initialize RecyclerView
        observeViewModel() // Observe ViewModel data
    }

    private fun setupViewModel() {
        viewModel = ViewModelProvider(requireActivity()).get(UpcomingViewModel::class.java) // Get ViewModel instance
    }

    private fun setupRecyclerView() {
        adapter = UpcomingAdapter() // Initialize adapter
        binding.rvUpcomingEvents.layoutManager = LinearLayoutManager(requireContext()) // Set layout manager
        binding.rvUpcomingEvents.adapter = adapter // Set adapter to RecyclerView
        binding.rvUpcomingEvents.addItemDecoration(DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL)) // Add item decoration
    }

    private fun observeViewModel() {
        viewModel.upcomingEvents.observe(viewLifecycleOwner) { events ->
            adapter.submitList(events) // Submit event data to adapter
        }

        // Observe loading state to show or hide the ProgressBar
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE // Show or hide the ProgressBar
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // Clear binding reference
    }
}
```

### Step 4: Layout XML Files

#### Fragment Layout with ProgressBar and RecyclerView

```xml
<!-- fragment_upcoming.xml -->
<?xml version="1.0" encoding="utf-8"?>
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <ProgressBar
        android:id="@+id/progressBar" <!-- ProgressBar for loading indicator -->
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:layout_gravity="center" <!-- Center the ProgressBar -->
        android:visibility="gone" /> <!-- Initially hidden -->

    <androidx.recyclerview.widget.RecyclerView
        android:id="@+id/rvUpcomingEvents" <!-- RecyclerView for displaying events -->
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:layout_margin="16dp" />
</FrameLayout>
```

### Step 5: Item Layout for RecyclerView

```xml
<!-- item_upcoming.xml -->
<?xml version="1.0" encoding="utf-8"?>
<com.google.android.material.card.MaterialCardView xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:elevation="8dp"
    android:layout_marginBottom="8dp">

    <androidx.constraintlayout.widget.ConstraintLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:padding="8dp">

        <ImageView
            android:id="@+id/ivUpcomingEventImage" <!-- ImageView for event image -->
            android:layout_width="0dp"
            android:layout_height="250dp"
            android:scaleType="centerCrop"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintEnd_toEndOf="parent"
            app:layout_constraintTop_toTopOf="parent" />

        <TextView
            android:id="@+id/tvUpcomingEventName" <!-- TextView for event name -->
            android:layout_width="0dp"
            android:layout_height="wrap_content"
            android:layout_marginStart="8dp"
            android:textSize="18sp"
            android:textStyle="bold"
           

 app:layout_constraintTop_toBottomOf="@id/ivUpcomingEventImage"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintEnd_toEndOf="parent" />

    </androidx.constraintlayout.widget.ConstraintLayout>
</com.google.android.material.card.MaterialCardView>
```

## Connecting the Components

### Data Flow:
1. **Initialization**:
   - When the `UpcomingFragment` is created, it initializes the `UpcomingViewModel` and the `RecyclerView`.
  
2. **Data Fetching**:
   - The `UpcomingViewModel` fetches data from the API during its initialization.
   - The `_isLoading` LiveData is set to `true` while the data is being fetched and to `false` once the data is received or an error occurs.

3. **UI Updates**:
   - The `UpcomingFragment` observes the `isLoading` LiveData. When it becomes `true`, the ProgressBar is displayed; when `false`, it is hidden.
   - The `upcomingEvents` LiveData is also observed, and when data is available, it is submitted to the `UpcomingAdapter`, which updates the RecyclerView.

## Conclusion

Implementing a loading indicator in your Kotlin Android app enhances user experience by informing users that data is being loaded. This guide demonstrated how to set up a loading indicator while fetching data from an API.

### Key Takeaways:
- Use **ViewModel** to handle data fetching and loading states.
- Use **LiveData** to observe data changes in your UI.
- Show and hide the **ProgressBar** based on the loading state.
- Use **RecyclerView** and an adapter to display lists of data efficiently.

By following this structured approach, you'll be able to create a responsive and user-friendly application that handles data fetching smoothly. Feel free to experiment with the code and adapt it to your specific needs!

---

#### Reference
1. Explained by GPT
package com.siaptekno.dicodingevent.ui

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.siaptekno.dicodingevent.R
import com.siaptekno.dicodingevent.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    // Binding object instance corresponding to the activity_main.xml layout
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        // Initialize the binding object
        binding = ActivityMainBinding.inflate(layoutInflater)
        // Set the content view to the root view of the binding
        setContentView(binding.root)

        // Initialize the bottom navigation view
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.nav_view)

        // Apply window insets to the main view
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            // Apply padding to the main view
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)

            // Adjust the bottom navigation bar
            bottomNavigationView.setPadding(0, 0, 0, systemBars.bottom)
            insets
        }

        // Set up the bottom navigation view
        val navView: BottomNavigationView = binding.navView
        // Set up the navigation controller
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Set up the app bar appBar to shows as menu, if ID not added here, it will show the back button
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_upcoming, R.id.navigation_finished
            )
        )
        // Used to show Title in Action Bar match with the fragment that is displayed
        setupActionBarWithNavController(navController, appBarConfiguration)
        // Navigation bar will show the fragment that is selected
        navView.setupWithNavController(navController)

        navView.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    navController.navigate(R.id.navigation_home)
                    Log.d("Navigation", "Navigated to HomeFragment")
                    true
                }
                R.id.navigation_upcoming -> {
                    navController.navigate(R.id.navigation_upcoming)
                    Log.d("Navigation", "Navigated to UpcomingFragment")
                    true
                }
                R.id.navigation_finished -> {
                    navController.navigate(R.id.navigation_finished)
                    Log.d("Navigation", "Navigated to FinishedFragment")
                    true
                }
                else -> false
            }
        }
    }
}
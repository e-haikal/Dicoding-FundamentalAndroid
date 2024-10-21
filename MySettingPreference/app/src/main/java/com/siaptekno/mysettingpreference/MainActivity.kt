// MainActivity.kt
// This activity sets up the main interface and initializes the preference fragment.

package com.siaptekno.mysettingpreference

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {
    // Called when the activity is created
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge() // Enables edge-to-edge display for a full-screen experience
        setContentView(R.layout.activity_main) // Sets the content view for the activity

        // Set padding for the setting holder based on system bar insets
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.setting_holder)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            // Adjust padding based on system bar insets
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets // Return the insets for further processing
        }

        // Start the MyPreferenceFragment to display preferences
        supportFragmentManager.beginTransaction().add(R.id.setting_holder, MyPreferenceFragment()).commit()
    }
}

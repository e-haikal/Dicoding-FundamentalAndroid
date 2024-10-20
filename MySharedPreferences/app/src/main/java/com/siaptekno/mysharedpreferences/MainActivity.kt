package com.siaptekno.mysharedpreferences

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.registerForActivityResult
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.siaptekno.mysharedpreferences.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var mUserPreference: UserPreference  // User preference instance

    private var isPreferenceEmpty = false  // Flag to check if preferences are empty
    private lateinit var userModel: UserModel  // User model instance

    private lateinit var binding: ActivityMainBinding  // View binding

    private val resultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result: ActivityResult ->
        // Handle the result from FormUserPreferenceActivity
        if (result.data != null && result.resultCode == FormUserPreferenceActivity.RESULT_CODE) {
            userModel = result.data?.getParcelableExtra<UserModel>(FormUserPreferenceActivity.EXTRA_RESULT) as UserModel
            populateView(userModel)  // Populate view with returned user data
            checkForm(userModel)  // Check the form state
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)  // Inflate the layout
        setContentView(binding.root)  // Set the content view

        // Adjust padding for system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
//        enableEdgeToEdge()  // Uncomment to enable edge-to-edge layout

        supportActionBar?.title = "My User Preference"  // Set action bar title

        mUserPreference = UserPreference(this)  // Create UserPreference instance

        showExistingPreference()  // Load existing preferences

        binding.btnSave.setOnClickListener(this)  // Set click listener for save button
    }

    private fun showExistingPreference() {
        userModel = mUserPreference.getUser()  // Retrieve user data from preferences
        populateView(userModel)  // Populate view with user data
        checkForm(userModel)  // Check the form state
    }

    private fun populateView(userModel: UserModel) {
        // Display user data in the corresponding TextViews
        binding.tvName.text = if (userModel.name.toString().isEmpty()) "Tidak Ada" else userModel.name
        binding.tvAge.text = if (userModel.age.toString().isEmpty()) "Tidak Ada" else userModel.age.toString()
        binding.tvIsLoveMu.text = if (userModel.isLove) "Ya" else "Tidak"
        binding.tvEmail.text = if (userModel.email.toString().isEmpty()) "Tidak Ada" else userModel.email
        binding.tvPhone.text = if (userModel.phoneNumber.toString().isEmpty()) "Tidak Ada" else userModel.phoneNumber
    }

    private fun checkForm(userModel: UserModel) {
        // Update button text based on whether preferences are empty
        when {
            userModel.name.toString().isNotEmpty() -> {
                binding.btnSave.text = getString(R.string.change)  // Change button text for editing
                isPreferenceEmpty = false  // Preferences are not empty
            }
            else -> {
                binding.btnSave.text = getString(R.string.save)  // Set button text for adding
                isPreferenceEmpty = true  // Preferences are empty
            }
        }
    }

    override fun onClick(view: View) {
        // Handle save button click
        if (view.id == R.id.btn_save) {
            val intent = Intent(this@MainActivity, FormUserPreferenceActivity::class.java)  // Create intent for FormUserPreferenceActivity
            when {
                isPreferenceEmpty -> {
                    intent.putExtra(
                        FormUserPreferenceActivity.EXTRA_TYPE_FORM,
                        FormUserPreferenceActivity.TYPE_ADD  // Set form type to add
                    )
                    intent.putExtra("USER", userModel)  // Pass user model
                }
                else -> {
                    intent.putExtra(
                        FormUserPreferenceActivity.EXTRA_TYPE_FORM,
                        FormUserPreferenceActivity.TYPE_EDIT  // Set form type to edit
                    )
                    intent.putExtra("USER", userModel)  // Pass user model
                }
            }
            resultLauncher.launch(intent)  // Launch the form activity
        }
    }
}

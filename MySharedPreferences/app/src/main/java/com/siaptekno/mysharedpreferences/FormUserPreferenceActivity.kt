package com.siaptekno.mysharedpreferences

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.siaptekno.mysharedpreferences.databinding.ActivityFormUserPreferenceBinding

class FormUserPreferenceActivity : AppCompatActivity(), View.OnClickListener {

    companion object {
        // Keys for Intent extras
        const val EXTRA_TYPE_FORM = "extra_type_form"
        const val EXTRA_RESULT = "extra_result"
        const val RESULT_CODE = 101

        // Form types
        const val TYPE_ADD = 1
        const val TYPE_EDIT = 2

        // Error messages
        private const val FIELD_REQUIRED = "Field tidak boleh kosong"
        private const val FIELD_DIGIT_ONLY = "Hanya boleh terisi numerik"
        private const val FIELD_IS_NOT_VALID = "Email tidak valid"
    }

    private lateinit var userModel: UserModel  // User model instance
    private lateinit var binding: ActivityFormUserPreferenceBinding  // View binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()  // Enables edge-to-edge layout
        binding = ActivityFormUserPreferenceBinding.inflate(layoutInflater)  // Inflate the layout
        setContentView(binding.root)  // Set the content view

        binding.btnSave.setOnClickListener(this)  // Set click listener for save button

        // Adjust padding for system bars
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.activity_form_user_preference)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Retrieve user data and form type from Intent
        userModel = intent.getParcelableExtra<UserModel>("USER") as UserModel
        val formType = intent.getIntExtra(EXTRA_TYPE_FORM, 0)

        // Set titles based on form type
        var actionBarTitle = ""
        var btnTitle = ""
        when (formType) {
            TYPE_ADD -> {
                actionBarTitle = "Tambah Baru"  // Title for adding user
                btnTitle = "Simpan"  // Button title for saving
            }
            TYPE_EDIT -> {
                actionBarTitle = "Ubah"  // Title for editing user
                btnTitle = "Update"  // Button title for updating
                showPreferenceInForm()  // Populate form with existing user data
            }
        }
        supportActionBar?.title = actionBarTitle  // Set action bar title
        supportActionBar?.setDisplayHomeAsUpEnabled(true)  // Enable back button in action bar
        binding.btnSave.text = btnTitle  // Set button title
    }

    override fun onClick(view: View) {
        // Handle save button click
        if (view.id == R.id.btn_save) {
            val name = binding.edtName.text.toString().trim()
            val email = binding.edtEmail.text.toString().trim()
            val age = binding.edtAge.text.toString().trim()
            val phoneNo = binding.edtPhone.text.toString().trim()
            val isLoveMU = binding.rgLoveMu.checkedRadioButtonId == R.id.rb_yes

            // Validate input fields
            if (name.isEmpty()) {
                binding.edtName.error = FIELD_REQUIRED
                return
            }
            if (email.isEmpty()) {
                binding.edtEmail.error = FIELD_REQUIRED
                return
            }
            if (!isValidEmail(email)) {
                binding.edtEmail.error = FIELD_IS_NOT_VALID
                return
            }
            if (age.isEmpty()) {
                binding.edtAge.error = FIELD_REQUIRED
                return
            }
            if (phoneNo.isEmpty()) {
                binding.edtPhone.error = FIELD_REQUIRED
                return
            }
            if (!TextUtils.isDigitsOnly(phoneNo)) {
                binding.edtPhone.error = FIELD_DIGIT_ONLY
                return
            }
            saveUser(name, email, age, phoneNo, isLoveMU)  // Save user data
            val resultIntent = Intent()
            resultIntent.putExtra(EXTRA_RESULT, userModel)  // Pass user model back
            setResult(RESULT_CODE, resultIntent)  // Set result code
            finish()  // Close the activity
        }
    }

    private fun saveUser(name: String, email: String, age: String, phoneNo: String, isLoveMU: Boolean) {
        val userPreference = UserPreference(this)  // Create UserPreference instance
        userModel.name = name  // Set user name
        userModel.email = email  // Set user email
        userModel.age = Integer.parseInt(age)  // Set user age
        userModel.phoneNumber = phoneNo  // Set user phone number
        userModel.isLove = isLoveMU  // Set if user loves Manchester United
        userPreference.setUser(userModel)  // Save user data in SharedPreferences
        Toast.makeText(this, "Data tersimpan", Toast.LENGTH_SHORT).show()  // Show success message
    }

    private fun isValidEmail(email: String): Boolean {
        // Validate email format
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    private fun showPreferenceInForm() {
        // Populate the form with existing user data
        binding.edtName.setText(userModel.name)
        binding.edtEmail.setText(userModel.email)
        binding.edtAge.setText(userModel.age.toString())
        binding.edtPhone.setText(userModel.phoneNumber)
        // Set radio button based on user's preference
        if (userModel.isLove) {
            binding.rbYes.isChecked = true
        } else {
            binding.rbNo.isChecked = true
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle back button in action bar
        if (item.itemId == android.R.id.home) {
            finish()  // Close the activity
        }
        return super.onOptionsItemSelected(item)  // Call super method
    }
}

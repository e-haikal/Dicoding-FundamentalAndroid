// MyPreferenceFragment.kt
// This fragment manages user preferences, allowing users to set their name, email, age, phone number, and a checkbox preference.

package com.siaptekno.mysettingpreference

import android.content.SharedPreferences
import android.os.Bundle
import androidx.preference.CheckBoxPreference
import androidx.preference.EditTextPreference
import androidx.preference.PreferenceFragmentCompat

class MyPreferenceFragment : PreferenceFragmentCompat(), SharedPreferences.OnSharedPreferenceChangeListener {
    // Declare keys for preferences
    private lateinit var NAME: String
    private lateinit var EMAIL: String
    private lateinit var AGE: String
    private lateinit var PHONE: String
    private lateinit var LOVE: String

    // Declare preference variables
    private lateinit var namePreference: EditTextPreference
    private lateinit var emailPreference: EditTextPreference
    private lateinit var agePreference: EditTextPreference
    private lateinit var phonePreference: EditTextPreference
    private lateinit var isLoveMuPreference: CheckBoxPreference

    // Default value for preferences
    companion object {
        private const val DEFAULT_VALUE = "Tidak Ada"
    }

    // Called to create the preferences from XML
    override fun onCreatePreferences(bundle: Bundle?, s: String?) {
        // Load preferences from the XML resource
        addPreferencesFromResource(R.xml.preferences)
        init() // Initialize preference keys and find preference objects
        setSummaries() // Set initial summaries for preferences
    }

    // Initialize preference keys and references
    private fun init() {
        // Get string resources for preference keys
        NAME = resources.getString(R.string.key_name)
        EMAIL = resources.getString(R.string.key_email)
        AGE = resources.getString(R.string.key_age)
        PHONE = resources.getString(R.string.key_phone)
        LOVE = resources.getString(R.string.key_love)

        // Safely find preferences and create new instances if null
        namePreference = findPreference<EditTextPreference>(NAME) ?: EditTextPreference(requireContext())
        emailPreference = findPreference<EditTextPreference>(EMAIL) ?: EditTextPreference(requireContext())
        agePreference = findPreference<EditTextPreference>(AGE) ?: EditTextPreference(requireContext())
        phonePreference = findPreference<EditTextPreference>(PHONE) ?: EditTextPreference(requireContext())
        isLoveMuPreference = findPreference<CheckBoxPreference>(LOVE) ?: CheckBoxPreference(requireContext())
    }

    // Register the listener when the fragment is resumed
    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences?.registerOnSharedPreferenceChangeListener(this)
    }

    // Unregister the listener when the fragment is paused
    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences?.unregisterOnSharedPreferenceChangeListener(this)
    }

    // Update preference summaries when changes occur
    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String?) {
        // Update the summary for each changed preference
        if (key == NAME) {
            namePreference.summary = sharedPreferences.getString(NAME, DEFAULT_VALUE)
        }
        if (key == EMAIL) {
            emailPreference.summary = sharedPreferences.getString(EMAIL, DEFAULT_VALUE)
        }
        if (key == AGE) {
            agePreference.summary = sharedPreferences.getString(AGE, DEFAULT_VALUE)
        }
        if (key == PHONE) {
            phonePreference.summary = sharedPreferences.getString(PHONE, DEFAULT_VALUE)
        }
        if (key == LOVE) {
            isLoveMuPreference.isChecked = sharedPreferences.getBoolean(LOVE, false)
        }
    }

    // Set initial summaries for each preference
    private fun setSummaries() {
        val sh = preferenceManager.sharedPreferences
        namePreference.summary = sh?.getString(NAME, DEFAULT_VALUE) ?: DEFAULT_VALUE
        emailPreference.summary = sh?.getString(EMAIL, DEFAULT_VALUE) ?: DEFAULT_VALUE
        agePreference.summary = sh?.getString(AGE, DEFAULT_VALUE) ?: DEFAULT_VALUE
        phonePreference.summary = sh?.getString(PHONE, DEFAULT_VALUE) ?: DEFAULT_VALUE
        isLoveMuPreference.isChecked = sh?.getBoolean(LOVE, false) ?: false
    }
}

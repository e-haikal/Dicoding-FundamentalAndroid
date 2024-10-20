package com.siaptekno.mysharedpreferences

import android.content.Context

internal class UserPreference(context: Context) {
    companion object {
        private const val PREFS_NAME = "user_pref"  // SharedPreferences file name
        private const val NAME = "name"  // Key for user name
        private const val EMAIL = "email"  // Key for user email
        private const val AGE = "age"  // Key for user age
        private const val PHONE_NUMBER = "phone"  // Key for user phone number
        private const val LOVE_MU = "islove"  // Key for user's preference about Manchester United
    }

    private val preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)  // SharedPreferences instance

    fun setUser(value: UserModel) {
        val editor = preferences.edit()  // Editor for modifying preferences
        editor.putString(NAME, value.name)  // Save user name
        editor.putString(EMAIL, value.email)  // Save user email
        editor.putInt(AGE, value.age)  // Save user age
        editor.putString(PHONE_NUMBER, value.phoneNumber)  // Save user phone number
        editor.putBoolean(LOVE_MU, value.isLove)  // Save user's preference
        editor.apply()  // Commit changes
    }

    fun getUser(): UserModel {
        val model = UserModel()  // Create a new UserModel instance
        // Retrieve user data from preferences
        model.name = preferences.getString(NAME, "")
        model.email = preferences.getString(EMAIL, "")
        model.age = preferences.getInt(AGE, 0)
        model.phoneNumber = preferences.getString(PHONE_NUMBER, "")
        model.isLove = preferences.getBoolean(LOVE_MU, false)
        return model  // Return populated UserModel
    }
}

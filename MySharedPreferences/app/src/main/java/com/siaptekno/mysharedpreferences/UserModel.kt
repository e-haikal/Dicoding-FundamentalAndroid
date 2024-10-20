package com.siaptekno.mysharedpreferences

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserModel (
    var name: String? = null,  // User's name
    var email: String? = null,  // User's email
    var age: Int = 0,  // User's age
    var phoneNumber: String? = null,  // User's phone number
    var isLove: Boolean = false  // User's preference about Manchester United
): Parcelable  // Parcelable implementation for passing between activities

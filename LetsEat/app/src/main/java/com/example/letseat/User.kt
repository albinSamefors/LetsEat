package com.example.letseat

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(
    val email: String? = null,
    val password: String? = null)
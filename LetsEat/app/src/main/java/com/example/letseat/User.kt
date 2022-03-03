package com.example.letseat

data class User(val email: String, val password: String) {
    constructor(): this("", "")
}
package com.example.letseat

data class restaurantItem(
    val id : Int,
    val restaurantName : String,
    val restaurantType : String,
    val rating : Float,
    val distance : Float)
        {
    override fun toString() = restaurantName
    }

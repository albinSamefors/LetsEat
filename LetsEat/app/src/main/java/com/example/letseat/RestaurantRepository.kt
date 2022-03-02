package com.example.letseat

import android.location.Location
import com.google.android.gms.common.api.internal.BackgroundDetector.initialize
import com.google.android.gms.common.api.internal.GoogleServices.initialize



//Placeholder class for creating restaurant items

val restaurantRepository = RestaurantRepository().apply {
    addRestaurant("Beirut","Saviour of students", 5.0f, 1.0f)
    addRestaurant("Donken", "Munchies",5.0f,1.0f)

}

class RestaurantRepository {
    private val restaurants = mutableListOf<RestaurantItem>()

    fun addRestaurant(restaurantName : String, restaurantType : String, rating : Float, distanceFromUser : Float) : Int
    {
        val id = when{
            restaurants.count() == 0 -> 1
            else -> restaurants.last().id+1
        }
        restaurants.add(RestaurantItem(id,restaurantName,restaurantType,rating,distanceFromUser))
        return id
    }
    fun getAllRestaurants() = restaurants

    fun getItemCount() : Int
    {
        return restaurants.size

    }
    fun updateRestaurantList(location: Location, radius : Float)
    {

    }






}

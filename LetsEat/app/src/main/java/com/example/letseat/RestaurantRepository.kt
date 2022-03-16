package com.example.letseat

import android.content.Context
import android.location.Location
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.common.api.internal.BackgroundDetector.initialize
import com.google.android.gms.common.api.internal.GoogleServices.initialize
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.PlacesClient
import kotlinx.coroutines.GlobalScope


//Placeholder class for creating restaurant items

val restaurantRepository = RestaurantRepository()

class RestaurantRepository {
	private val restaurants = mutableListOf<RestaurantItem>()
	private lateinit var context: Context
	fun setContext(mContext: Context) {
		context = mContext
	}

	fun addRestaurant(placeID: String,name : String, latLng: LatLng, rating : Float,adress : String): Int {

		val id = when {
			restaurants.count() == 0 -> 1
			else -> restaurants.last().id + 1
		}
		restaurants.add(
			RestaurantItem(
				id,
				name,
				rating,
				latLng,
				adress,
				"Dont know"//ÄNDRA DENNA
			)
		)


		return id
	}
	fun addRestaurant(placeID: String,name : String, latLng: LatLng, rating : Float,adress : String, openingHours : Boolean): Int {

		val id = when {
			restaurants.count() == 0 -> 1
			else -> restaurants.last().id + 1
		}
		var openString = ""
		if(openingHours)
		{
			openString = "Open" //ÄNDRA
		}
		else{
			openString = "Closed"
		}
		restaurants.add(
			RestaurantItem(
				id,
				name,
				rating,
				latLng,
				adress,
				openString

			)
		)


		return id
	}

	fun getAllRestaurants() = restaurants

	fun getItemCount(): Int {
		return restaurants.size

	}
	fun addRestaurantsOnScreen() : RestaurantListAdapter
	{
		val restaurantListAdapter: RestaurantListAdapter = RestaurantListAdapter(
			context, R.layout.restaurant_item,
			restaurantRepository.getAllRestaurants()
		)

	return restaurantListAdapter
	}
	fun dropAllRestaurants()
	{
		restaurantRepository.restaurants.clear()
	}
	fun getSpecificRestaurant(id : Int) : RestaurantItem
	{
		var tempId = -1
		for(restaurant in restaurantRepository.restaurants)
		{
			if((restaurant.id) == id+1)
			{
				tempId = id
			}
		}
		return restaurantRepository.restaurants[tempId]
	}
	fun drop(id : Int)
	{
		restaurantRepository.restaurants.removeAt(id)
	}
	 fun cutOff(latLng: LatLng, radius : Int)
	{
		for(restaurants in restaurantRepository.restaurants)
		{
			if(distanceFromUser(latLng, restaurants.latLng) > radius)
			{
				restaurantRepository.drop(restaurants.id)
			}
		}
	}
	fun distanceFromUser(user : LatLng, restaurant : LatLng) : Int
	{
		val earthRadius = (6371 * 1000)
		val userLat = user.latitude * (Math.PI/180)
		val restaurantLat = restaurant.latitude * (Math.PI/180)
		val latDiff = (restaurant.latitude-user.latitude) * (Math.PI/180)
		val longDiff= (restaurant.longitude-user.longitude) * (Math.PI/180)

		val a = Math.sin(latDiff/2) * Math.sin(latDiff/2)+
				Math.cos(userLat) * Math.cos(restaurantLat) *
				Math.sin(longDiff/2) * Math.sin(longDiff/2)
		val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a))
		val d = earthRadius * c
		return d.toInt()
	}





}
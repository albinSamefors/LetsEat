package com.example.letseat

import android.content.Context
import android.location.Location
import com.google.android.gms.maps.model.LatLng


//Placeholder class for creating restaurant items

val restaurantRepository = RestaurantRepository()
val favoriteRestaurantRepository = RestaurantRepository()

class RestaurantRepository {
	private lateinit var user: LatLng
	private val restaurants = mutableListOf<RestaurantItem>()
	private lateinit var context: Context
	fun setContext(mContext: Context) {
		context = mContext
	}

	fun addRestaurant(name : String, latLng: LatLng, rating : Float,adress : String): Int {

		val id = when {
			restaurants.count() == 0 -> 0
			else -> restaurants.last().id + 1
		}
		restaurants.add(
			RestaurantItem(
				id,
				name,
				rating,
				latLng,
				adress,
				"Dont know"//ÄNDRA DENNA// TODO: ASS
			)
		)


		return id
	}
	fun addRestaurant(name : String, latLng: LatLng, rating : Float,adress : String, openingHours : Boolean): Int {

		val id = when {
			restaurants.count() == 0 -> 0
			else -> restaurants.last().id + 1
		}
		var openString = ""
		if(openingHours)
		{
			openString = "Open" //ÄNDRA// TODO: ASS
		}
		else{
			openString = "Closed"// TODO: ASS
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
	fun addRestaurant(restaurantItem: RestaurantItem): Int {

		val id = when {
			restaurants.count() == 0 -> 0
			else -> restaurants.last().id + 1
		}
		restaurants.add(restaurantItem)


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
			restaurantRepository.getAllRestaurants(),
			user
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
		for(restaurant in restaurants)
		{
			if((restaurant.id) == id)
			{
				tempId = id
			}
		}
		return restaurants[tempId]
	}
	fun drop(id : Int)
	{
		restaurantRepository.restaurants[getSpecificRestaurant(id).id]
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
	fun setUserLocation(latLng: LatLng)
	{
		user = latLng
	}
	fun sortAfterRating()
	{
		restaurants.sortByDescending { it.rating }
		var i = 0
		for(restaurant in restaurants){
			restaurant.id = i
			i++
		}
	}
	fun sortAfterDistacne()
	{
		var userLoc = Location("userPos")
		var restaurantLoc = Location("restPos")
		userLoc.latitude = user.latitude
		userLoc.longitude = user.longitude
		restaurants.sortBy {
			restaurantLoc.latitude = it.latLng.latitude
			restaurantLoc.longitude = it.latLng.longitude
				userLoc.distanceTo(restaurantLoc)



		}
		var i = 0
		for(restaurant in restaurants){
			restaurant.id = i
			i++
		}


	}






}
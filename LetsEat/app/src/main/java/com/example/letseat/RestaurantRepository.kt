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

	fun addRestaurant(placeID: String,name : String, latLng: LatLng, rating : Float): Int {

		val id = when {
			restaurants.count() == 0 -> 1
			else -> restaurants.last().id + 1
		}
		restaurants.add(
			RestaurantItem(
				id,
				name,
				rating,
				latLng
			)
		)
		addRestaurantsOnScreen()


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





}
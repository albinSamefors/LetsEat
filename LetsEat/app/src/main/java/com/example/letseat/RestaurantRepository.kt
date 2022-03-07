package com.example.letseat

import android.content.Context
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.common.api.internal.BackgroundDetector.initialize
import com.google.android.gms.common.api.internal.GoogleServices.initialize
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import com.google.android.libraries.places.api.net.PlacesClient


//Placeholder class for creating restaurant items

val restaurantRepository = RestaurantRepository()

class RestaurantRepository {
	private val restaurants = mutableListOf<RestaurantItem>()
	private lateinit var context: Context
	fun setContext(mContext: Context) {
		context = mContext
	}

	fun addRestaurant(placeID: String): Int {
		val id = when {
			restaurants.count() == 0 -> 1
			else -> restaurants.last().id + 1
		}

		var client = Places.createClient(context)
		var placeFields = mutableListOf<Place.Field>()
		placeFields.add(Place.Field.LAT_LNG)
		placeFields.add(Place.Field.RATING)
		placeFields.add(Place.Field.NAME)
		var fetchPlaceRequest = FetchPlaceRequest.builder(placeID, placeFields).build()
		var response = client.fetchPlace(fetchPlaceRequest)

		restaurants.add(
			RestaurantItem(
				id,
				response.result.place.name,
				response.result.place.rating.toFloat(),
				response.result.place.latLng
			)
		)
		return id
	}

	fun getAllRestaurants() = restaurants

	fun getItemCount(): Int {
		return restaurants.size

	}

	fun updateRestaurantList(location: Location, radius: Float) {

	}


}

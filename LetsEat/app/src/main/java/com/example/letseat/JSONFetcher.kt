package com.example.letseat

import android.location.Location
import android.os.Handler
import android.os.Looper
import com.google.android.gms.maps.model.LatLng
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL


class JSONFetcher(url: String, userLatLng: LatLng, radius : Int){
	private var mUrl = url
	var jsonString = ""
	var mUserLatLng = userLatLng
	var mRadius = radius
	private var idList = ArrayList<String>()



	 fun run(callback: () -> Unit) {

		val mainHandler = Handler(Looper.getMainLooper())

		Thread(Runnable {


			//Uses the url to establish connection
			val url = URL(mUrl)
			val httpURLConnection = url.openConnection() as HttpURLConnection
			val inputStream = httpURLConnection.inputStream
			val bufferedReader = inputStream.bufferedReader()

			idList.clear()
			for(line in bufferedReader.lines())
			{
				jsonString += line
			}
			if (jsonString.isNotEmpty()) {
				val jsonObject = JSONObject(jsonString)
				val jsonArray = jsonObject.getJSONArray("results")
				for(i in 0 until jsonArray.length())
				{
					//Places all restaurant objects into restaurant repository list
					var exists = false;
					val place = jsonArray.getJSONObject(i)
					val location = place.getJSONObject("geometry").getJSONObject("location")
					val lat = location.getString("lat").toDouble()
					val lng = location.getString("lng").toDouble()
					val latLng = LatLng(lat,lng)

					var rating = 0f
					if(place.has("rating"))
					{
						rating = place.getString("rating").toFloat()
					}

					var name = place.getString("name")
					for(restaurant in restaurantRepository.getAllRestaurants())
					{
						if(name == restaurant.restaurantName)
						{
							exists = true
						}
					}
					var address = ""
					//Certain controlls for less common fields
					if(place.has("vicinity"))
					{
						 address = place.getString("vicinity")
					}
					else{
						address = ""
					}
					var openNow = "";
					if(place.has("opening_hours"))
					{
						openNow= place.getJSONObject("opening_hours").getString("open_now")
					}
					else{
						if(!exists) {
							restaurantRepository.addRestaurant( name, latLng, rating, address)
						}
					}
					var bOpenNow = false
					val userLocation = Location("userLocation")
					userLocation.latitude = mUserLatLng.latitude
					userLocation.longitude = mUserLatLng.longitude
					val restaurantLocation = Location("restaurantLocation")
					restaurantLocation.latitude = latLng.latitude
					restaurantLocation.longitude = latLng.longitude

					if(userLocation.distanceTo(restaurantLocation) <= mRadius) {
						if(openNow == "true" || openNow == "false") {
							if(openNow == "true") {
								if(!exists) {
									restaurantRepository.addRestaurant(
										name,
										latLng,
										rating,
										address,
										true
									)
								}
								else continue
							}
							else{
								if(!exists) {
									restaurantRepository.addRestaurant(

										name,
										latLng,
										rating,
										address,
										false
									)
								}
								else
								{
									continue
								}
							}
						}
						else {
							if(!exists) {
							restaurantRepository.addRestaurant(name,latLng,rating,address)
							}
							else
							{
								continue
							}
						}

					}
					else {
						continue
					}
				}
			}
			mainHandler.post{
				callback()
			}

		}).start()

	}


}
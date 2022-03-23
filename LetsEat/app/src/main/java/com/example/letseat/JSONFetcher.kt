package com.example.letseat

import android.location.Location
import android.os.Handler
import android.os.Looper
import com.google.android.gms.maps.model.LatLng
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL


class JSONFetcher(mUrl: String, userLatLng: LatLng, radius : Int){
	var sUrl = mUrl
	var data = ""
	var mUserLatLng = userLatLng
	var mRadius = radius
	private var idList = ArrayList<String>()



	 fun run(callback: () -> Unit) {

		//GlobalScope.launch(Dispatchers.IO)
		val mainHandler = Handler(Looper.getMainLooper())

		Thread(Runnable {



			var url = URL(sUrl)
			var httpURLConnection = url.openConnection() as HttpURLConnection
			var inputStream = httpURLConnection.inputStream
			var bufferedReader = inputStream.bufferedReader()
			var temp : String

			idList.clear()
			for(line in bufferedReader.lines())
			{
				data = data + line
			}
			if (data.isNotEmpty()) {
				var jsonObject = JSONObject(data)
				var jsonArray = jsonObject.getJSONArray("results")
				for(i in 0 until jsonArray.length())
				{
					var exists = false;
					val place = jsonArray.getJSONObject(i)
					var id = place.getString("place_id")
					var location = place.getJSONObject("geometry").getJSONObject("location")
					var lat = location.getString("lat").toDouble()
					var lng = location.getString("lng").toDouble()
					var latLng = LatLng(lat,lng)

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
					var adress = ""
					if(place.has("formatted_address"))
					{
						 adress = place.getString("formatted_address")
					}
					else{
						adress = ""
					}
					var openNow = "";
					if(place.has("opening_hours"))
					{
						openNow= place.getJSONObject("opening_hours").getString("open_now")
					}
					else{
						if(!exists) {
							restaurantRepository.addRestaurant( name, latLng, rating, adress)
						}
					}

					var bOpenNow = false

					var userLocation = Location("userLocation")
					userLocation.latitude = mUserLatLng.latitude
					userLocation.longitude = mUserLatLng.longitude
					var restaurantLocation = Location("restaurantLocation")
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
										adress,
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
										adress,
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
							restaurantRepository.addRestaurant(name,latLng,rating,adress)
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
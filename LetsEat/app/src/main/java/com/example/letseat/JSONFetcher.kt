package com.example.letseat

import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import java.util.logging.Handler

class JSONFetcher(mUrl: String)	:	Thread() {
	var sUrl = mUrl
	var data = ""
	private var idList = ArrayList<String>()
	var isDone = false;

	private fun addToRestaurantList()
	{

	}
	override fun run() {

		//GlobalScope.launch(Dispatchers.IO)
		Thread(Runnable {

			isDone = false
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
					val place = jsonArray.getJSONObject(i)
					var id = place.getString("place_id")
					var location = place.getJSONObject("geometry").getJSONObject("location")
					var lat = location.getString("lat").toDouble()
					var lng = location.getString("lng").toDouble()
					var latLng = LatLng(lat,lng)

					var rating = place.getString("rating").toFloat()
					var name = place.getString("name")
					restaurantRepository.addRestaurant(id,name,latLng,rating)
					restaurantRepository.addRestaurantsOnScreen()
				}

			}

		}).start()

	}

}
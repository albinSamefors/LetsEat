package com.example.letseat

import android.os.Handler
import android.os.Looper
import com.google.android.gms.maps.model.LatLng
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL


class JSONFetcher(mUrl: String){
	var sUrl = mUrl
	var data = ""
	private var idList = ArrayList<String>()
	var isDone = false;


	 fun run(callback: () -> Unit) {

		//GlobalScope.launch(Dispatchers.IO)
		val mainHandler = Handler(Looper.getMainLooper())

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
					var adress = place.getString("formatted_address")
					var openNow = "";
					if(place.has("opening_hours"))
					{
						openNow= place.getJSONObject("opening_hours").getString("open_now")
					}
					else{
						restaurantRepository.addRestaurant(id,name,latLng,rating,adress)
					}

					var bOpenNow = false
					if(openNow == "true")
					{
						bOpenNow = true
					}
					else if(openNow == "false")
					{
						bOpenNow = false
					}
					else
					{
						restaurantRepository.addRestaurant(id,name,latLng,rating,adress)
					}
					restaurantRepository.addRestaurant(id,name,latLng,rating,adress,bOpenNow)
				}


			}


			mainHandler.post{
				callback()
			}

		}).start()

	}

}
package com.example.letseat

import android.content.Context
import android.os.AsyncTask
import com.google.android.gms.common.api.GoogleApi
import com.google.android.gms.common.api.GoogleApiActivity
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.URL
import java.nio.channels.AsynchronousByteChannel
import kotlin.collections.HashMap


class MapInformationGetter() : AsyncTask<String, Int, String>() {

	@Throws(IOException::class)
	fun downloadUrl(string: String): String {
		val download = URL(string)
		val connection = download.openConnection() as HttpURLConnection
		connection.connect()
		val stream = connection.inputStream
		val inputStreamReader = InputStreamReader(stream)
		val reader = BufferedReader(inputStreamReader)
		val builder = StringBuilder()
		val line = ""
		while (reader.readLine() != null) {
			builder.append(line)

		}

		var data = builder.toString();

		reader.close()

		return data


	}

	override fun doInBackground(vararg string: String?): String {

		var data = ""
		try {
			data = downloadUrl(string[0]!!)
		} catch (e: IOException) {
			e.printStackTrace()
		}
		return data


	}

	override fun onPostExecute(result: String?) {
		var parserTask = ParserTask()
		parserTask.execute(result)
	}


}

class ParserTask() : AsyncTask<String, Int, List<HashMap<String, String>>>() {


	override fun doInBackground(vararg string: String?): MutableList<HashMap<String, String>> {
		var jsonParser = com.example.letseat.JsonParser()
		var mapList = mutableListOf<HashMap<String, String>>()
		var jsonObject: JSONObject
		try {
			jsonObject = JSONObject(string[0])
			mapList = jsonParser.parseResult(jsonObject)
		} catch (e: JSONException) {
			e.printStackTrace()
		}
		return mapList
	}

	override fun onPostExecute(result: List<HashMap<String, String>>?) {
		for (i in result!!) {
			val ooh = i.get("id")
			restaurantRepository.addRestaurant(i.get("id")!!)

		}
	}


}

class JsonParser {
	private fun parseJsonObject(jsonData: JSONObject): HashMap<String, String> {

		val dataList = HashMap<String, String>()
		try {


			val name = jsonData.getString("name")
			val id = jsonData.getString("rating")
			val openingHours = jsonData.getString("opening_hours")
			val type = jsonData.getString("type")
			val latitude =
				jsonData.getJSONObject("geometry").getJSONObject("location").getString("lat")
			val longitude =
				jsonData.getJSONObject("geometry").getJSONObject("location").getString("lng")
			dataList.put("name", name)
			dataList.put("id", id)
			dataList.put("opening_hours", openingHours)
			dataList.put("type", type)
			dataList.put("lat", latitude)
			dataList.put("lng", longitude)
		} catch (e: JSONException) {
			e.printStackTrace()
		}

		return dataList
	}

	private fun parseJsonArray(jsonArray: JSONArray): MutableList<HashMap<String, String>> {
		var length = jsonArray.length()
		var dataList = mutableListOf<HashMap<String, String>>()


		for (i in jsonArray.length() downTo 0) {
			try {
				var data = parseJsonObject(jsonArray.getJSONObject(i))
				dataList.add(data)
			} catch (e: JSONException) {
				e.printStackTrace()
			}
		}
		return dataList
	}

	fun parseResult(jsonObject: JSONObject): MutableList<HashMap<String, String>> {
		lateinit var jsonArray: JSONArray
		try {
			jsonArray = jsonObject.getJSONArray("results")
		} catch (e: JSONException) {
			e.printStackTrace()
		}
		return parseJsonArray(jsonArray)
	}
}

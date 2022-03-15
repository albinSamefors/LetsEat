package com.example.letseat

import android.content.Context
import android.os.AsyncTask
import android.util.JsonReader
import com.google.android.gms.common.api.GoogleApi
import com.google.android.gms.common.api.GoogleApiActivity
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.UrlTileProvider
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.gson.JsonObject
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedInputStream
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.lang.StringBuilder
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.ProtocolException
import java.net.URL
import java.nio.channels.AsynchronousByteChannel
import kotlin.collections.HashMap


class MapInformationGetter(url : String) {
	 private lateinit var url: String
	public fun MapInformationGetter(mUrl : String)
	{
	var url = mUrl
	}

	fun httpServiceCall() : String
	{
		var result = ""
		try {
			var url = URL(url)
			var connection = url.openConnection() as HttpURLConnection
			connection.requestMethod = "GET"

			var inputStream = BufferedInputStream(connection.inputStream)
			result = convertResultToString(inputStream)
		}catch (e : MalformedURLException){
			e.printStackTrace()
		}catch (e : IOException){
			e.printStackTrace()
		}catch (e : ProtocolException){
			e.printStackTrace()
		}
		return result
	}

	private fun convertResultToString(inputStream: BufferedInputStream): String {
		var bufferedReader = BufferedReader(InputStreamReader(inputStream))
		var stringBuilder = StringBuilder()
		while (true)
		{
			try {
			    if (bufferedReader.readLine() != null)
				{
			    	stringBuilder.append(bufferedReader.readLine())
				}
			}catch (e : IOException){
				e.printStackTrace()
			}finally {
				try {
					inputStream.close()
				}catch (e : IOException)
				{
					e.printStackTrace()
				}

			}
		}
		return stringBuilder.toString()
	}

}
private class IDFetcher(mUrl : String) : AsyncTask<Void,Void,Void>()
{
	private lateinit var url : String
	fun IDFetcher(mUrl: String)
	{
		url = mUrl
	}
	override fun doInBackground(vararg voids: Void?): Void {
		val getter = MapInformationGetter(url)
		var jsonString = getter.httpServiceCall()
		if (jsonString != null) {
			var jsonObject = JsonObject().getAsJsonObject(jsonString)
			var ids = jsonObject.getAsJsonArray("candidates")
			for(id in ids)
			{
				id.
			}
		}
	}
}

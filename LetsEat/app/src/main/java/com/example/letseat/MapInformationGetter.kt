package com.example.letseat

import android.annotation.SuppressLint
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.lang.StringBuilder
import java.net.URL
import kotlin.collections.HashMap


class MapInformationGetter() : AsyncTask<String, Int, String>() {
    private lateinit var placesClient : PlacesClient
    private lateinit var url : String

    fun createRestaurantURL(radius : Int, location : LatLng, apiKey : String)
    {
        url = "https://maps.googleapis.com/maps/api/place/findplacefromtext/json?" +
                "fields=place_id%2Cname%2Crating%2Copening_hours%2Ctype%2Clocation"+
                "&locationbias=circle:%3A"+radius +"@"+location.latitude +","+ location.longitude +
                "&type=restaurant"+ "&key=" + apiKey
    }

   private fun downloadUrl() : String
    {
        var download = URL(url)
        var connection = download.openConnection()
        connection.connect()
        var stream = connection.getInputStream()
        var reader = BufferedReader(InputStreamReader(stream))
        var builder = StringBuilder()
        var line = ""
        while( reader.readLine() != null)
        {
            builder.append(line)
        }

        var data = builder.toString();

        reader.close()

        return data


    }



    override fun doInBackground(vararg string: String?): String {

            var data  = ""
        try {
                data = downloadUrl()
        }catch (e: IOException)
        {
            e.printStackTrace()
        }
        return data


    }


}
class ParserTask : AsyncTask<String,Int,List<HashMap<String,String>>>()
{
    override fun doInBackground(vararg string: String?): MutableList<HashMap<String, String>> {
        var jsonParser = com.example.letseat.JsonParser()
        var mapList = mutableListOf<HashMap<String,String>>()
        var jsonObject :JSONObject
        try {
            jsonObject = JSONObject(string[0])
            mapList = jsonParser.parseResult(jsonObject)
        }catch (e: JSONException){
            e.printStackTrace()
        }
        return mapList
    }

    override fun onPostExecute(result: List<HashMap<String, String>>) {

        for(i in result)
        {
            restaurantRepository.addRestaurant(i.get("name").toString(),i.get("type").toString(),i.get("rating")!!.toFloat(),
                LatLng(i.get("lat")!!.toDouble(),i.get("lng")!!.toDouble())
            )
        }
    }

}
class JsonParser{
    private fun parseJsonObject(jsonData : JSONObject) : HashMap<String,String>
    {

        val dataList =  HashMap<String,String>()
        try {


            val name = jsonData.getString("name")
            val id = jsonData.getString("rating")
            val openingHours = jsonData.getString("opening_hours")
            val type = jsonData.getString("type")
            val latitude = jsonData.getJSONObject("geometry").getJSONObject("location").getString("lat")
            val longitude = jsonData.getJSONObject("geometry").getJSONObject("location").getString("lng")
            dataList.put("name", name)
            dataList.put("id", id)
            dataList.put("opening_hours", openingHours)
            dataList.put("type", type)
            dataList.put("lat", latitude)
            dataList.put("lng", longitude)
        }catch ( e: JSONException)
        {
            e.printStackTrace()
        }

        return dataList
    }
    private fun parseJsonArray(jsonArray: JSONArray) : MutableList<HashMap<String,String>>
    {
        var length = jsonArray.length()
        var dataList = mutableListOf<HashMap<String,String>>()


        for(i in jsonArray.length() downTo 0)
        {
            try {
                var data = parseJsonObject(jsonArray.getJSONObject(i))
                dataList.add(data)
            }catch (e : JSONException)
            {
                e.printStackTrace()
            }
        }
        return dataList
    }
    fun parseResult(jsonObject: JSONObject) : MutableList<HashMap<String,String>>{
       lateinit var jsonArray : JSONArray
        try {
            jsonArray = jsonObject.getJSONArray("results")
        }catch (e: JSONException)
        {
            e.printStackTrace()
        }
        return parseJsonArray(jsonArray)
    }
}

package com.example.letseat

import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.net.PlacesClient
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.StringBuilder
import java.net.URL
import kotlin.collections.HashMap


class MapInformationGetter()  {
    private lateinit var placesClient : PlacesClient
    private lateinit var url : String

    fun createRestaurantURL(radius : Int, location : LatLng, apiKey : String)
    {
        url = "https://maps.googleapis.com/maps/api/place/findplacefromtext/json?" +
                "fields=place_id%2Cname%2Crating%2Copening_hours%2Ctype%2Clocation"+
                "&locationbias=circle:%3A"+radius +"@"+location.latitude +","+ location.longitude +
                "&type=restaurant"+ "&key=" + apiKey
    }
    fun get() : String{
       val jsonObject = JSONObject(downloadUrl())
        parser(jsonObject)
        return downloadUrl()
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
    private fun parser(jsonData : JSONObject) : HashMap<String,String>
    {

        val dataList =  HashMap<String,String>()
        val name = jsonData.getString("name")
        val id = jsonData.getString("rating")
        val openingHours = jsonData.getString("opening_hours")
        val type = jsonData.getString("type")
        val location = jsonData.getString("location")
        dataList.put("name",name)
        dataList.put("id",id)
        dataList.put("opening_hours",openingHours)
        dataList.put("type",type)

        return dataList
    }


}
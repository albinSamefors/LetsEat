package com.example.letseat

import android.content.Context
import android.location.Location
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.RatingBar
import android.widget.TextView
import androidx.core.view.isVisible
import com.google.android.gms.maps.model.LatLng
import kotlin.math.nextDown

class RestaurantListAdapter(context: Context, resource: Int, objects: MutableList<RestaurantItem>, userLatLng: LatLng) :
	ArrayAdapter<RestaurantItem>(context, resource, objects) {

	private val mContext = context
	private val mResource = resource
	private val user = userLatLng

	override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
		val name: String = getItem(position)!!.restaurantName
		val rating: Float = getItem(position)!!.rating
		val distance: LatLng = getItem(position)!!.latLng

		val inflater: LayoutInflater = LayoutInflater.from(mContext)
		val convertView = inflater.inflate(mResource, parent, false)


		val titleView: TextView = convertView.findViewById(R.id.restaurantName)
		val ratingView: RatingBar = convertView.findViewById(R.id.ratingBar)
		val distanceView: TextView = convertView.findViewById(R.id.distanceFromUser)
		val openView = convertView.findViewById<TextView>(R.id.restaurantOpen)
		var floatArray = floatArrayOf(0f)
		val userLoc = Location("locationA")
		val restaurantLocation = Location("locationB")
		userLoc.latitude = user.latitude
		userLoc.longitude = user.longitude
		restaurantLocation.latitude = getItem(position)!!.latLng.latitude
		restaurantLocation.longitude = getItem(position)!!.latLng.longitude

		val fDistanceToRestaurant = userLoc.distanceTo(restaurantLocation)
		titleView.text = name

		if(rating ==  0.0f)
		{
			ratingView.isVisible = false
		}
		else{
			ratingView.isVisible = true
			ratingView.rating = rating
		}
		if(getItem(position)!!.openNow != "Open"&&getItem(position)!!.openNow != "Closed"&&getItem(position)!!.openNow != "Dont know")
		{
			openView.text = "Dont know"
		}
		else{
			openView.text = getItem(position)!!.openNow
		}

		distanceView.text = fDistanceToRestaurant.toInt().toString() + "m"

		return convertView
	}
}
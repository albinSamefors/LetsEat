package com.example.letseat

import android.content.Context
import android.location.Location
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.RatingBar
import android.widget.TextView
import com.google.android.gms.maps.model.LatLng
import kotlin.math.nextDown

class RestaurantListAdapter(context: Context, resource: Int, objects: MutableList<RestaurantItem>, userLatLng: LatLng) :
	ArrayAdapter<RestaurantItem>(context, resource, objects) {
	private val mContext = context
	private val mResource = resource
	private val user = userLatLng

	override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
		val name: String = getItem(position)!!.restaurantName
		//  val type : String = getItem(position)!!.restaurantType
		val rating: Float = getItem(position)!!.rating
		val distance: LatLng = getItem(position)!!.latLng


		val inflater: LayoutInflater = LayoutInflater.from(mContext)
		val convertView = inflater.inflate(mResource, parent, false)


		val titleView: TextView = convertView.findViewById(R.id.restaurantName)
		// val descriptionView : TextView = convertView.findViewById(R.id.restaurantType)
		val ratingView: RatingBar = convertView.findViewById(R.id.ratingBar)
		val distanceView: TextView = convertView.findViewById(R.id.distanceFromUser)
		var floatArray = floatArrayOf(0f)
		var userLoc = Location("locationA")
		var restaurantLocation = Location("locationB")
		userLoc.latitude = user.latitude
		userLoc.longitude = user.longitude
		restaurantLocation.latitude = getItem(position)!!.latLng.latitude
		restaurantLocation.longitude = getItem(position)!!.latLng.longitude

		var fDistanceToRestaurant = userLoc.distanceTo(restaurantLocation)
		titleView.text = name
		// descriptionView.text = type
		ratingView.rating = rating
		distanceView.text = fDistanceToRestaurant.toInt().toString() + "m"

		return convertView
	}
}
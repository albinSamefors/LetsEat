package com.example.letseat

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.RatingBar
import android.widget.TextView
import com.google.android.gms.maps.model.LatLng

class RestaurantListAdapter(context: Context, resource: Int, objects: MutableList<RestaurantItem>) :
	ArrayAdapter<RestaurantItem>(context, resource, objects) {
	private val mContext = context
	private val mResource = resource


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

		titleView.text = name
		// descriptionView.text = type
		ratingView.rating = rating
		// distanceView.text = distance.toString() + " km"

		return convertView
	}
}
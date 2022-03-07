package com.example.letseat

import com.google.android.gms.maps.model.LatLng

data class RestaurantItem(
	val id: Int,
	val restaurantName: String,
	val rating: Float,
	val latLng: LatLng
) {
	override fun toString() = restaurantName
}

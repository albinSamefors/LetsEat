package com.example.letseat

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageButton
import android.widget.Toast
import android.widget.ToggleButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RestaurantActivity : AppCompatActivity() {
	private lateinit var firebaseAuth : FirebaseAuth
	//retrieve restaurant id
	private val restaurantId = intent.getStringExtra("id")!!

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_restaurant)

		//init firebase auth
		firebaseAuth = FirebaseAuth.getInstance()

		val favoriteButton = findViewById<ImageButton>(R.id.favoritButton)
		favoriteButton.setOnClickListener {
			if (firebaseAuth.currentUser == null){
				//user not logged in, can not use favorite function
				Toast.makeText(this, "You are not logged in", Toast.LENGTH_SHORT).show()
			}else{
				//user logged in, can use function
			}
		}


		val homeViewButton = findViewById<ImageButton>(R.id.backToListViewButton)
		homeViewButton.setOnClickListener {
			val intent = Intent(this, MainActivity::class.java)
			startActivity(intent)
		}
	}
	private fun checkIsFavorite(){

	}

	private fun addToFavorites(){
		//setup data to add to database
		val hashMap = HashMap<String, Any>()
		hashMap["restaurantId"] = restaurantId

		//save to database
		val ref = FirebaseDatabase.getInstance().getReference("users")
		ref.child(firebaseAuth.uid!!).child("Favorites").child(restaurantId).setValue(hashMap)
			.addOnSuccessListener {
			//add to fav
				Log.d(TAG, "addToFavorite: Added to fav")
		}
			.addOnFailureListener{
			//failed to add to fav
				Log.d(TAG, "Failed to add to fav")
				Toast.makeText(this, "Failed to add to fav", Toast.LENGTH_SHORT).show()
			}
	}

	private fun removeFromFavorites(){

	}
}

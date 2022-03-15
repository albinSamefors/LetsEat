package com.example.letseat

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class RestaurantActivity : AppCompatActivity() {
	private lateinit var firebaseAuth: FirebaseAuth

	//retrieve restaurant id
	private val restaurantId = intent.getStringExtra("id")!!

	//will hold a boolean value to indicate either is in current users favorites list or not
	private var isInMyFavorites = false

	private val favoriteButton: Button = findViewById<Button>(R.id.favoritButton)

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_restaurant)

		//init firebase auth
		firebaseAuth = FirebaseAuth.getInstance()
		if (firebaseAuth.currentUser != null) {
			//user is logged in, check if is in fav or not
			checkIsFavorite()
		}


		favoriteButton.setOnClickListener {
			if (firebaseAuth.currentUser == null) {
				//user not logged in, can not use favorite function
				Toast.makeText(this, R.string.You_are_not_logged_in, Toast.LENGTH_SHORT).show()
			} else {
				//user logged in, can use function
				if (isInMyFavorites) {
					//already in fav, remove
					removeFromFavorites()
				} else {
					//not in fav, add
					addToFavorites()
				}
			}
		}

		val homeViewButton = findViewById<ImageButton>(R.id.backToListViewButton)
		homeViewButton.setOnClickListener {
			val intent = Intent(this, MainActivity::class.java)
			startActivity(intent)
		}
	}

	private fun checkIsFavorite() {
		Log.d(TAG, "checkIsFavorite: Checking if restaurant is in favorite or not")

		val ref = FirebaseDatabase.getInstance().getReference("users")
		ref.child(firebaseAuth.uid!!).child("Favorites").child(restaurantId)
			.addValueEventListener(object : ValueEventListener {
				override fun onDataChange(snapshot: DataSnapshot) {
					isInMyFavorites = snapshot.exists()
					if (isInMyFavorites) {
						//available in favorite
						Log.d(TAG, "onDataChanged: available in favorites")
						//set drawable to top icon
						favoriteButton.setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.drawable.ic_baseline_favorite_filled_red,0,0)
					} else {
						//not available in favorites
						Log.d(TAG, "onDataChanged: not available in favorites")
						//set drawable to top icon
						favoriteButton.setCompoundDrawablesRelativeWithIntrinsicBounds(0,R.drawable.ic_baseline_favorite_border_red,0,0)
					}
				}

				override fun onCancelled(error: DatabaseError) {
					Log.d(TAG, "onDataChanged: Failed to read value")
				}
			})
	}

	private fun addToFavorites() {
		//setup data to add to database
		val hashMap = HashMap<String, Any>()
		hashMap["restaurantId"] = restaurantId

		//save to database
		val ref = FirebaseDatabase.getInstance().getReference("users")
			ref.child(firebaseAuth.uid!!).child("Favorites").child(restaurantId).setValue(hashMap)
			.addOnSuccessListener {
				//add to fav
				Log.d(TAG, "addToFavorite: Added to fav")
				Toast.makeText(this, "Added to favorites", Toast.LENGTH_SHORT).show()
			}
			.addOnFailureListener { e ->
				//failed to add to fav
				Log.d(TAG, "addToFavorites: Failed to add to fav due to ${e.message}")
				Toast.makeText(this, "Failed to add to fav due to ${e.message}", Toast.LENGTH_SHORT)
					.show()
			}
	}

	private fun removeFromFavorites() {
		Log.d(TAG, "removeFromFavorite: Removing from fav")

		//database ref
		val ref = FirebaseDatabase.getInstance().getReference("users")
			ref.child(firebaseAuth.uid!!).child("Favorites").child(restaurantId)
			.removeValue()
			.addOnSuccessListener {
				Log.d(TAG, "removeFromFavorite: RemovedFromFav")
				Toast.makeText(this, "Removed from favorites", Toast.LENGTH_SHORT).show()
			}
			.addOnFailureListener { e ->
				Log.d(TAG, "removeFromFavorite: Failed to remove due to ${e.message}")
				Toast.makeText(this, "Failed to remove due to ${e.message}", Toast.LENGTH_SHORT)
					.show()
			}
	}
}

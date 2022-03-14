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
import com.google.firebase.database.*

class RestaurantActivity : AppCompatActivity() {
	private lateinit var firebaseAuth : FirebaseAuth

	//retrieve restaurant id
	private val restaurantId = intent.getStringExtra("id")!!

	//will hold a boolean value to indicate either is in current users favorites list or not
	private var isInMyFavorites = false

	private val favoriteButton: ImageButton = findViewById<ImageButton>(R.id.favoritButton)

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_restaurant)

		//init firebase auth
		firebaseAuth = FirebaseAuth.getInstance()
		if (firebaseAuth.currentUser != null){
			//user is logged in, check if is in fav or not
			checkIsFavorite()
		}

		favoriteButton.setOnClickListener {
			if (firebaseAuth.currentUser == null){
				//user not logged in, can not use favorite function
				Toast.makeText(this, "You are not logged in", Toast.LENGTH_SHORT).show()
			}else{
				//user logged in, can use function
				if(isInMyFavorites){
					//already in fav, remove
					removeFromFavorites()
				}else{
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
	private fun checkIsFavorite(){
		Log.d(TAG, "checkIsFavorite: Checking if restaurant is in favorite or not")

		val ref = FirebaseDatabase.getInstance().getReference("users").ref.child(firebaseAuth.uid!!).child("Favorites").child(restaurantId)
			.addValueEventListener(object : ValueEventListener {
				override fun onDataChange(snapshot: DataSnapshot){
					isInMyFavorites = snapshot.exists()
					if (isInMyFavorites){
						//available in favorite
						Log.d(TAG, "onDataChanged: available in favorites")
						//set drawable to top icon

					}
					else{
						//not available in favorites
						Log.d(TAG, "onDataChanged: not available in favorites")
						//set drawable to top icon

					}
				}
				override fun onCancelled(error: DatabaseError) {
					Log.d(TAG, "onDataChanged: Failed to read value")
				}
			})
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
			.addOnFailureListener{ e->
			//failed to add to fav
				Log.d(TAG, "addToFavorites: Failed to add to fav due to ${e.message}")
				Toast.makeText(this, "Failed to add to fav due to ${e.message}", Toast.LENGTH_SHORT).show()
			}
	}

	private fun removeFromFavorites(){
		Log.d(TAG, "removeFromFavorite: Removing from fav")

		//database ref
		val ref = FirebaseDatabase.getInstance().getReference("users")
			.ref.child(firebaseAuth.uid!!).child("Favorites").child(restaurantId)
			.removeValue()
			.addOnSuccessListener {
				Log.d(TAG, "removeFromFavorite: RemovedFromFav")
				Toast.makeText(this, "Removed from favorites", Toast.LENGTH_SHORT).show()
			}
			.addOnFailureListener{ e->
				Log.d(TAG, "removeFromFavorite: Failed to remove due to ${e.message}")
				Toast.makeText(this, "Failed to remove due to ${e.message}", Toast.LENGTH_SHORT).show()
			}
	}
}

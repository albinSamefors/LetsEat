package com.example.letseat

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import org.w3c.dom.Text

class RestaurantActivity : AppCompatActivity() {
	private lateinit var firebaseAuth: FirebaseAuth

	//retrieve restaurant id
	private var restaurantId = -1
	private lateinit var restaurantAddress: String
	private lateinit var restaurantLatLng: String
	private lateinit var restaurantOpen: String
	private lateinit var restaurantRating: String
	private lateinit var restaurantName: String

	//will hold a boolean value to indicate either is in current users favorites list or not
	private var isInMyFavorites = false

	private lateinit var favoriteButton: Button

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_restaurant)
		if(intent.hasExtra("id"))
		{
			val extras = intent.extras
			restaurantId = extras!!.getInt("id")
		}
		if(intent.hasExtra("address"))
		{
			val extras = intent.extras
			restaurantAddress = extras!!.getString("address","")
		}
		if(intent.hasExtra("latLng"))
		{
			val extras = intent.extras
			restaurantLatLng = extras!!.getString("latLng").toString()
		}
		if(intent.hasExtra("open"))
		{
			val extras = intent.extras
			restaurantOpen = extras!!.getString("open").toString()
		}
		if(intent.hasExtra("rating"))
		{
			val extras = intent.extras
			restaurantRating = extras!!.getString("rating").toString()
		}
		if(intent.hasExtra("name"))
		{
			val extras = intent.extras
			restaurantName = extras!!.getString("name").toString()
		}


		favoriteButton = findViewById<Button>(R.id.favoritButton)
		var titleView = findViewById<TextView>(R.id.restaurantTitle)
		var ratingsBar = findViewById<RatingBar>(R.id.restaurantRating)
		var adressView = findViewById<TextView>(R.id.adressView)
		var openNowView = findViewById<TextView>(R.id.openNowView)
		titleView.text = ""
		if(restaurantId!= -1) {
			var restaurant = restaurantRepository.getSpecificRestaurant(restaurantId)
			titleView.text = restaurant.restaurantName
			ratingsBar.rating = restaurant.rating
			adressView.text = restaurant.adress
			if(restaurant.openNow != "Open" && restaurant.openNow != "Closed")
			{
				openNowView.text = "Dont Know"
			}
			else
			{
				openNowView.text = restaurant.openNow
			}

			restaurantAddress = restaurant.adress
			restaurantLatLng = restaurant.latLng.toString()
			restaurantOpen = restaurant.openNow
			restaurantName = restaurant.restaurantName
			restaurantRating = restaurant.rating.toString()
		}
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
			finish()
		}
	}

	private fun checkIsFavorite() {
		Log.d(TAG, "checkIsFavorite: Checking if restaurant is in favorite or not")

		val ref = FirebaseDatabase.getInstance("https://let-s-eat-c3632-default-rtdb.europe-west1.firebasedatabase.app/").getReference("users")
		ref.child(firebaseAuth.uid!!).child("Favorites").child(restaurantId.toString())
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
		hashMap["restaurantId"] = restaurantId.toString()
		hashMap["restaurantAddress"] = restaurantAddress
		hashMap["restaurantLatLng"] = restaurantLatLng
		hashMap["restaurantName"] = restaurantName
		hashMap["restaurantOpen"] = restaurantOpen
		hashMap["restaurantRating"] = restaurantRating

		//save to database
		val ref = FirebaseDatabase.getInstance("https://let-s-eat-c3632-default-rtdb.europe-west1.firebasedatabase.app/").getReference("users")
			ref.child(firebaseAuth.uid!!).child("Favorites").child(restaurantId.toString()).setValue(hashMap)
			.addOnSuccessListener {
				//add to fav
				Log.d(TAG, "addToFavorite: Added to fav")
				Toast.makeText(this, R.string.Added_to_favorites, Toast.LENGTH_SHORT).show()
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
		val ref = FirebaseDatabase.getInstance("https://let-s-eat-c3632-default-rtdb.europe-west1.firebasedatabase.app/").getReference("users")
			ref.child(firebaseAuth.uid!!).child("Favorites").child(restaurantId.toString())
			.removeValue()
			.addOnSuccessListener {
				Log.d(TAG, "removeFromFavorite: RemovedFromFav")
				Toast.makeText(this, R.string.Removed_from_favorites, Toast.LENGTH_SHORT).show()
			}
			.addOnFailureListener { e ->
				Log.d(TAG, "removeFromFavorite: Failed to remove due to ${e.message}")
				Toast.makeText(this, "Failed to remove due to ${e.message}", Toast.LENGTH_SHORT)
					.show()
			}
	}
}

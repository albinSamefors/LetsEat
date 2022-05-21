package com.example.letseat
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase


class AccountActivity : AppCompatActivity() {
	private lateinit var emailTextView: TextView
	private lateinit var firebaseAuth: FirebaseAuth
	private lateinit var ref: DatabaseReference
	private lateinit var userLatLng : LatLng
	private var listView: ListView? = null
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_account)
		//Initializing Firebase authentication
		firebaseAuth = FirebaseAuth.getInstance()
		emailTextView = findViewById(R.id.textViewEmail)
		listView = findViewById(R.id.lvAccountView)
		if(intent.hasExtra("userLat") && intent.hasExtra("userLng"))
		{
			var extras = intent.extras
			userLatLng = LatLng(extras!!.getDouble("userLat", 0.0),extras!!.getDouble("userLng",0.0))

		}

		val listViewButton = findViewById<ImageButton>(R.id.accountListViewButton)
		listViewButton.setOnClickListener {
			val intent = Intent(this, MainActivity::class.java)
			startActivity(intent)
		}

		val mapButton = findViewById<ImageButton>(R.id.accountMapButton)
		mapButton.setOnClickListener {
			val intent = Intent(this, MapsActivity::class.java)
			startActivity(intent)
		}

		val logOutButton = findViewById<Button>(R.id.btnLogout)
		logOutButton.setOnClickListener {
			Firebase.auth.signOut()
			Toast.makeText(this, R.string.Successfully_signed_out, Toast.LENGTH_SHORT).show()
			startActivity(Intent(this, MainActivity::class.java))
			finish()
		}
		initializeListView()
		{
			var adapter = RestaurantListAdapter(this,R.layout.restaurant_item_white,
				favoriteRestaurantRepository.getAllRestaurants(),userLatLng)
			listView!!.adapter = adapter
			listView!!.setOnItemClickListener { parent, view, position, id ->
				val intent = Intent(this, RestaurantActivity::class.java)
				intent.putExtra("id", favoriteRestaurantRepository.getSpecificRestaurant(position).id)
				intent.putExtra("isFavorite",true)
				startActivity(intent)
			}
		}





	}

	override fun onStart() {
		super.onStart()

		val firebaseUser: FirebaseUser? = firebaseAuth.currentUser
		if (firebaseUser != null) {
			//there is some user logged in
			emailTextView.text = firebaseUser.email
		} else {
			//no one logged in
			AlertDialog.Builder(this)
				.setTitle(R.string.not_logged_in)
				.setMessage(R.string.message)
				.setPositiveButton(
					R.string.yes
				) { dialog, whichButton ->
					//send user to login screen
					startActivity(Intent(this, LoginActivity::class.java))
					finish()
				}.setNegativeButton(
					R.string.no
				) { dialog, whichButton ->
					finish()
				}.show()
		}
	}
	private fun initializeListView(callback : () -> Unit)  {
		// creating a new array adapter for our list view.
		// below line is used for getting reference
		// of our Firebase Database.
		Thread(Runnable {
		ref = FirebaseDatabase.getInstance("https://let-s-eat-c3632-default-rtdb.europe-west1.firebasedatabase.app/")
			.getReference("users").child(firebaseAuth.currentUser!!.uid)
		var values = ref.child("Favorites")
		ref.get().addOnSuccessListener {
			for(child in it.child("Favorites").children)
			{
				val restaurantLat = child.child("restaurantLat").value.toString().toDouble()
				val restaurantLng = child.child("restaurantLng").value.toString().toDouble()
				val restaurantLatLng  = LatLng(restaurantLat,restaurantLng)
				var isOpen = child.child("restaurantOpen").value.toString()
				favoriteRestaurantRepository.addRestaurant(child.child("restaurantName").value.toString(),restaurantLatLng,child.child("restaurantRating").value.toString().toFloat(),child.child("restaurantAddress").value.toString())


			}
			callback()


		}
		}).start()

	}

}

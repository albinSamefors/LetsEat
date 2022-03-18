package com.example.letseat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase

class AccountActivity : AppCompatActivity() {
	private lateinit var usernameTextView: TextView
	private lateinit var firebaseAuth: FirebaseAuth
	private lateinit var database: FirebaseDatabase
	private lateinit var ref: DatabaseReference

	// creating variables for our list view.
	private var listView: ListView? = null
	var restaurantArrayList: ArrayList<String?>? = null
	val favouriteRepository = RestaurantRepository()
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_account)
		//initiation
		firebaseAuth = FirebaseAuth.getInstance()
		usernameTextView = findViewById(R.id.tvUsername)
		listView = findViewById(R.id.lvAccountView)
		restaurantArrayList = ArrayList()




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
	}

	override fun onStart() {
		super.onStart()

		val mFirebaseUser: FirebaseUser? = firebaseAuth.currentUser
		if (mFirebaseUser != null) {
			//there is some user logged in
			usernameTextView.text = mFirebaseUser.email
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
	private fun initializeListView() {
		// creating a new array adapter for our list view.
	//	val adapter = RestaurantListAdapter(this,R.layout.restaurant_item_white, ,user)// ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, restaurantArrayList)

		// below line is used for getting reference
		// of our Firebase Database.
		ref = FirebaseDatabase.getInstance("https://let-s-eat-c3632-default-rtdb.europe-west1.firebasedatabase.app/")
			.getReference("users")

		ref = ref.child(firebaseAuth.uid!!).child("Favorites")

		ref.addValueEventListener( object : ValueEventListener {
			override fun onDataChange(snapshot: DataSnapshot) {
				restaurantArrayList!!.add(snapshot.getValue(String::class.java))
			}

			override fun onCancelled(error: DatabaseError) {
				TODO("Not yet implemented")
			}
		})
		// in below line we are calling method for add child event
		// listener to get the child of our database.
		ref!!.addChildEventListener(object : ChildEventListener {
			override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
				// this method is called when new child is added to
				// our data base and after adding new child
				// we are adding that item inside our array list and
				// notifying our adapter that the data in adapter is changed.
				restaurantArrayList!!.add(snapshot.getValue(String::class.java))
	//			adapter.notifyDataSetChanged()
			}

			override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
				// this method is called when the new child is added.
				// when the new child is added to our list we will be
				// notifying our adapter that data has changed.
			//	adapter.notifyDataSetChanged()
			}

			override fun onChildRemoved(snapshot: DataSnapshot) {
				// below method is called when we remove a child from our database.
				// inside this method we are removing the child from our array list
				// by comparing with it's value.
				// after removing the data we are notifying our adapter that the
				// data has been changed.
				restaurantArrayList!!.remove(snapshot.getValue(String::class.java))
				//adapter.notifyDataSetChanged()
			}

			override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
				// this method is called when we move our
				// child in our database.
				// in our code we are note moving any child.
			}

			override fun onCancelled(error: DatabaseError) {
				// this method is called when we get any
				// error from Firebase with error.
			}
		})
		// below line is used for setting
		// an adapter to our list view.
	//	listView!!.adapter = adapter
	}
}

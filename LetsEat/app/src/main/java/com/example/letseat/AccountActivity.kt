package com.example.letseat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class AccountActivity : AppCompatActivity() {
	private lateinit var usernameTextView: TextView
	private lateinit var mFirebaseAuth: FirebaseAuth
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_account)

		usernameTextView = findViewById(R.id.tvUsername)
		mFirebaseAuth = FirebaseAuth.getInstance()


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
			Toast.makeText(this, "Successfully signed out ", Toast.LENGTH_SHORT).show()
			startActivity(Intent(this, MainActivity::class.java))
			finish()
		}
	}

	override fun onStart() {
		super.onStart()

		val mFirebaseUser: FirebaseUser? = mFirebaseAuth.currentUser
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


}
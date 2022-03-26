@file:Suppress("DEPRECATION")

package com.example.letseat

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.letseat.DatabaseRepository.updateUI
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider

@Suppress("DEPRECATION", "DEPRECATION")
class LoginActivity : AppCompatActivity() {

	private lateinit var tvRedirectSignUp: TextView
	private lateinit var etEmail: EditText
	private lateinit var etPass: EditText
	private lateinit var btnLogin: Button
	private lateinit var tvForgotPassword: TextView

	//google
	private lateinit var btnGoogle: SignInButton
	private lateinit var mGoogleSignInClient: GoogleSignInClient
	private val Req_Code: Int = 123

	// Creating firebaseAuth object
	private lateinit var auth: FirebaseAuth

	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_login)

		FirebaseApp.initializeApp(this)

		// View Binding
		tvRedirectSignUp = findViewById(R.id.tvRedirectSignUp)
		btnLogin = findViewById(R.id.btnLogin)
		etEmail = findViewById(R.id.etEmailAddress)
		etPass = findViewById(R.id.etPassword)
		btnGoogle = findViewById(R.id.google_button)
		tvForgotPassword = findViewById(R.id.tvForgotPassword)

		//Configure Google Sign In
		val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
			.requestIdToken(getString(R.string.default_web_client_id))
			.requestEmail()
			.build()
		// Getting the value of gso inside the GoogleSignInClient
		mGoogleSignInClient = GoogleSignIn.getClient(this, gso)

		// initialising Firebase auth object
		auth = FirebaseAuth.getInstance()

		btnLogin.setOnClickListener {
			loginFirebase()
		}

		btnGoogle.setOnClickListener { view: View? ->
			Toast.makeText(this, R.string.Logging_In, Toast.LENGTH_SHORT).show()
			signInGoogle()
		}

		tvRedirectSignUp.setOnClickListener {
			val intent = Intent(this, RegistrationActivity::class.java)
			startActivity(intent)
			// using finish() to end the activity
			finish()
		}

		tvForgotPassword.setOnClickListener {
			startActivity(Intent(this, ForgotPasswordActivity::class.java))
			finish()
		}
	}

	override fun onStart() {
		super.onStart()

		val mFirebaseUser: FirebaseUser? = auth.currentUser
		if (mFirebaseUser != null) {
			//there is some user logged in
			Toast.makeText(this, R.string.User_already_logged_in, Toast.LENGTH_SHORT).show()
			val intent = Intent(this,MainActivity::class.java)
			startActivity(intent)
			finish()
		}
	}

	private fun loginFirebase() {
		val email = etEmail.text.toString()
		val pass = etPass.text.toString()
		// check pass
		if (email.isBlank() || pass.isBlank()) {
			Toast.makeText(this, R.string.Email_and_Password_cant_be_blank, Toast.LENGTH_SHORT).show()
			return
		}
		// calling signInWithEmailAndPassword(email, pass)
		// function using Firebase auth object
		// On successful response Display a Toast
		auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(this) {
			if (it.isSuccessful) {
				Toast.makeText(this, R.string.Successfully_Logged_In, Toast.LENGTH_SHORT).show()
				startActivity(Intent(this, MainActivity::class.java))
				finish()
			} else
				Toast.makeText(this, R.string.Log_In_failed, Toast.LENGTH_SHORT).show()
		}
	}

	// signInGoogle() function
	private fun signInGoogle() {
		val signInIntent: Intent = mGoogleSignInClient.signInIntent
		startActivityForResult(signInIntent, Req_Code)
	}

	// onActivityResult() function : this is where we provide the task and data for the Google Account
	override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
		super.onActivityResult(requestCode, resultCode, data)

		//Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
		if (requestCode == Req_Code) {
			val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
			handleResult(task)
		}
	}

	// handleResult() function -  this is where we update the UI after Google signin takes place
	private fun handleResult(completedTask: Task<GoogleSignInAccount>) {
		try {
			val account: GoogleSignInAccount? = completedTask.getResult(ApiException::class.java)
			if (account != null) {
				firebaseAuthWithGoogle(account)
			}
		} catch (e: ApiException) {
			Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show()
		}
	}

	// firebaseAuthWithGoogle() function - this is where we specify what UI updating are needed after google signIn has taken place.
	private fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {
		val credential = GoogleAuthProvider.getCredential(account.idToken, null)
		auth.signInWithCredential(credential)
			.addOnCompleteListener { task ->
			if (task.isSuccessful) {
				val newUser = User(account.idToken, null)
				val user = auth.currentUser
				updateUI(user, newUser)
				val intent = Intent(this, MainActivity::class.java)
				startActivity(intent)
				finish()
			}
		}
	}
}
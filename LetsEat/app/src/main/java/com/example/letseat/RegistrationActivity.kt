package com.example.letseat

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.letseat.DatabaseRepository.updateUser
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class RegistrationActivity : AppCompatActivity() {
    private lateinit var etEmail: EditText
    private lateinit var etConfPass: EditText
    private lateinit var etPass: EditText
    private lateinit var btnSignUp: Button
    private lateinit var tvRedirectLogin: TextView

    // create Firebase authentication object
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        // View Bindings
        etEmail = findViewById(R.id.etSEmailAddress)
        etConfPass = findViewById(R.id.etSConfPassword)
        etPass = findViewById(R.id.etSPassword)
        btnSignUp = findViewById(R.id.btnSSigned)
        tvRedirectLogin = findViewById(R.id.tvRedirectLogin)

        //Initialising auth object
        auth = Firebase.auth

        //Sign up user
        btnSignUp.setOnClickListener {
            signUpUser()
        }

        //Switching from RegistrationActivity to LoginActivity
        tvRedirectLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun signUpUser() {
        val email = etEmail.text.toString()
        val pass = etPass.text.toString()
        val confirmPassword = etConfPass.text.toString()

        // check if any field is blank
        if (email.isBlank() || pass.isBlank() || confirmPassword.isBlank()) {
            Toast.makeText(this, R.string.Email_and_Password_cant_be_blank, Toast.LENGTH_SHORT).show()
        }
        // check if password match confirm password
        if (pass != confirmPassword) {
            Toast.makeText(this, R.string.Password_and_Confirm_Password_do_not_match, Toast.LENGTH_SHORT)
                .show()
        }

        // If all credential are correct
        // We call createUserWithEmailAndPassword
        // using auth object and pass the
        // email and pass in it.

        val newUser = User(email, pass)

        auth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                Log.d(TAG, "createUserWithEmail:success")
                Toast.makeText(this, R.string.Successfully_Signed_Up, Toast.LENGTH_SHORT).show()
                val user = auth.currentUser
                updateUser(user, newUser)
                startActivity(Intent (this, MainActivity::class.java))
                finish()
            } else {
                Log.w(TAG, "createUserWithEmail:failure", task.exception)
                Toast.makeText(this, R.string.Sign_Up_Failed, Toast.LENGTH_SHORT).show()
                updateUser(null, newUser)
            }
        }
    }

}

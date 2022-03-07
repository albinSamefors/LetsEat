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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase


class RegistrationActivity : AppCompatActivity() {
    lateinit var etEmail: EditText
    lateinit var etConfPass: EditText
    private lateinit var etPass: EditText
    private lateinit var btnSignUp: Button
    lateinit var tvRedirectLogin: TextView
    private lateinit var database: DatabaseReference

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

        // Initialising auth object
        auth = Firebase.auth

        btnSignUp.setOnClickListener {
            signUpUser()
        }

        // switching from RegistrationActivity to LoginActivity
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
            Toast.makeText(this, "Email and Password can't be blank", Toast.LENGTH_SHORT).show()
        }
        // check if password match confirm password
        if (pass != confirmPassword) {
            Toast.makeText(this, "Password and Confirm Password do not match", Toast.LENGTH_SHORT)
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
                Toast.makeText(this, "Successfully Signed Up", Toast.LENGTH_SHORT).show()
                val user = auth.currentUser
                Toast.makeText(this, "updateUI", Toast.LENGTH_SHORT).show()
                updateUI(user, newUser)
                Toast.makeText(this, "updateUI done", Toast.LENGTH_SHORT).show()
                startActivity(Intent (this, MainActivity::class.java))
                finish()
            } else {
                Log.w(TAG, "createUserWithEmail:failure", task.exception)
                Toast.makeText(this, "Sign Up Failed!", Toast.LENGTH_SHORT).show()
                updateUI(null, newUser)
            }
        }
    }

    private fun updateUI(user: FirebaseUser?, newAccount: User){

        Toast.makeText(this, "Set path users", Toast.LENGTH_SHORT).show()

        FirebaseDatabase.getInstance("https://let-s-eat-c3632-default-rtdb.europe-west1.firebasedatabase.app/").getReference("users")
            .child(FirebaseAuth.getInstance().currentUser?.uid!!).setValue(newAccount)
            .addOnCompleteListener {task ->
                if (task.isSuccessful){
                    Log.d(TAG, "createUserWithEmail:success")
                    Toast.makeText(baseContext, "Database successful", Toast.LENGTH_SHORT).show()
                } else{
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Database failed", Toast.LENGTH_SHORT).show()
                }
            }
    }
}
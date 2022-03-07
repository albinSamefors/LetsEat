package com.example.letseat

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase


class RegistrationActivity : AppCompatActivity() {
    lateinit var etEmail: EditText
    lateinit var etConfPass: EditText
    private lateinit var etPass: EditText
    private lateinit var btnSignUp: Button
    lateinit var tvRedirectLogin: TextView

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
        }
    }

    private fun signUpUser() {
        val email = etEmail.text.toString()
        val pass = etPass.text.toString()
        val confirmPassword = etConfPass.text.toString()

        // check pass
        if (email.isBlank() || pass.isBlank() || confirmPassword.isBlank()) {
            Toast.makeText(this, "Email and Password can't be blank", Toast.LENGTH_SHORT).show()
            return
        }

        if (pass != confirmPassword) {
            Toast.makeText(this, "Password and Confirm Password do not match", Toast.LENGTH_SHORT)
                .show()
            return
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
                updateUI(user, newUser)
                startActivity(Intent (this, LoginActivity::class.java))
                finish()
            } else {
                Log.w(TAG, "createUserWithEmail:failure", task.exception)
                Toast.makeText(this, "Sign Up Failed!", Toast.LENGTH_SHORT).show()
                updateUI(null, newUser)
            }
        }
    }

    private fun updateUI(user: FirebaseUser?, newAccount: User){
        FirebaseDatabase.getInstance().getReference("users")
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

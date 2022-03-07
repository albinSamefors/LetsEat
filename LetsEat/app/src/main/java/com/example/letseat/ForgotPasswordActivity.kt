package com.example.letseat

import com.example.letseat.R
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class ForgotPasswordActivity : AppCompatActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_forgot_password)

		//viewbindings
		val etForgotPassword: EditText = findViewById(R.id.etEmailAddress)
		val submitButton: Button = findViewById(R.id.btn_submit)

		submitButton.setOnClickListener {
			val email: String = etForgotPassword.text.toString().trim { it <= ' ' }
			if (email.isEmpty()) {
				Toast.makeText(this, "Please enter email adress.", Toast.LENGTH_SHORT).show()
			} else {
				FirebaseAuth.getInstance().sendPasswordResetEmail(email)
					.addOnCompleteListener { task ->
						if (task.isSuccessful) {
							Toast.makeText(
								this,
								"Email sent successfully to reset your password!",
								Toast.LENGTH_SHORT
							).show()
							finish()
						} else {
							Toast.makeText(
								this,
								task.exception!!.message.toString(),
								Toast.LENGTH_SHORT
							).show()
						}
					}
			}
		}
	}
}
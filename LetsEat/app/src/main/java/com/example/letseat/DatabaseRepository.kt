package com.example.letseat

import android.content.ContentValues.TAG
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase

object DatabaseRepository{
    fun updateUI(user: FirebaseUser?, newAccount: User) {
        FirebaseDatabase.getInstance("https://let-s-eat-c3632-default-rtdb.europe-west1.firebasedatabase.app/")
            .getReference("users")
            .child(FirebaseAuth.getInstance().currentUser?.uid!!).setValue(newAccount)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "createUserWithEmail:success")
                    /*Toast.makeText(baseContext, "Database successful", Toast.LENGTH_SHORT).show()*/
                } else {
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    /*Toast.makeText(baseContext, "Database failed", Toast.LENGTH_SHORT).show()*/
                }
            }
    }
}
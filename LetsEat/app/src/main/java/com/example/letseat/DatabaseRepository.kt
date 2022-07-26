package com.example.letseat

import android.content.ContentValues.TAG
import android.content.res.Resources
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.FirebaseDatabase

object DatabaseRepository{
    fun updateUser(user: FirebaseUser?, newAccount: User) {
        FirebaseDatabase.getInstance(Resources.getSystem().getString(R.string.database_url))
            .getReference("users")
            .child(FirebaseAuth.getInstance().currentUser?.uid!!).setValue(newAccount)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "createUserWithEmail:success")
                } else {
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)
                }
            }
    }
}
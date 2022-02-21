package com.example.letseat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton

class AccountActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)


        val listViewButton = findViewById<ImageButton>(R.id.accountListViewButton)
        listViewButton.setOnClickListener{
            val intent = Intent (this, MainActivity::class.java)
            startActivity(intent)
        }

        val mapButton = findViewById<ImageButton>(R.id.accountMapButton)
        mapButton.setOnClickListener{
            val intent =Intent (this, MapsActivity::class.java)
            startActivity(intent)
        }
    }


}
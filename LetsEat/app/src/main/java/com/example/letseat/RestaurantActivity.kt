package com.example.letseat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton

class RestaurantActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_restaurant)

        //retrieve restaurant id
        val restaurantId = intent.getIntExtra("id", 0)

        val favoriteButton = findViewById<ImageButton>(R.id.favoritButton)



        val homeViewButton = findViewById<ImageButton>(R.id.backToListViewButton)
        homeViewButton.setOnClickListener{
            val intent = Intent (this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}
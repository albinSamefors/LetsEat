package com.example.letseat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.GestureDetector
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout


class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val rightIntent = Intent(this, MapsActivity::class.java)
        val container = findViewById<ListView>(R.id.restaurantView)
        val mapButton = findViewById<ImageButton>(R.id.mapButton)

       mapButton.setOnClickListener{
           startActivity(rightIntent)
       }
        /*
        var swipeListener : SwipeListener = SwipeListener()
        swipeListener.SwipeListener(container,rightIntent,false,this)
        */


        // Adds the restaurants to the main screen
        val listView = findViewById<ListView>(R.id.restaurantView)
        val restaurantListAdapter : RestaurantListAdapter = RestaurantListAdapter(this,R.layout.restaurant_item,
            restaurantRepository.getAllRestaurants())
        listView.adapter=restaurantListAdapter




    }




}
package com.example.letseat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.GestureDetector
import android.widget.ListAdapter
import android.widget.ScrollView
import androidx.constraintlayout.widget.ConstraintLayout


class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val rightIntent = Intent(this, MapsActivity::class.java)
        val container = findViewById<ConstraintLayout>(R.id.mainContainer)
        var swipeListener : SwipeListener = SwipeListener()
        swipeListener.SwipeListener(container,rightIntent,true,this)


        // TODO: Implement reading of the restaurants in the global variable and show them on the home screen 




    }




}
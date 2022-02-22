package com.example.letseat

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.GestureDetector
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat

// TODO: Ändra så att alla färger hämtas ifrån temat istället för de hårdkodade färgerna Samt fixa darkmode
class MainActivity : AppCompatActivity() {

private lateinit var permissionsLauncher : ActivityResultLauncher<Array<String>>
private var fineLocationPermissionGranted = false
    private var coarseLocationPermissionGranted = false
    private var internetPermissionGranted = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        permissionsLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){permissions ->
            fineLocationPermissionGranted = permissions[android.Manifest.permission.ACCESS_FINE_LOCATION] ?: fineLocationPermissionGranted
            coarseLocationPermissionGranted = permissions[android.Manifest.permission.ACCESS_COARSE_LOCATION] ?: coarseLocationPermissionGranted
            internetPermissionGranted = permissions[android.Manifest.permission.INTERNET] ?: internetPermissionGranted

        }
        requestPermissions()

        val rightIntent = Intent(this, MapsActivity::class.java)
        val logInIntent = Intent(this, LoginActivity::class.java)
        val container = findViewById<ListView>(R.id.restaurantView)
        val mapButton = findViewById<ImageButton>(R.id.mapButton)
       mapButton.setOnClickListener{
           startActivity(rightIntent)
       }


        /*
        var swipeListener : SwipeListener = SwipeListener()
        swipeListener.SwipeListener(container,rightIntent,false,this)
        */
        val loginButton = findViewById<ImageButton>(R.id.logInButton)
        loginButton.setOnClickListener{
            val intent =Intent (this, LoginActivity::class.java)
            startActivity(intent)
        }


        // Adds the restaurants to the main screen
        val listView = findViewById<ListView>(R.id.restaurantView)
        val restaurantListAdapter : RestaurantListAdapter = RestaurantListAdapter(this,R.layout.restaurant_item,
            restaurantRepository.getAllRestaurants())
        listView.adapter=restaurantListAdapter

        val restaurantItem = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            android.R.id.text1,
            restaurantRepository.getAllRestaurants()
        )
        listView.setOnItemClickListener { parent,view,position,id->
            val clickRestaurant = restaurantItem.getItem(position)
            val listId = clickRestaurant?.id

            val intent = Intent(this, RestaurantActivity::class.java)
            intent.putExtra("id",listId)
            startActivity(intent)
        }



    }
    private fun requestPermissions()
    {
        coarseLocationPermissionGranted = ContextCompat.checkSelfPermission(
            this, android.Manifest.permission.ACCESS_COARSE_LOCATION)== PackageManager.PERMISSION_GRANTED

        fineLocationPermissionGranted = ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED

        internetPermissionGranted = ContextCompat.checkSelfPermission(this, android.Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED

        val permissionRequest : MutableList<String> = ArrayList()

        if(!coarseLocationPermissionGranted)
        {
            permissionRequest.add(android.Manifest.permission.ACCESS_COARSE_LOCATION)
        }
        if(!fineLocationPermissionGranted)
        {
            permissionRequest.add(android.Manifest.permission.ACCESS_FINE_LOCATION)
        }
        if(!internetPermissionGranted)
        {
            permissionRequest.add(android.Manifest.permission.INTERNET)
        }
        if(permissionRequest.isNotEmpty())
        {
            permissionsLauncher.launch(permissionRequest.toTypedArray())
        }
    }




}
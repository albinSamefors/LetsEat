package com.example.letseat

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat

// TODO: Ändra så att alla färger hämtas ifrån temat istället för de hårdkodade färgerna Samt fixa darkmode
class MainActivity : AppCompatActivity() {



private lateinit var permissionsLauncher : ActivityResultLauncher<Array<String>>
private var fineLocationPermissionGranted = false
    private var coarseLocationPermissionGranted = false
    private var internetPermissionGranted = false

    var progressValue = 500
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        permissionsLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()){permissions ->
            fineLocationPermissionGranted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: fineLocationPermissionGranted
            coarseLocationPermissionGranted = permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: coarseLocationPermissionGranted
            internetPermissionGranted = permissions[Manifest.permission.INTERNET] ?: internetPermissionGranted

        }
        requestPermissions()

        val mapIntent = Intent(this, MapsActivity::class.java)
        


        val logInIntent = Intent(this, LoginActivity::class.java)
        val mapButton = findViewById<ImageButton>(R.id.mapButton)
        val distanceBar = findViewById<SeekBar>(R.id.distanceBar)
        val listView = findViewById<ListView>(R.id.restaurantView)
        val distanceView= findViewById<TextView>(R.id. distanceView)


        mapButton.setOnClickListener{ mapIntent.putExtra("radius", progressValue)
           startActivity(mapIntent)
       }



        val loginButton = findViewById<ImageButton>(R.id.logInButton)
        loginButton.setOnClickListener{
            val intent =Intent (this, LoginActivity::class.java)
            startActivity(intent)
        }


        // Adds the restaurants to the main screen

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

        //Seekbar setup
        progressValue = intent.getIntExtra("radius",500)


        distanceBar.max = 5000
        distanceBar.progress = progressValue

        distanceBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener
        {
            override fun onProgressChanged(bar: SeekBar?, progress: Int, fromUser: Boolean) {
                distanceView.text = progress.toString() + "m"
                progressValue = progress
            }

            override fun onStartTrackingTouch(bar: SeekBar?) {
               // When user starts touching the bar do this
            }

            override fun onStopTrackingTouch(bar: SeekBar?) {
                // When user releases the bar do this!

            }

        })



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
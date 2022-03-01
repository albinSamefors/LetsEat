package com.example.letseat

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.widget.ImageButton
import android.widget.SeekBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.letseat.databinding.ActivityMapsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.*
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var mapCircle: Circle
    private lateinit var binding: ActivityMapsBinding
    private lateinit var client: FusedLocationProviderClient
    private lateinit var mapFragment : SupportMapFragment
    var progressValue  = 500
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
         mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        val usedIntent = intent
       progressValue = usedIntent.getIntExtra("radius",500)
        val listViewButton = findViewById<ImageButton>(R.id.listViewButton)
        listViewButton.setOnClickListener{
            val intent =Intent (this, MainActivity::class.java)
            intent.putExtra("radius", progressValue)
            startActivity(intent)
        }
        val accountButton = findViewById<ImageButton>(R.id.AccountButton)
        accountButton.setOnClickListener{
            val intent = Intent (this, AccountActivity::class.java)
            startActivity(intent)
        }
        client = LocationServices.getFusedLocationProviderClient(this)
        //Permission check
        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
            getCurrentPosition()
// TODO: Update the position with a set interval 
        }
        else{
            // When permission denied
            // Request permission
               ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),44)
        }


        val distanceBar = findViewById<SeekBar>(R.id.mapDistanceBar)
        distanceBar.max = 5000 //Ändra detta till en variabel
        distanceBar.progress = progressValue
        distanceBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener
        {
            override fun onProgressChanged(bar: SeekBar?, progress: Int, fromUser: Boolean) {
                 progressValue = progress
                mapCircle.radius = progressValue.toDouble()
            }
            override fun onStartTrackingTouch(bar: SeekBar?) {
                // When user starts touching the bar do this
            }
            override fun onStopTrackingTouch(bar: SeekBar?) {
                // When user releases the bar do this!
            }
        })
    }

    @SuppressLint("MissingPermission")
    private fun getCurrentPosition() {
        val task : Task<Location> = client.lastLocation


        task.addOnSuccessListener(object : OnSuccessListener<Location> {
            override fun onSuccess(location: Location?) {
                if(location != null)
                {
                    //Location Success
                    mapFragment.getMapAsync(object : OnMapReadyCallback{
                        override fun onMapReady(gMap: GoogleMap) {
                            //init LatLng
                            val userLatLng = LatLng(location.latitude,location.longitude)
                            val markerOptions = MarkerOptions().position(userLatLng).title("Your Location")

                            //zoom camera
                            gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 14.5f))
                            //Place Marker
                            gMap.addMarker(markerOptions)
                            // TODO: Fix these hardcoded values so they get their source from variable places
                            val circleOptions = CircleOptions()
                            circleOptions.center(userLatLng)
                            circleOptions.radius(progressValue.toDouble())
                            circleOptions.strokeColor(R.color.Red_Blurred)
                            circleOptions.fillColor(R.color.Transparent_Red) // TODO: Ändra denna 
                            circleOptions.strokeWidth(5f)

                            mapCircle = gMap.addCircle(circleOptions)
                        }
                    })
                }
            }
        })

    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == 44)
        {
            if(grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                getCurrentPosition()
            }
        }
    }
}
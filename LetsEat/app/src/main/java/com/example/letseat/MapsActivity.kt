package com.example.letseat

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentProviderClient
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
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
    private lateinit var binding: ActivityMapsBinding
    private lateinit var client: FusedLocationProviderClient
    private lateinit var location : Location
    private lateinit var mapFragment : SupportMapFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
         mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


        val listViewButton = findViewById<ImageButton>(R.id.listViewButton)
        listViewButton.setOnClickListener{
            val intent =Intent (this, MainActivity::class.java)
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


    }

    @SuppressLint("MissingPermission")
    private fun getCurrentPosition() {
        var task : Task<Location> = client.lastLocation


        task.addOnSuccessListener(object : OnSuccessListener<Location> {
            override fun onSuccess(location: Location?) {
                if(location != null)
                {
                    //Location Success
                    mapFragment.getMapAsync(object : OnMapReadyCallback{
                        override fun onMapReady(gMap: GoogleMap) {
                            //init LatLng
                            var userLatLng : LatLng = LatLng(location.latitude,location.longitude)
                            var markerOptions = MarkerOptions().position(userLatLng).title("Your Location")

                            //zoom camera
                            gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 14.5f))
                            //Place Marker
                            gMap.addMarker(markerOptions)
                            // TODO: Fix these hardcoded values so they get their source from variable places
                            var circleOptions = CircleOptions()
                            circleOptions.center(userLatLng)
                            circleOptions.radius(150.0)
                            circleOptions.strokeColor(R.color.Red_Blurred)
                            circleOptions.fillColor(R.color.Transparent_Red)
                            circleOptions.strokeWidth(5f)

                            gMap.addCircle(circleOptions)
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
                getCurrentPosition();
            }
        }

    }


}
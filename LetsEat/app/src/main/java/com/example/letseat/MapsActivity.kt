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
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.Marker
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

        }
        else{
            // When permission denied
            // Request permission
            ActivityCompat.requestPermissions(this,)
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
                            gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLatLng, 10f))
                            //Place Marker
                            gMap.addMarker(markerOptions)
                        }

                    })
                }


            }


        })

    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap



        val userLatLng = LatLng(location.latitude, location.longitude)
      //  locationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        // TODO: Ändra stilen på pinsen så att det stämmer överens med temat 
        // Add a marker in Sydney and move the camera
        mMap.addMarker(MarkerOptions().position(userLatLng))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(userLatLng))

    }


}
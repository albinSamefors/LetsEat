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
import kotlin.math.log

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var mapCircle: Circle
    private lateinit var binding: ActivityMapsBinding
    private lateinit var client: FusedLocationProviderClient
    private lateinit var mapFragment : SupportMapFragment
    private lateinit var userLatLng: LatLng
    var progressValue  = 0
    private lateinit var circleBounds: LatLngBounds
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
         mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        val usedIntent = intent
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
        progressValue = usedIntent.getIntExtra("radius",resources.getInteger(R.integer.standard_radius))
        distanceBar.max = resources.getInteger(R.integer.maximum_radius)
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

                mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(getBounds(mapCircle),20))
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
                             userLatLng = LatLng(location.latitude,location.longitude)
                            val markerOptions = MarkerOptions().position(userLatLng).title("Your Location")
                           
                            //zoom camera
                            gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userLatLng,14.5f))
                            //Place Marker
                            gMap.addMarker(markerOptions)
                            val circleOptions = CircleOptions()
                            circleOptions.center(userLatLng)
                            circleOptions.radius(progressValue.toDouble())
                            circleOptions.strokeColor(R.color.Red_Blurred)
                            circleOptions.fillColor(R.color.Transparent_Red)
                            circleOptions.strokeWidth(R.integer.circle_stroke.toFloat())
                            mapCircle = gMap.addCircle(circleOptions)
                            gMap.animateCamera(CameraUpdateFactory.newLatLngBounds(getBounds(mapCircle),20))



                        }
                    })
                }
            }
        })

    }
    fun getBounds(circle: Circle) : LatLngBounds
    {
        val earthRadius = (6371 * 1000) //In meters
        val southWestLat = circle.center.latitude - (circle.radius / earthRadius) * (180/Math.PI)
        val southWestLng = circle.center.longitude - (circle.radius/earthRadius) * (180/Math.PI) / Math.cos(circle.center.latitude * Math.PI/180)
        val northEastLat = circle.center.latitude + (circle.radius / earthRadius) * (180/Math.PI)
        val northEastLng = circle.center.longitude + (circle.radius/earthRadius) * (180/Math.PI) / Math.cos(circle.center.latitude * Math.PI/180)
        val southWest = LatLng(southWestLat,southWestLng)
        val northEast = LatLng(northEastLat,northEastLng)
        val bounds = LatLngBounds(southWest,northEast)
        return bounds


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
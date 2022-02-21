package com.example.letseat

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentContainerView
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.letseat.databinding.ActivityMapsBinding
import com.google.android.gms.location.places.Place
import com.google.android.gms.location.places.Places
import java.util.jar.Manifest

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    // TODO: Fixa så¨att kartan fokuserar på användarens position 
  // lateinit var locationManager : LocationManager
   //lateinit var locationListener: LocationListener
   //lateinit var latLng: LatLng


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var locationManager : LocationManager





       val locationPermissionRequest = registerForActivityResult(
           ActivityResultContracts.RequestMultiplePermissions())
       {permissions ->
           when{
               permissions.getOrDefault(android.Manifest.permission.ACCESS_FINE_LOCATION, false) ->{
                   // Precise location granted
               }
               permissions.getOrDefault(android.Manifest.permission.ACCESS_COARSE_LOCATION, false) ->{
                   //Limited location granted
               }
               else ->{
                   //No location granted
               }

           }
       }
       // locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10,1,





        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)



        /*
        val leftIntent = Intent(this, MainActivity::class.java)
        var swipeListener : SwipeListener = SwipeListener()
        swipeListener.SwipeListener(mapContainer,leftIntent,false,this)
        */

        val listViewButton = findViewById<ImageButton>(R.id.listViewButton)
        listViewButton.setOnClickListener{
            val intent =Intent (this, MainActivity::class.java)
            startActivity(intent)
        }

        val accountButton = findViewById<ImageButton>(R.id.AccountButton)
        accountButton.setOnClickListener{
            val intent =Intent (this, AccountActivity::class.java)
            startActivity(intent)
        }

    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

      //  locationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager

        // TODO: Ändra stilen på pinsen så att det stämmer överens med temat 
        // Add a marker in Sydney and move the camera
        val sydney = LatLng(57.778394, 14.163911)
        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))

    }

}
package com.example.letseat

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.places.Place
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.tasks.OnSuccessListener
import com.google.android.gms.tasks.Task
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.FetchPlaceRequest
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// TODO: Ändra så att alla färger hämtas ifrån temat istället för de hårdkodade färgerna Samt fixa darkmode
class MainActivity : AppCompatActivity() {


	private lateinit var permissionsLauncher: ActivityResultLauncher<Array<String>>
	private lateinit var client: FusedLocationProviderClient
	private lateinit var userLatLng: LatLng
	private lateinit var debug: String
	private var fineLocationPermissionGranted = false
	private var coarseLocationPermissionGranted = false
	private var internetPermissionGranted = false


	var progressValue: Int = 0
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContentView(R.layout.activity_main)

		permissionsLauncher =
			registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
				fineLocationPermissionGranted =
					permissions[Manifest.permission.ACCESS_FINE_LOCATION]
						?: fineLocationPermissionGranted
				coarseLocationPermissionGranted =
					permissions[Manifest.permission.ACCESS_COARSE_LOCATION]
						?: coarseLocationPermissionGranted
				internetPermissionGranted =
					permissions[Manifest.permission.INTERNET] ?: internetPermissionGranted

			}
		requestPermissions()

		restaurantRepository.setContext(this)
		restaurantRepository.dropAllRestaurants()
		client = LocationServices.getFusedLocationProviderClient(this)
		Places.initialize(this, resources.getString(R.string.google_maps_key))
		RestaurantRepository().setContext(this)


		//Permission check
		if (ActivityCompat.checkSelfPermission(
				this,
				Manifest.permission.ACCESS_FINE_LOCATION
			) == PackageManager.PERMISSION_GRANTED
		) {
			getCurrentPosition()
// TODO: Update the position with a set interval
		} else {
			// When permission denied
			// Request permission
			ActivityCompat.requestPermissions(
				this,
				arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
				44
			)
		}


		val mapIntent = Intent(this, MapsActivity::class.java)


		val logInIntent = Intent(this, LoginActivity::class.java)
		val mapButton = findViewById<ImageButton>(R.id.mapButton)
		val distanceBar = findViewById<SeekBar>(R.id.distanceBar)
		val listView = findViewById<ListView>(R.id.restaurantView)
		val distanceView = findViewById<TextView>(R.id.distanceView)



		mapButton.setOnClickListener {
			mapIntent.putExtra("radius", progressValue)
			startActivity(mapIntent)
			finish()
		}


		val loginButton = findViewById<ImageButton>(R.id.logInButton)
		loginButton.setOnClickListener {
			val intent = Intent(this, LoginActivity::class.java)
			startActivity(intent)
			finish()
		}
		listView.adapter = restaurantRepository.addRestaurantsOnScreen()


		val restaurantItem = ArrayAdapter(
			this,
			android.R.layout.simple_list_item_1,
			android.R.id.text1,
			restaurantRepository.getAllRestaurants()
		)

		listView.setOnItemClickListener { parent, view, position, id ->
			val clickRestaurant = restaurantItem.getItem(position)
			val listId = clickRestaurant?.id

			val intent = Intent(this, RestaurantActivity::class.java)
			intent.putExtra("id", listId)
			startActivity(intent)
		}


			//Seekbar setup
			progressValue =
				intent.getIntExtra("radius", resources.getInteger(R.integer.standard_radius))


			distanceBar.max = resources.getInteger(R.integer.maximum_radius)
			distanceBar.progress = progressValue

			distanceBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
				override fun onProgressChanged(bar: SeekBar?, progress: Int, fromUser: Boolean) {
					distanceView.text = progress.toString() + "m"
					progressValue = progress
				}

				override fun onStartTrackingTouch(bar: SeekBar?) {
					// When user starts touching the bar do this
				}

				override fun onStopTrackingTouch(bar: SeekBar?) {
					restaurantRepository.dropAllRestaurants()
					updateRestaurantList()
					var jsonFetcher = JSONFetcher(
						"https://maps.googleapis.com/maps/api/place/textsearch/json?input=restaurant&inputtype=textquery&types=[%22restaurant%22,%22establishment%22]&locationbias=circle%3A" + progressValue + "%" + userLatLng.latitude + "%2C" + userLatLng.longitude +
								"&key=" + resources.getString(R.string.google_maps_key)
					)
					jsonFetcher.run() {
						updateRestaurantList()


					}

				}

			})



	}

		//////////////////////////THIS IS ALL SHIT
		@SuppressLint("MissingPermission")
		fun getCurrentPosition() {
			val task: Task<Location> = client.lastLocation


			task.addOnSuccessListener(object : OnSuccessListener<Location> {
				override fun onSuccess(location: Location?) {
					if (location != null) {
						//Location Success
						//init LatLng
						restaurantRepository.dropAllRestaurants()
						updateRestaurantList()
						userLatLng = LatLng(location.latitude, location.longitude)
						var jsonFetcher = JSONFetcher(
							"https://maps.googleapis.com/maps/api/place/textsearch/json?input=restaurant&inputtype=textquery&types=[%22restaurant%22,%22establishment%22]&locationbias=circle%3A" + progressValue + "%" + userLatLng.latitude + "%2C" + userLatLng.longitude +
									"&key=" + resources.getString(R.string.google_maps_key)
						)

						jsonFetcher.run() {
							updateRestaurantList()

						}


					}
				}
			})

		}


		//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		private fun requestPermissions() {
			coarseLocationPermissionGranted = ContextCompat.checkSelfPermission(
				this, android.Manifest.permission.ACCESS_COARSE_LOCATION
			) == PackageManager.PERMISSION_GRANTED

			fineLocationPermissionGranted = ContextCompat.checkSelfPermission(
				this,
				android.Manifest.permission.ACCESS_FINE_LOCATION
			) == PackageManager.PERMISSION_GRANTED

			internetPermissionGranted = ContextCompat.checkSelfPermission(
				this,
				android.Manifest.permission.INTERNET
			) == PackageManager.PERMISSION_GRANTED

			val permissionRequest: MutableList<String> = ArrayList()

			if (!coarseLocationPermissionGranted) {
				permissionRequest.add(android.Manifest.permission.ACCESS_COARSE_LOCATION)
			}
			if (!fineLocationPermissionGranted) {
				permissionRequest.add(android.Manifest.permission.ACCESS_FINE_LOCATION)
			}
			if (!internetPermissionGranted) {
				permissionRequest.add(android.Manifest.permission.INTERNET)
			}
			if (permissionRequest.isNotEmpty()) {
				permissionsLauncher.launch(permissionRequest.toTypedArray())
			}


		}

		fun updateRestaurantList() {
			//	restaurantRepository.cutOff(userLatLng,progressValue)
			val listView = findViewById<ListView>(R.id.restaurantView)
			listView.adapter = restaurantRepository.addRestaurantsOnScreen()
			listView.setOnItemClickListener { parent, view, position, id ->
				val intent = Intent(this, RestaurantActivity::class.java)
				intent.putExtra("id", position)
				startActivity(intent)
			}
		}



}

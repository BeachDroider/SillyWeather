package com.example.foad.sillyweather.ui.weather

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v4.view.GravityCompat
import android.util.Log
import android.view.MenuItem
import com.example.foad.sillyweather.R
import com.example.foad.sillyweather.constants.Constants
import com.example.foad.sillyweather.data.PickedCity
import com.example.foad.sillyweather.db.PickedCityDao
import com.example.foad.sillyweather.di.DaggerAppComponent
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.places.Place
import com.google.android.gms.location.places.ui.PlaceAutocomplete
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.async
import kotlinx.coroutines.experimental.launch
import javax.inject.Inject


class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var pickedCityDao: PickedCityDao

    private val PLACE_AUTOCOMPLETE_REQUEST_CODE = 1
    private val COARSE_LOCATION_PERMISSION_REQUEST_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.ic_menu)
        }

        DaggerAppComponent.builder().application(application).build().inject(this)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().add(R.id.container_weather, WeatherFragment.newInstance()).commit()
        }

        fab_add_city.setOnClickListener { openCityPicker() }

        populateNavDrawer()

        doCurrentLocation()

    }

    private fun populateNavDrawer() {
        nav_view.setNavigationItemSelectedListener {
            if (it.title.toString().equals(resources.getString(R.string.current_location)) && it.intent == null) {
                requestLocationPermission()
            } else {
                val pickedCity = it.intent.getParcelableExtra<PickedCity>(Constants.PICKED_CITY)
                onCitySelected(pickedCity)
            }
            layout_drawer.closeDrawers()
            true
        }

        nav_view.menu.add(R.string.current_location)
        launch {
            pickedCityDao.getPickedCities().forEach {
                addNavItem(it)
            }
        }
    }

    private fun doCurrentLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.getFusedLocationProviderClient(this).lastLocation.addOnSuccessListener {
                it?.let {
                    PickedCity(resources.getString(R.string.current_location), it.latitude, it.longitude).apply {
                        updateCurrentLocationNavItem(this)
                        onCitySelected(this)
                    }
                }
            }
        } else {
            requestLocationPermission()
        }
    }

    private fun requestLocationPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION),
                COARSE_LOCATION_PERMISSION_REQUEST_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            COARSE_LOCATION_PERMISSION_REQUEST_CODE -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    doCurrentLocation()
                }
            }
        }
    }

    private fun onCitySelected(city: PickedCity) {
        (supportFragmentManager.findFragmentById(R.id.container_weather) as WeatherFragment).loadWeather(city)
        supportActionBar?.title = city.name
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                layout_drawer.openDrawer(GravityCompat.START)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun openCityPicker() {
        try {
            PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).build(this).apply {
                startActivityForResult(this, PLACE_AUTOCOMPLETE_REQUEST_CODE)
            }
        } catch (e: Exception) {

        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                val place = PlaceAutocomplete.getPlace(this, data)
                onaddNewCity(place)
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {

            } else if (resultCode == Activity.RESULT_CANCELED) {

            }
        }
    }

    private fun onaddNewCity(place: Place) {
        val city = PickedCity(
                place.name.toString(),
                place.latLng.latitude,
                place.latLng.longitude
        )
        launch(UI) {
            async {
                pickedCityDao.insert(city)
            }.await()
            addNavItem(city)
        }
    }

    private fun addNavItem(city: PickedCity) {
        Intent().putExtra(Constants.PICKED_CITY, city).apply {
            nav_view.menu.add(city.name).intent = this
        }
    }

    private fun updateCurrentLocationNavItem(city: PickedCity) {
        Intent().putExtra(Constants.PICKED_CITY, city).apply {
            nav_view.menu.getItem(0).intent = Intent().putExtra(Constants.PICKED_CITY, city)
        }
    }

}


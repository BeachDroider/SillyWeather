package com.example.foad.sillyweather.ui.weather

import android.app.Activity
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.view.GravityCompat
import android.view.MenuItem
import com.example.foad.sillyweather.R
import com.example.foad.sillyweather.constants.Constants
import com.example.foad.sillyweather.data.PickedCity
import com.example.foad.sillyweather.db.PickedCityDao
import com.example.foad.sillyweather.di.DaggerAppComponent
import com.google.android.gms.location.places.Place
import com.google.android.gms.location.places.ui.PlaceAutocomplete
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.experimental.launch
import javax.inject.Inject


class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var pickedCityDao: PickedCityDao

    val PLACE_AUTOCOMPLETE_REQUEST_CODE = 1

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
        nav_view.setNavigationItemSelectedListener {
            val pickedCity = it.intent.getParcelableExtra<PickedCity>(Constants.PICKED_CITY)
            onCitySelected(pickedCity)
            layout_drawer.closeDrawers()
            true
        }

        fab_add_city.setOnClickListener { openCityPicker() }

        launch {
            pickedCityDao.getPickedCities().forEach {
                Intent().putExtra(Constants.PICKED_CITY, it).apply {
                    nav_view.menu.add(it.name).intent = this
                }
            }
        }
    }

    private fun onCitySelected(city: PickedCity) {
        (supportFragmentManager.findFragmentById(R.id.container_weather) as WeatherFragment).loadWeather(city)
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
                saveToDb(place)
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {

            } else if (resultCode == Activity.RESULT_CANCELED) {

            }
        }
    }

    private fun saveToDb(place: Place) {
        launch {
            with(place) {
                PickedCity(
                        name.toString(),
                        String.format("%.2f", latLng.latitude).toDouble(),
                        String.format("%.2f", latLng.longitude).toDouble()
                )
            }.apply {
                pickedCityDao.insert(this)
            }
        }

    }

}


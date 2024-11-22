package com.dicoding.picodiploma.loginwithanimation.view.maps

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.dicoding.picodiploma.loginwithanimation.R
import com.dicoding.picodiploma.loginwithanimation.data.response.ListStoryItem

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.dicoding.picodiploma.loginwithanimation.databinding.ActivityMapsBinding
import com.dicoding.picodiploma.loginwithanimation.view.ViewModelFactory
import com.dicoding.picodiploma.loginwithanimation.view.main.MainViewModel
import com.google.android.gms.maps.model.LatLngBounds

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private val viewModel by viewModels<MapsViewModel> {
        ViewModelFactory.getInstance(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)


        viewModel.storyResult.observe(this, Observer { storyList ->
            addMarkersToMap(storyList)
        })


        viewModel.isLoading.observe(this, Observer { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        })

        // Panggil fungsi untuk mendapatkan cerita dengan lokasi
        viewModel.getStoryWithLocation()
    }

    // Fungsi untuk menambahkan marker pada peta
    private fun addMarkersToMap(storyList: List<ListStoryItem>) {
        val builder = LatLngBounds.Builder()

        storyList.forEach { story ->
            val latitude = story.lat
            val longitude = story.lon

            if (latitude != null && longitude != null) {
                val location = LatLng(latitude, longitude)

                builder.include(location)

                mMap.addMarker(
                    MarkerOptions()
                        .position(location)
                        .title(story.name)
                        .snippet(story.description)// Bisa diganti dengan properti lain dari ListStoryItem
                )
                //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 10f)) // Zoom to marker
            }
        }
        val bounds = builder.build()
        val padding = 100
        val cameraUpdate = CameraUpdateFactory.newLatLngBounds(bounds, padding)
        mMap.animateCamera(cameraUpdate)

    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Centang kamera ke posisi default jika diperlukan
        //val defaultLocation = LatLng(-34.0, 151.0) // Koordinat default
        //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLocation, 10f))
    }
}
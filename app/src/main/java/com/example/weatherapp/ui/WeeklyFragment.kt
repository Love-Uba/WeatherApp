package com.example.weatherapp.ui

import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import com.google.android.gms.location.LocationRequest
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.weatherapp.data.wrapper.Result
import com.example.weatherapp.databinding.FragmentWeeklyBinding
import com.example.weatherapp.ui.adapter.WeatherForDaysAdapter
import com.example.weatherapp.utils.format
import com.example.weatherapp.utils.showcurrentTime
import com.example.weatherapp.utils.toString
import com.example.weatherapp.viewmodels.WeeklyViewModel
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WeeklyFragment : Fragment() {

    private lateinit var bnd: FragmentWeeklyBinding
    private val viewModel: WeeklyViewModel by viewModels()
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var lastLocation: Location
    private var lastLong = 0.00
    private var lastLat = 0.00
    private val weatherAdapter = WeatherForDaysAdapter()

    private lateinit var locationCallback: LocationCallback

    private lateinit var locationRequest: LocationRequest
    private var locationUpdateState = false

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
        private const val REQUEST_CHECK_SETTINGS = 2
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bnd = FragmentWeeklyBinding.inflate(layoutInflater, container, false)

        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())

        return bnd.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                super.onLocationResult(p0)
                lastLocation = p0.lastLocation
                lastLat = lastLocation.latitude.format(2).toDouble()
                lastLong = lastLocation.longitude.format(2).toDouble()
//                viewModel.actionSearch(lastLat, lastLong)
            }
        }
        createLocationRequest()
        setUpViews()
        setUpViewModels()
    }

    private val currentDate = showcurrentTime().toString("dd/MM/yyyy")


    private fun setUpViewModels() {
        viewModel.actionSearch(6.45,3.65)
        viewModel.weeklyFetchResponse.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Error -> {}
                is Result.Loading -> {
                    Toast.makeText(requireContext(), "Still Loading", Toast.LENGTH_SHORT).show()
                }
                is Result.Success -> {
                    bnd.cityNameTv.text = result.value.timezone.substringAfter("/")
                    bnd.weatherTv.text = result.value.status
                    bnd.tempTv.text = result.value.temp.toString()
//                    weatherAdapter.populatePredictions(result.value)

                    val icon = result.value.icon
                    Glide.with(requireContext())
                        .load("http://openweathermap.org/img/wn/$icon@2x.png")
                        .into(bnd.weatherIcon)

                }
            }
        }
    }

    private fun setUpViews() {
        bnd.daysRv.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = weatherAdapter
            setHasFixedSize(true)
        }
        bnd.dateTv.text = currentDate
    }

    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
            return
        }
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }

    private fun createLocationRequest() {
        locationRequest = LocationRequest()
        locationRequest.interval = 10000
        locationRequest.fastestInterval = 5000
        locationRequest.priority =
            PRIORITY_HIGH_ACCURACY
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
        val client = LocationServices.getSettingsClient(requireActivity())
        val task = client.checkLocationSettings(builder.build())
        task.addOnSuccessListener {
            locationUpdateState = true
            startLocationUpdates()
        }
        task.addOnFailureListener { e ->
            if (e is ResolvableApiException) {
                try {
                    e.startResolutionForResult(
                        requireActivity(),
                        REQUEST_CHECK_SETTINGS
                    )
                } catch (sendEx: IntentSender.SendIntentException) {
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            if (resultCode == Activity.RESULT_OK) {
                locationUpdateState = true
                startLocationUpdates()
            }
        }
    }

    override fun onPause() {
        super.onPause()
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }

    override fun onResume() {
        super.onResume()
        if (!locationUpdateState) {
            startLocationUpdates()
        }
    }

}
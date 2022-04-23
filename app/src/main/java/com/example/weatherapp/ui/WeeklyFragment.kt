package com.example.weatherapp.ui

import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.icu.math.MathContext.ROUND_HALF_EVEN
import android.location.Location
import android.location.LocationRequest
import android.os.Bundle
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.weatherapp.data.wrapper.Result
import com.example.weatherapp.databinding.FragmentWeeklyBinding
import com.example.weatherapp.viewmodels.WeeklyViewModel
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
import dagger.hilt.android.AndroidEntryPoint
import java.math.BigDecimal
import java.math.BigDecimal.ROUND_HALF_EVEN

@AndroidEntryPoint
class WeeklyFragment : Fragment() {

    private lateinit var bnd: FragmentWeeklyBinding
    private val viewModel: WeeklyViewModel by viewModels()
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var lastLocation: Location
    private var lastLong = 0.00
    private var lastLat = 0.00

    private lateinit var locationCallback: LocationCallback

    private lateinit var locationRequest: com.google.android.gms.location.LocationRequest
    private var locationUpdateState = false

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
        private const val REQUEST_CHECK_SETTINGS = 2
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        bnd = FragmentWeeklyBinding.inflate(layoutInflater, container, false)

        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                super.onLocationResult(p0)
                lastLocation = p0.lastLocation
                lastLat = lastLocation.latitude.format(2).toDouble()
                lastLong = lastLocation.longitude.format(2).toDouble()
                viewModel.actionSearch(lastLat, lastLong)
            }
        }
        createLocationRequest()
        Log.d("TraceLocation", "$lastLong $lastLat")
        return bnd.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        locationCallback = object : LocationCallback() {
//            override fun onLocationResult(p0: LocationResult) {
//                super.onLocationResult(p0)
//                lastLocation = p0.lastLocation
//                lastLat = lastLocation.latitude.format(2).toDouble()
//                lastLong = lastLocation.longitude.format(2).toDouble()
//                viewModel.actionSearch(lastLat, lastLong)
//            }
//        }
//        createLocationRequest()
        setUpViews()
        setUpViewModels()
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
            Looper.getMainLooper()/* Looper */
        )
    }

    private fun setUpViewModels() {
        viewModel.getAllSearchResponse.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Error -> {}
                is Result.Loading -> {}
                is Result.Success -> {
                    bnd.cityNameTv.text = result.value.timezone.substringAfter("/")
//                    Glide.with(requireContext())
//                        .load("http://openweathermap.org/img/wn/10d@2x.png")
//                        .into(bnd.weatherIcon)
//                    bnd.dateTv.text
//                    bnd.tempTv.text
//                    bnd.weatherTv.text = result.value

                }
            }
        }
    }

    private fun setUpViews() {
        Glide.with(requireContext())
            .load("http://openweathermap.org/img/wn/10d@2x.png")
            .into(bnd.weatherIcon)
    }

    private fun createLocationRequest() {
        // 1
        locationRequest = LocationRequest()
        // 2
        locationRequest.interval = 10000
        // 3
        locationRequest.fastestInterval = 5000
        locationRequest.priority =
            PRIORITY_HIGH_ACCURACY
        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)
        // 4
        val client = LocationServices.getSettingsClient(requireActivity())
        val task = client.checkLocationSettings(builder.build())
        // 5
        task.addOnSuccessListener {
            locationUpdateState = true
            startLocationUpdates()
        }
        task.addOnFailureListener { e ->
            // 6
            if (e is ResolvableApiException) {
                // Location settings are not satisfied, but this can be fixed
                // by showing the user a dialog.
                try {
                    // Show the dialog by calling startResolutionForResult(),
                    // and check the result in onActivityResult().
                    e.startResolutionForResult(
                        requireActivity(),
                        REQUEST_CHECK_SETTINGS
                    )
                } catch (sendEx: IntentSender.SendIntentException) {
                    // Ignore the error.
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

    // 3
    override fun onResume() {
        super.onResume()
        if (!locationUpdateState) {
            startLocationUpdates()
        }
        viewModel.actionSearch(lastLat, lastLong)
    }

    fun Double.format(digits: Int) = "%.${digits}f".format(this)


}
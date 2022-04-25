package com.example.weatherapp.ui

import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.fragment.app.activityViewModels
import com.example.weatherapp.R
import com.example.weatherapp.data.local.LocationData
import com.example.weatherapp.databinding.FragmentHomeBinding
import com.example.weatherapp.ui.HomeFragment.Companion.LOCATION_PERMISSION_REQUEST_CODE
import com.example.weatherapp.ui.HomeFragment.Companion.REQUEST_CHECK_SETTINGS
import com.example.weatherapp.utils.format
import com.example.weatherapp.viewmodels.WeeklyViewModel
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var bnd: FragmentHomeBinding
    private val sharedViewModel : WeeklyViewModel by activityViewModels()

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var lastLocation: Location
    private var lastLong = 0.00
    private var lastLat = 0.00

    private lateinit var locationCallback: LocationCallback

    private lateinit var locationRequest: LocationRequest
    private var locationUpdateState = false

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1
        private const val REQUEST_CHECK_SETTINGS = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        fusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(requireActivity())

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        bnd = FragmentHomeBinding.inflate(layoutInflater, container, false)
        setUpNav()
        return bnd.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(p0: LocationResult) {
                super.onLocationResult(p0)
                lastLocation = p0.lastLocation
                lastLat = lastLocation.latitude.format(2).toDouble()
                lastLong = lastLocation.longitude.format(2).toDouble()
                sharedViewModel.shareLocationData(LocationData(lastLat, lastLong))
            }
        }
        createLocationRequest()
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
        }else {
            fusedLocationProviderClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        }
    }

    private fun createLocationRequest() {
        locationRequest = LocationRequest()
        locationRequest.interval = 1000000
        locationRequest.fastestInterval = 500000
        locationRequest.priority =
            LocationRequest.PRIORITY_HIGH_ACCURACY
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
        sharedViewModel.shareLocationData(LocationData(lastLat,lastLong))
    }


    private fun setUpNav() {

        val todayFragment = TodayFragment()
        val weeklyFragment = WeeklyFragment()
        val shareFragment = ShareFragment()
        setCurrentFragment(todayFragment)

        bnd.bottomNav.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav_today -> setCurrentFragment(todayFragment)
                R.id.nav_week -> setCurrentFragment(weeklyFragment)
                R.id.nav_share -> setCurrentFragment(shareFragment)
                else -> setCurrentFragment(TodayFragment())
            }
            return@setOnItemSelectedListener true
        }
    }

    private fun setCurrentFragment(fragment: Fragment) =
        childFragmentManager.beginTransaction().apply {
            replace(R.id.nav_fragment_container, fragment)
                .commit()
        }


}
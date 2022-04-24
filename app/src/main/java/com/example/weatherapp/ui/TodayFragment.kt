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
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.weatherapp.data.wrapper.Result
import com.example.weatherapp.databinding.FragmentTodayBinding
import com.example.weatherapp.utils.*
import com.example.weatherapp.viewmodels.TodayViewModel
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TodayFragment : Fragment() {

    private lateinit var bnd: FragmentTodayBinding
    private val viewModel: TodayViewModel by activityViewModels()
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

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bnd = FragmentTodayBinding.inflate(layoutInflater, container, false)

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
                viewModel.actionSearch(lastLat, lastLong)
            }
        }
        createLocationRequest()
        setUpViews()
        setUpViewModels()
    }

    private fun setUpViews() {
        bnd.dateTv.text = currentDate
    }

    private val currentDate = showcurrentTime().toString("dd/MM/yyyy")

    private fun setUpViewModels() {
//        viewModel.actionSearch(6.45, 3.65)
        viewModel.todayFetchResponse.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Error -> {
                    Toast.makeText(requireContext(), "Failed to Fetch Weather Data", Toast.LENGTH_LONG).show()
                }
                is Result.Loading -> {
                    bnd.loadingView.setLoadingText("Loading Weather Data...")
                    bnd.loadingView.show()
                }
                is Result.Success -> {
                    bnd.loadingView.gone()
                    bnd.todayWrap.show()
                    bnd.cityNameTv.text = result.value.timezone.substringAfter("/")
                    bnd.tempTv.text = result.value.temp.toString()
                    bnd.humidityPercentTv.text = result.value.humidity.toString()
                    bnd.windSpeedTv.text = result.value.wind.toString()
                    bnd.feelsLikeTempTv.text = result.value.feelsLike.toString()
                    bnd.weatherTv.text = result.value.status
                    bnd.uvIndexTv.text = result.value.uvIndex.toString()
                    val icon = result.value.icon
                    Glide.with(requireContext())
                        .load("http://openweathermap.org/img/wn/$icon@2x.png")
                        .into(bnd.weatherIcon)
                }
            }
        }
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
    }

}
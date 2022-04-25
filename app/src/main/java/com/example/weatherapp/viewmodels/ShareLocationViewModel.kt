package com.example.weatherapp.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherapp.data.local.LocationData

class ShareLocationViewModel : ViewModel() {

    private val _locationData = MutableLiveData<LocationData>()

    val getLocationData: LiveData<LocationData> = _locationData

    fun shareLocationData(locationData: LocationData) {
        _locationData.value = locationData
    }



}
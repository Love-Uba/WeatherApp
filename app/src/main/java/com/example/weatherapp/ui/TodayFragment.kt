package com.example.weatherapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.weatherapp.data.wrapper.Result
import com.example.weatherapp.databinding.FragmentTodayBinding
import com.example.weatherapp.utils.*
import com.example.weatherapp.viewmodels.WeatherViewModel
import com.example.weatherapp.viewmodels.ShareLocationViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TodayFragment : Fragment() {

    private lateinit var bnd: FragmentTodayBinding
    private val viewModel: WeatherViewModel by activityViewModels()
    private val sharedViewModel: ShareLocationViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bnd = FragmentTodayBinding.inflate(layoutInflater, container, false)

        return bnd.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpViews()
        setUpViewModels()
    }

    private fun setUpViews() {
        bnd.dateTv.text = currentDate
    }

    private val currentDate = showcurrentTime().toString("dd/MM/yyyy")

    private fun setUpViewModels() {
        sharedViewModel.getLocationData.observe(viewLifecycleOwner) { result ->
            viewModel.actionSearch(result.lastLat, result.lastLong)
        }
        viewModel.todayFetchResponse.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Error -> {
                    Toast.makeText(
                        requireContext(),
                        "Failed to Fetch Weather Data",
                        Toast.LENGTH_LONG
                    ).show()
                }
                is Result.Loading -> {
                    bnd.loadingView.setLoadingText("Loading Weather Data...")
                    bnd.loadingView.show()
                }
                is Result.Success -> {
                    bnd.loadingView.gone()
                    bnd.todayWrap.show()
                    bnd.feelsLikeWrap.setTempItem(result.value.feelsLike.toString())
                    bnd.humidityWrap.setTempItem(result.value.humidity.toString())
                    bnd.windWrap.setTempItem(result.value.wind.toString())
                    bnd.uvWrap.setTempItem(result.value.uvIndex.toString())
                    bnd.cityNameTv.text = result.value.timezone.substringAfter("/")
                    bnd.tempTv.text = result.value.temp.toString()
                    bnd.weatherTv.text = result.value.status
                    val icon = result.value.icon
                    Glide.with(requireContext())
                        .load("http://openweathermap.org/img/wn/01d@2x.png")
                        .into(bnd.weatherIcon)
                }
            }
        }
    }

}
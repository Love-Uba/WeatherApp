package com.example.weatherapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.weatherapp.data.wrapper.Result
import com.example.weatherapp.databinding.FragmentWeeklyBinding
import com.example.weatherapp.ui.adapter.WeatherForDaysAdapter
import com.example.weatherapp.utils.gone
import com.example.weatherapp.utils.show
import com.example.weatherapp.utils.showcurrentTime
import com.example.weatherapp.utils.toString
import com.example.weatherapp.viewmodels.TodayViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WeeklyFragment : Fragment() {

    private lateinit var bnd: FragmentWeeklyBinding
    private val viewModel: TodayViewModel by activityViewModels()
    private val weatherAdapter = WeatherForDaysAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bnd = FragmentWeeklyBinding.inflate(layoutInflater, container, false)

        return bnd.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpViews()
        setUpViewModels()
    }

    private val currentDate = showcurrentTime().toString("dd/MM/yyyy")


    private fun setUpViewModels() {
        viewModel.getWeekReport()
        viewModel.todayFetchResponse.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Error -> {
                    Toast.makeText(requireContext(), "Failed to Fetch Forecast Data", Toast.LENGTH_LONG).show()
                }
                is Result.Loading -> {
                }
                is Result.Success -> {
                    bnd.cityNameTv.text = result.value.timezone.substringAfter("/")
                    bnd.weatherTv.text = result.value.status
                    bnd.tempTv.text = result.value.temp.toString()
                    val icon = result.value.icon
                    Glide.with(requireContext())
                        .load("http://openweathermap.org/img/wn/$icon@2x.png")
                        .into(bnd.weatherIcon)

                }
            }
        }
        viewModel.weeklyFetchResponse.observe(viewLifecycleOwner) { result ->
            when (result) {
                is Result.Error -> {}
                is Result.Loading -> {}
                is Result.Success -> {
                    weatherAdapter.populatePredictions(result.value)
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

}
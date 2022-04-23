package com.example.weatherapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.bumptech.glide.Glide
import com.example.weatherapp.data.wrapper.Result
import com.example.weatherapp.databinding.FragmentTodayBinding
import com.example.weatherapp.utils.toString
import com.example.weatherapp.viewmodels.TodayViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.*

@AndroidEntryPoint
class TodayFragment : Fragment() {

    private lateinit var bnd : FragmentTodayBinding

    private val viewModel : TodayViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        bnd = FragmentTodayBinding.inflate(layoutInflater, container, false)
        viewModel.actionSearch("Lagos")
        return bnd.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViews()
        setUpViewModels()
    }

    override fun onResume() {
        super.onResume()
        viewModel.actionSearch("Lagos")
    }

    private fun setUpViews(){
        bnd.dateTv.text = currentDate
    }

    fun showcurrentTime(): Date {
        return Calendar.getInstance().time
    }

    val date = showcurrentTime()
    val currentDate = date.toString("dd/MM/yyyy")

    private fun setUpViewModels(){
        viewModel.getSearchResponse.observe(viewLifecycleOwner) { result ->
            when(result){
                is Result.Error-> {}
                is Result.Loading-> {}
                is Result.Success-> {
                    bnd.cityNameTv.text = result.value.name
                    bnd.tempTv.text = result.value.main.temp.toString()
                    bnd.humidityPercentTv.text = result.value.main.humidity.toString()
                    bnd.windSpeedTv.text = result.value.wind.speed.toString()
                    bnd.feelsLikeTempTv.text = result.value.main.feels_like.toString()
                    for(items in result.value.weather){
                        bnd.weatherTv.text = items.main
                    }
                    bnd.uvIndexTv.text = result.value.clouds.all.toString()
                        Glide.with(requireContext())
                        .load("http://openweathermap.org/img/wn/10d@2x.png")
                        .into(bnd.weatherIcon)

                }

            }
        }
    }

}
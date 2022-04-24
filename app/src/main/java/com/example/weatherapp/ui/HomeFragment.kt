package com.example.weatherapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.weatherapp.R
import com.example.weatherapp.databinding.FragmentHomeBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private lateinit var bnd : FragmentHomeBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        bnd = FragmentHomeBinding.inflate(layoutInflater, container, false)
        setUpNav()
        return bnd.root
    }

    private fun setUpNav() {

        val todayFragment = TodayFragment()
        val weeklyFragment = WeeklyFragment()
        val shareFragment = ShareFragment()
        setCurrentFragment(todayFragment)

        bnd.bottomNav.setOnItemSelectedListener {
            when(it.itemId){
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
            replace(R.id.nav_fragment_container,fragment)
                .commit()
        }


}
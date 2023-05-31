package com.codegeniuses.estetikin.ui.splash

import android.content.Context
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.codegeniuses.estetikin.R
import com.codegeniuses.estetikin.databinding.FragmentSettingBinding
import com.codegeniuses.estetikin.databinding.FragmentSplashScreenBinding
import com.codegeniuses.estetikin.ui.MainActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class SplashScreenFragment : Fragment() {
    private lateinit var binding: FragmentSplashScreenBinding
    private var mainActivity: MainActivity? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is MainActivity) {
            mainActivity = context
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        @Suppress("DEPRECATION")
        Handler().postDelayed({
            findNavController().navigate(R.id.action_splashScreenFragment_to_onBoardingFragment)
        }, 3000)
        binding = FragmentSplashScreenBinding.inflate(inflater, container, false)

        mainActivity?.hideBottomNavigationView() // Call the method to hide bottom navigation view

        return binding.root
    }
}

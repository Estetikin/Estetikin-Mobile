package com.codegeniuses.estetikin.ui.onBoarding

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.codegeniuses.estetikin.R
import com.codegeniuses.estetikin.data.local.UserPreference
import com.codegeniuses.estetikin.databinding.OnBoardingItemBinding
import com.codegeniuses.estetikin.ui.MainActivity
import com.codegeniuses.estetikin.ui.authentication.AuthActivity
import com.codegeniuses.estetikin.ui.onBoarding.adapter.OnBoardingAdapter
import com.google.android.material.tabs.TabLayoutMediator

class OnBoardingActivity : AppCompatActivity() {
    private lateinit var binding: OnBoardingItemBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = OnBoardingItemBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        checkToken()
        setupAction()
    }

    private fun checkToken() {
        val pref = UserPreference(this)
        val token = pref.getToken()
        if (token != null) {
            navigateToMainActivity()
        }
    }

    private fun setupAction() {
        binding.viewPager2.adapter = OnBoardingAdapter()

        TabLayoutMediator(binding.tabLayout, binding.viewPager2) { tab, position ->
        }.attach()

        binding.viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                if (position == MAX_STEP - 1) {
                    binding.btnNext.text = getString(R.string.get_started_title)
                    binding.btnNext.contentDescription = getString(R.string.get_started_title)
                } else {
                    binding.btnNext.text = getString(R.string.next_title)
                    binding.btnNext.contentDescription = getString(R.string.next_title)
                }
            }
        })

        binding.btnSkip.setOnClickListener {
            navigateToLoginActivity()
        }

        binding.btnNext.setOnClickListener {
            if (binding.btnNext.text.toString() == getString(R.string.get_started_title)) {
                navigateToLoginActivity()
            } else {
                // to change current page - on click "Next BUTTON"
                val current = (binding.viewPager2.currentItem) + 1
                binding.viewPager2.currentItem = current

                // to update button text
                if (current >= MAX_STEP - 1) {
                    binding.btnNext.text = getString(R.string.get_started_title)
                    binding.btnNext.contentDescription = getString(R.string.get_started_title)
                } else {
                    binding.btnNext.text = getString(R.string.next_title)
                    binding.btnNext.contentDescription = getString(R.string.next_title)
                }
            }
        }
    }

    private fun navigateToLoginActivity() {
        // TODO update this if to loginactivity if the login logic already finish
        val intent = Intent(this@OnBoardingActivity, AuthActivity::class.java)
        intent.flags =
            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    private fun navigateToMainActivity() {
        // TODO update this if to loginactivity if the login logic already finish
        val intent = Intent(this@OnBoardingActivity, MainActivity::class.java)
        intent.flags =
            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    companion object {
        const val MAX_STEP = 2
    }
}
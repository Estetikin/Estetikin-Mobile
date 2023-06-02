package com.codegeniuses.estetikin.ui.onBoarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.codegeniuses.estetikin.R
import com.codegeniuses.estetikin.databinding.ItemOnBoardingBinding
import com.codegeniuses.estetikin.ui.MainActivity
import com.codegeniuses.estetikin.ui.onBoarding.adapter.OnBoardingAdapter
import com.google.android.material.tabs.TabLayoutMediator

class OnBoardingFragment : Fragment() {
    private var _binding: ItemOnBoardingBinding? = null
    private val binding get() = _binding!!

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = ItemOnBoardingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.hide()

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
            navigateToHomeFragment()
        }

        binding.btnNext.setOnClickListener {
            if (binding.btnNext.text.toString() == getString(R.string.get_started_title)) {
                navigateToHomeFragment()
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

    private fun navigateToHomeFragment() {
        //TODO update this if to loginactivity if the login logic already finish
        val action = R.id.action_onBoardingFragment_to_homeFragment
        findNavController().navigate(action)
        showBottomNavigationView()
    }

    private fun showBottomNavigationView() {
        val mainActivity = activity as MainActivity
        mainActivity.showBottomNavigationView()
    }

    companion object {
        const val MAX_STEP = 2
    }
}

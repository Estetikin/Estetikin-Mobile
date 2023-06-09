package com.codegeniuses.estetikin.ui.setting

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.codegeniuses.estetikin.R
import com.codegeniuses.estetikin.data.local.UserPreference
import com.codegeniuses.estetikin.databinding.FragmentSettingBinding
import com.codegeniuses.estetikin.ui.MainActivity
import com.codegeniuses.estetikin.ui.authentication.AuthActivity

class SettingFragment : Fragment() {

    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!

    private val settingViewModel: SettingViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        (activity as? MainActivity)?.setActionBarTitle(getString(R.string.title_setting))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupAction()
    }

    private fun setupAction() {
        binding.logoutItem.setOnClickListener {
            logout()
            val intent = Intent(requireContext(), AuthActivity::class.java)
            intent.flags =
                Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
    }

    private fun logout() {
        val pref = UserPreference(requireContext())
        pref.clearToken()
    }
}

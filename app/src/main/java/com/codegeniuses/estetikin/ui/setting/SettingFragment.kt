package com.codegeniuses.estetikin.ui.setting

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.codegeniuses.estetikin.R
import com.codegeniuses.estetikin.databinding.FragmentSettingBinding
import java.util.*

class SettingFragment : Fragment() {

    private lateinit var binding: FragmentSettingBinding
    private val settingViewModel: SettingViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingBinding.inflate(inflater, container, false)

        val language = binding.languageItem
        language.setOnClickListener{
            showBottomSheet()
        }

        return binding.root
    }

    private fun showBottomSheet() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.item_bottom_language)

        val english = dialog.findViewById<TextView>(R.id.english)
        val indonesian = dialog.findViewById<TextView>(R.id.bahasa_indonesia)

        english.setOnClickListener {
            changeLanguage("en")
            dialog.dismiss()
            Toast.makeText(requireContext(), "English is Clicked", Toast.LENGTH_SHORT).show()
        }

        indonesian.setOnClickListener {
            changeLanguage("id")
            dialog.dismiss()
            Toast.makeText(requireContext(), "Bahasa Indonesia is Clicked", Toast.LENGTH_SHORT).show()
        }

        dialog.show()
        dialog.window?.apply {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            attributes?.windowAnimations = R.style.Bottom_Sheet_Animation
            setGravity(Gravity.BOTTOM)
        }
    }

    private fun changeLanguage(languageCode: String) {
        val locale = Locale(languageCode)
        val configuration = resources.configuration
        configuration.setLocale(locale)
        resources.updateConfiguration(configuration, resources.displayMetrics)
        requireActivity().recreate()
    }
}


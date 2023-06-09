package com.codegeniuses.estetikin.ui.setting

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.codegeniuses.estetikin.R
import com.codegeniuses.estetikin.databinding.FragmentSettingBinding
import java.util.*

class SettingFragment : Fragment() {


    private lateinit var binding: FragmentSettingBinding
    private lateinit var tvUiSelected: TextView  // Reference to tvUiSelected in the layout
    private val settingViewModel: SettingViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingBinding.inflate(inflater, container, false)

        val sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val languageCode = sharedPreferences.getString("language", "en") // Default to English if not found

        // Update the text of tv_title_language based on the saved language
        val languageTextResId = if (languageCode == "en") {
            R.string.english // English
        } else {
            R.string.bahasa_indonesia // Bahasa Indonesia
        }
        binding.tvLanguageSelected.text = getString(languageTextResId)

        val language = binding.languageItem
        language.setOnClickListener {
            showLanguageBottomSheet()
        }

        val uiMode = binding.uiItem
        tvUiSelected = binding.tvUiSelected  // Assign the reference to tvUiSelected
        uiMode.setOnClickListener {
            showUiBottomSheet()
        }

        // Update the UI mode text initially
        checkUiSelected()

        return binding.root
    }


    private fun showLanguageBottomSheet() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.item_bottom_language)

        val english = dialog.findViewById<TextView>(R.id.english)
        val indonesian = dialog.findViewById<TextView>(R.id.bahasa_indonesia)

        english.setOnClickListener {
            changeLanguage("en")
            binding.tvLanguageSelected
            dialog.dismiss()
            Toast.makeText(requireContext(), "English is Clicked", Toast.LENGTH_SHORT).show()
        }

        indonesian.setOnClickListener {
            changeLanguage("id")
            dialog.dismiss()
            Toast.makeText(requireContext(), "Bahasa Indonesia is Clicked", Toast.LENGTH_SHORT)
                .show()
        }

        dialog.show()
        dialog.window?.apply {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            attributes?.windowAnimations = R.style.Bottom_Sheet_Animation
            setGravity(Gravity.BOTTOM)
        }
    }

    private fun showUiBottomSheet() {
        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.item_ui_mode)

        val light = dialog.findViewById<TextView>(R.id.light_mode)
        val dark = dialog.findViewById<TextView>(R.id.dark_mode)

        light.setOnClickListener {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            binding.ivUi.setImageResource(R.drawable.ic_light_mode)
            binding.ivLanguage.setImageResource(R.drawable.ic_language)
            binding.ivLogout.setImageResource(R.drawable.ic_logout)
            tvUiSelected.setText(R.string.light_mode)  // Use the stored reference to update tvUiSelected
            dialog.dismiss()
            Toast.makeText(requireContext(), "Light Mode is Clicked", Toast.LENGTH_SHORT).show()
        }

        dark.setOnClickListener {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            binding.ivUi.setImageResource(R.drawable.ic_dark_mode)
            binding.ivLanguage.setImageResource(R.drawable.ic_language_light)
            binding.ivLogout.setImageResource(R.drawable.ic_logout_light)
            tvUiSelected.setText(R.string.dark_mode)  // Use the stored reference to update tvUiSelected
            dialog.dismiss()
            Toast.makeText(requireContext(), "Dark Mode is Clicked", Toast.LENGTH_SHORT).show()
        }

        dialog.window?.apply {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
            setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            attributes?.windowAnimations = R.style.Bottom_Sheet_Animation
            setGravity(Gravity.BOTTOM)
        }

        dialog.show()
    }

    private fun checkUiSelected() {
        // Update the UI Mode Text based on the UI mode
        val nightMode = AppCompatDelegate.getDefaultNightMode()
        val uiModeTextResId = if (nightMode == AppCompatDelegate.MODE_NIGHT_YES) {
            R.string.dark_mode
        } else {
            R.string.light_mode
        }
        tvUiSelected.setText(uiModeTextResId)

        // Update the UI Mode icons based on the UI mode
        val uiModeIconResId = if (nightMode == AppCompatDelegate.MODE_NIGHT_YES) {
            R.drawable.ic_dark_mode // Dark mode icon
        } else {
            R.drawable.ic_light_mode // Light mode icon
        }
        binding.ivUi.setImageResource(uiModeIconResId)

        // Update the Language icons based on the UI mode
        val languageIconResId = if (nightMode == AppCompatDelegate.MODE_NIGHT_YES) {
            R.drawable.ic_language_light // Dark mode icon
        } else {
            R.drawable.ic_language // Light mode icon
        }
        binding.ivLanguage.setImageResource(languageIconResId)

        // Update the Logout icons based on the UI mode
        val logoutIconResId = if (nightMode == AppCompatDelegate.MODE_NIGHT_YES) {
            R.drawable.ic_logout_light // Dark mode icon
        } else {
            R.drawable.ic_logout // Light mode icon
        }
        binding.ivLogout.setImageResource(logoutIconResId)
    }




    private fun changeLanguage(languageCode: String) {
        val sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("language", languageCode)
        editor.apply()

        val locale = Locale(languageCode)
        val configuration = resources.configuration
        configuration.setLocale(locale)
        resources.updateConfiguration(configuration, resources.displayMetrics)

        // Update the text and background of btnLanguageSelected based on the selected language
        val languageTextResId = if (languageCode == "en") {
            R.string.english // English
        } else {
            R.string.bahasa_indonesia // Bahasa Indonesia
        }
        binding.tvLanguageSelected.setText(languageTextResId)


        requireActivity().recreate()
    }

}


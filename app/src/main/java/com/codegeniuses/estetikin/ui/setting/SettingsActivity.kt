package com.codegeniuses.estetikin.ui.setting

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.codegeniuses.estetikin.R
import com.codegeniuses.estetikin.data.local.UserPreference
import com.codegeniuses.estetikin.databinding.ActivitySettingsBinding
import com.codegeniuses.estetikin.ui.authentication.AuthActivity
import java.util.*

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySettingsBinding
    private lateinit var tvUiSelected: TextView
    private val settingViewModel: SettingViewModel by viewModels()
    private lateinit var preferences: UserPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        setupAction()

        binding.ivBackButton.setOnClickListener {
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        supportActionBar?.title = getString(R.string.title_setting)
    }

    private fun setupAction() {
        preferences = UserPreference(this)

        val sharedPreferences =
            getSharedPreferences("settings", Context.MODE_PRIVATE)
        val languageCode = sharedPreferences.getString("language", "en") // Default to English if not found
        // Update the text of tv_title_language based on the saved language
        val languageTextResId = if (languageCode == "en") {
            R.string.english
        } else {
            R.string.bahasa_indonesia
        }

        binding.tvLanguageSelected.text = getString(languageTextResId)


        val language = binding.languageItem
        language.setOnClickListener {
            showLanguageBottomSheet()
        }

        val uiMode = binding.uiItem
        tvUiSelected = binding.tvUiSelected
        uiMode.setOnClickListener {
            showUiBottomSheet()
        }
        checkUiSelected()

        binding.logoutItem.setOnClickListener {
            val pref = UserPreference(this)
            pref.clearPreferences()
            navigateToLogin()
        }
    }

    private fun navigateToLogin() {
        val intent = Intent(this, AuthActivity::class.java)
        intent.flags =
            Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
    }

    private fun showLanguageBottomSheet() {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.item_bottom_language)

        val english = dialog.findViewById<TextView>(R.id.english)
        val indonesian = dialog.findViewById<TextView>(R.id.bahasa_indonesia)

        english.setOnClickListener {
            changeLanguage("en")
            binding.tvLanguageSelected
            dialog.dismiss()
            preferences.setLanguage("en")
            Toast.makeText(this, "English is Clicked", Toast.LENGTH_SHORT).show()
        }

        indonesian.setOnClickListener {
            changeLanguage("id")
            dialog.dismiss()
            preferences.setLanguage("id")
            Toast.makeText(this, "Bahasa Indonesia is Clicked", Toast.LENGTH_SHORT)
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
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.item_ui_mode)

        val light = dialog.findViewById<TextView>(R.id.light_mode)
        val dark = dialog.findViewById<TextView>(R.id.dark_mode)

        light.setOnClickListener {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            binding.ivUi.setImageResource(R.drawable.ic_light_mode)
            binding.ivLanguage.setImageResource(R.drawable.ic_language)
            binding.ivLogout.setImageResource(R.drawable.ic_logout)
            tvUiSelected.setText(R.string.light_theme)  // Use the stored reference to update tvUiSelected
            dialog.dismiss()
            preferences.setTheme("Light")
            Toast.makeText(this, "Light Mode is Clicked", Toast.LENGTH_SHORT).show()
        }

        dark.setOnClickListener {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            binding.ivUi.setImageResource(R.drawable.ic_dark_mode)
            binding.ivLanguage.setImageResource(R.drawable.ic_language_light)
            binding.ivLogout.setImageResource(R.drawable.ic_logout_light)
            tvUiSelected.setText(R.string.dark_theme)  // Use the stored reference to update tvUiSelected
            dialog.dismiss()
            preferences.setTheme("Dark")
            Toast.makeText(this, "Dark Mode is Clicked", Toast.LENGTH_SHORT).show()
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
        val nightMode = AppCompatDelegate.getDefaultNightMode()
        val uiModeTextResId = if (nightMode == AppCompatDelegate.MODE_NIGHT_YES) {
            R.string.dark_theme
        } else {
            R.string.light_theme
        }
        tvUiSelected.setText(uiModeTextResId)


        val uiModeIconResId = if (nightMode == AppCompatDelegate.MODE_NIGHT_YES) {
            R.drawable.ic_dark_mode
        } else {
            R.drawable.ic_light_mode
        }
        binding.ivUi.setImageResource(uiModeIconResId)


        val languageIconResId = if (nightMode == AppCompatDelegate.MODE_NIGHT_YES) {
            R.drawable.ic_language_light
        } else {
            R.drawable.ic_language
        }
        binding.ivLanguage.setImageResource(languageIconResId)


        val logoutIconResId = if (nightMode == AppCompatDelegate.MODE_NIGHT_YES) {
            R.drawable.ic_logout_light
        } else {
            R.drawable.ic_logout
        }
        binding.ivLogout.setImageResource(logoutIconResId)
    }

    private fun changeLanguage(languageCode: String) {
        val sharedPreferences =
            getSharedPreferences("settings", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("language", languageCode)
        editor.apply()

        val locale = Locale(languageCode)
        val configuration = resources.configuration
        configuration.setLocale(locale)
        resources.updateConfiguration(configuration, resources.displayMetrics)

        // Update the text and background of btnLanguageSelected based on the selected language
        val languageTextResId = if (languageCode == "en") {
            R.string.english
        } else {
            R.string.bahasa_indonesia
        }
        binding.tvLanguageSelected.setText(languageTextResId)


        recreate()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    fun getPreviousFragmentTag(): String {
        return ""
    }
}

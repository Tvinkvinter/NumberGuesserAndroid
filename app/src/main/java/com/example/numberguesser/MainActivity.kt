package com.example.numberguesser

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.GravityCompat
import com.example.numberguesser.databinding.ActivityMainBinding
import com.google.android.material.switchmaterial.SwitchMaterial

class MainActivity : AppCompatActivity(){

    private lateinit var binding: ActivityMainBinding
    lateinit var preferences: SharedPreferences
    private lateinit var options: Options


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also { setContentView(it.root) }
        preferences = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
        options = savedInstanceState?.getParcelable(KEY_OPTIONS) ?: Options.DEFAULT
        binding.openMenuButton.setOnClickListener {
            binding.drawer.openDrawer(GravityCompat.END)
            //menuButtonAnim = AnimationUtils.loadAnimation(applicationContext, R.anim.move_left)
            //it.startAnimation(menuButtonAnim)
        }

        setMenu()
        restoreFromSharedPreferences()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(KEY_OPTIONS, options)
    }

    fun restoreFromSharedPreferences() {
        if (preferences.getBoolean(PREF_DARK_MODE, false)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        options.maxNumber =
            preferences.getFloat(PREF_SLIDER_VAL, options.maxNumber.toFloat()).toInt()
    }

    private fun setMenu() {
        val switchView =
            binding.settingsMenu.menu.findItem(R.id.switch_item).actionView as SwitchMaterial

        switchView.isChecked = preferences.getBoolean(PREF_DARK_MODE, false)
        switchView.setOnCheckedChangeListener { _, b ->
            if (b) {
                preferences.edit().putBoolean(PREF_DARK_MODE, true).apply()
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                preferences.edit().putBoolean(PREF_DARK_MODE, false).apply()
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    companion object {
        @JvmStatic
        private val KEY_OPTIONS = "OPTIONS"

        @JvmStatic
        private val APP_PREFERENCES = "APP_PREFERENCES"

        @JvmStatic
        private val PREF_DARK_MODE = "PREF_DARK_MODE"

        @JvmStatic
        val PREF_SLIDER_VAL = "PREF_SLIDER_VAL"
    }
}
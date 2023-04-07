package com.example.numberguesser

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.SimpleAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.GravityCompat
import com.example.numberguesser.databinding.ActivityMainBinding
import com.google.android.material.switchmaterial.SwitchMaterial

class MainActivity : AppCompatActivity() {

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

        val spinner = binding.settingsMenu.menu.findItem(R.id.spinner_item).actionView as Spinner
        val data = listOf(
            mapOf(
                KEY_TITLE to getString(R.string.eng_lang),
                KEY_ICON to R.drawable.eng_lang_icon
            ),
            mapOf(
                KEY_TITLE to getString(R.string.es_lang),
                KEY_ICON to R.drawable.es_lang_icon
            ),
            mapOf(
                KEY_TITLE to getString(R.string.rus_lang),
                KEY_ICON to R.drawable.ru_lang_icon
            ),
            mapOf(
                KEY_TITLE to getString(R.string.zh_lang),
                KEY_ICON to R.drawable.zh_lang_icon
            )
        )


        val adapter = SimpleAdapter(
            this,
            data,
            R.layout.spinner_row,
            arrayOf(KEY_TITLE, KEY_ICON),
            intArrayOf(R.id.text_lang, R.id.icon_lang)
        )
        spinner.adapter = adapter
    }

    companion object {
        private const val KEY_OPTIONS = "OPTIONS"
        private const val APP_PREFERENCES = "APP_PREFERENCES"
        private const val PREF_DARK_MODE = "PREF_DARK_MODE"
        const val PREF_SLIDER_VAL = "PREF_SLIDER_VAL"
        private const val KEY_TITLE = "LANGUAGE"
        private const val KEY_ICON = "ICON"
    }
}
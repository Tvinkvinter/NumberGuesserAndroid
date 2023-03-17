package com.example.numberguesser

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.Gravity
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.example.numberguesser.contract.Navigator
import com.example.numberguesser.databinding.ActivityMainBinding
import com.google.android.material.slider.Slider
import com.google.android.material.switchmaterial.SwitchMaterial

class MainActivity : AppCompatActivity(), Navigator {

    private lateinit var binding: ActivityMainBinding

    private lateinit var preferences: SharedPreferences

    private lateinit var options: Options

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also { setContentView(it.root) }

        options = savedInstanceState?.getParcelable(KEY_OPTIONS) ?: Options.DEFAULT
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container, MainFragment.newInstance(options), "Main")
                .commit()
        }

        setHeader()

        binding.menuIcon.setOnClickListener {
            binding.drawer.openDrawer(GravityCompat.END)
        }

        preferences = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE)
        setMenu()
        restoreFromSharedPreferences()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(KEY_OPTIONS, options)
    }

    fun restoreFromSharedPreferences(){
        if (preferences.getBoolean(PREF_DARK_MODE, false)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        options.maxNumber = preferences.getFloat(PREF_SLIDER_VAL, options.maxNumber.toFloat()).toInt()
    }

    private fun setHeader() {
        val spannable = SpannableStringBuilder(getString(R.string.app_name))
        spannable.setSpan(
            ForegroundColorSpan(getColor(R.color.capital_letter_color)),
            0, 1, Spannable.SPAN_EXCLUSIVE_INCLUSIVE
        )
        spannable.setSpan(
            ForegroundColorSpan(getColor(R.color.capital_letter_color)),
            6, 7, Spannable.SPAN_EXCLUSIVE_INCLUSIVE
        )
        val header: TextView = findViewById(R.id.headerText)
        header.text = spannable
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

        val rangeText =
            binding.settingsMenu.menu.findItem(R.id.text_range_item).actionView as TextView
        rangeText.gravity = Gravity.CENTER_VERTICAL

        val sliderView = binding.settingsMenu.menu.findItem(R.id.slider_item).actionView as Slider
        with(sliderView) {
            valueFrom = 100.0F
            value = preferences.getFloat(PREF_SLIDER_VAL, 1000F)
            valueTo = 10000.0F
            stepSize = 100.0F
            isTickVisible = false
        }

        rangeText.text = getString(R.string.menu_game_range_val, sliderView.value.toInt())

//        sliderView.addOnChangeListener { _, value, _ ->
//            rangeText.text = getString(R.string.menu_game_range_val, value.toInt())
//            options.maxNumber = value.toInt()
//            (supportFragmentManager.findFragmentByTag("Main") as MainFragment).updateInstructions()
//
//        }
        sliderView.addOnChangeListener { _, value, _ ->
            preferences.edit().putFloat(PREF_SLIDER_VAL, value).apply()
            rangeText.text = getString(R.string.menu_game_range_val, value.toInt())
            options.maxNumber = value.toInt()
            (supportFragmentManager.findFragmentByTag("Main") as MainFragment).updateInstructions()
        }
    }

    fun hideRangeItem() {
        binding.settingsMenu.menu.findItem(R.id.text_range_item).isVisible = false
        binding.settingsMenu.menu.findItem(R.id.slider_item).isVisible = false
    }

    fun showRangeItem() {
        binding.settingsMenu.menu.findItem(R.id.text_range_item).isVisible = true
        binding.settingsMenu.menu.findItem(R.id.slider_item).isVisible = true
    }

    override fun showMainScreen() {
        launchFragment(MainFragment.newInstance(options))
    }

    override fun showGameScreen() {
        options.tryNumber = 1
        options.curNumber = options.maxNumber / 2
        launchFragment(GameFragment.newInstance(options))
    }

    override fun showFinishScreen(options: Options) {
        launchFragment(FinishFragment.newInstance(options))
    }

    private fun launchFragment(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            .addToBackStack(null)
            .replace(R.id.fragment_container, fragment)
            .commit()
    }

    companion object {
        @JvmStatic
        private val KEY_OPTIONS = "OPTIONS"

        @JvmStatic
        private val APP_PREFERENCES = "APP_PREFERENCES"

        @JvmStatic
        private val PREF_DARK_MODE = "PREF_DARK_MODE"

        @JvmStatic
        private val PREF_SLIDER_VAL = "PREF_SLIDER_VAL"
    }
}
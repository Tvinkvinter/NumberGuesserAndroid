package com.example.numberguesser

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.Gravity
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import com.example.numberguesser.contract.Navigator
import com.example.numberguesser.databinding.ActivityMainBinding
import com.google.android.material.slider.Slider

class MainActivity : AppCompatActivity(), Navigator {

    private lateinit var binding: ActivityMainBinding

    private lateinit var options: Options

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also { setContentView(it.root) }

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container, MainFragment())
                .commit()
        }
        options = savedInstanceState?.getParcelable(KEY_OPTIONS) ?: Options.DEFAULT

        setHeader()

        binding.menuIcon.setOnClickListener {
            binding.drawer.openDrawer(GravityCompat.END)
        }

        setMenu()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(KEY_OPTIONS, options)
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
        val rangeText =
            binding.settingsMenu.menu.findItem(R.id.text_range_item).actionView as TextView
        rangeText.gravity = Gravity.CENTER_VERTICAL

        val sliderView = binding.settingsMenu.menu.findItem(R.id.slider_item).actionView as Slider
        with(sliderView) {
            valueFrom = 0.0F
            value = 1000.0F
            valueTo = 10000.0F
            stepSize = 100.0F

        }

        rangeText.text = getString(R.string.menu_game_range_val, sliderView.value.toInt())

        sliderView.addOnChangeListener { _, value, _ ->
            rangeText.text = getString(R.string.menu_game_range_val, value.toInt())
        }
    }

    override fun showMainScreen() {
        launchFragment(MainFragment())
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
    }
}
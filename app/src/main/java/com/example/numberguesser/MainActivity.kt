package com.example.numberguesser

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.view.Gravity
import android.widget.TextView
import androidx.core.view.GravityCompat
import com.example.numberguesser.databinding.ActivityMainBinding
import com.google.android.material.slider.Slider

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater).also { setContentView(it.root) }

        setHeader()
        setInstructions()

        binding.menuIcon.setOnClickListener { binding.drawer.openDrawer(GravityCompat.END) }

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

        binding.startButton.setOnClickListener { onStartPressed() }

    }

    private fun onStartPressed() {
        val intent = Intent(this, GameActivity::class.java)
        startActivity(intent)
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

    private fun setInstructions() {
        val spannable = SpannableStringBuilder(getString(R.string.instruction_1))
        spannable.setSpan(
            AbsoluteSizeSpan(22, true),
            17, 18, Spannable.SPAN_EXCLUSIVE_INCLUSIVE
        )

        spannable.setSpan(
            AbsoluteSizeSpan(22, true),
            22, 26, Spannable.SPAN_EXCLUSIVE_INCLUSIVE
        )
        binding.instructionText1.text = spannable
    }
}
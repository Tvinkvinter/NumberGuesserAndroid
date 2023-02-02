package com.example.numberguesser

import android.content.Intent
import android.graphics.Color
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import android.webkit.WebSettings
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import kotlin.math.pow

class MainActivity : AppCompatActivity() {

    lateinit var startButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setHeader()
        setInstructions()

        startButton = findViewById(R.id.startButton)
        startButton.setOnClickListener { onStartPressed() }

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
        findViewById<TextView>(R.id.instruction_text_1).text = spannable
    }
}
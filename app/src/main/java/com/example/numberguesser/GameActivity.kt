package com.example.numberguesser

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.TextView
import com.example.numberguesser.databinding.ActivityGameBinding
import kotlin.math.pow

class GameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGameBinding
    var curTryNumber = 1
    var limitPow = 10
    var curNumberVal = 2.0.pow(limitPow).toInt()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater).also { setContentView(it.root) }

        setHeader()

        binding.lessButton.setOnClickListener { onClickButtons(it) }
        binding.moreButton.setOnClickListener { onClickButtons(it) }
        binding.finishButton.setOnClickListener { onClickFinish() }


        if (savedInstanceState == null) {
            curNumberVal = binarySearch(false)

        } else {
            curTryNumber = savedInstanceState.getInt(KEY_ATTEMPT)
            curNumberVal = savedInstanceState.getInt(KEY_NUMBER)
        }
        val newText =
            applicationContext.resources.getString(R.string.try_number) + " " + curTryNumber.toString()
        binding.tryNumberText.text = newText
        binding.number.text = curNumberVal.toString()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_ATTEMPT, curTryNumber)
        outState.putInt(KEY_NUMBER, curNumberVal)
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

    private fun onClickButtons(v: View) {
        curTryNumber++
        when (v.id) {
            R.id.less_button -> curNumberVal = binarySearch(false)
            R.id.more_button -> curNumberVal = binarySearch(true)
        }
        val newText =
            applicationContext.resources.getString(R.string.try_number) + " " + curTryNumber.toString()
        binding.tryNumberText.text = newText
        binding.number.text = curNumberVal.toString()
        if (curTryNumber >= 10) onClickFinish()
    }

    private fun onClickFinish() {
        val intent = Intent(this, FinishActivity::class.java)
        intent.putExtra(FinishActivity.KEY_ATTEMPT, curTryNumber)
        intent.putExtra(FinishActivity.KEY_NUMBER, curNumberVal)
        startActivity(intent)
        if (curTryNumber >= 10) finish()
    }


    private fun binarySearch(nextIsMore: Boolean): Int {
        var nextNumber = 0.0
        nextNumber += if (nextIsMore) {
            curNumberVal + 2.0.pow(limitPow - curTryNumber)
        } else {
            curNumberVal - 2.0.pow(limitPow - curTryNumber)
        }
        return nextNumber.toInt()
    }

    companion object {
        @JvmStatic
        private val KEY_ATTEMPT = "ATTEMPT"
        @JvmStatic
        private val KEY_NUMBER = "NUMBER"
    }
}
package com.example.numberguesser

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import com.example.numberguesser.databinding.ActivityFinishBinding

class FinishActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFinishBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFinishBinding.inflate(layoutInflater).also { setContentView(it.root) }

        setHeader()
        if(Math.random() > 0.5)
        binding.background.setImageDrawable(AppCompatResources.getDrawable(this, R.drawable.bg_finish_digits))

        binding.againButton.setOnClickListener { onClickAgain() }
        binding.instructionButton.setOnClickListener { onClickInstruction() }


        val curTryNumber = intent.getIntExtra(KEY_ATTEMPT, -1)
        val curNumberVal = intent.getIntExtra(KEY_NUMBER, -1)

        binding.guessedNumberText.text = curNumberVal.toString()
        binding.attemptsSpentText.text =
            binding.attemptsSpentText.text.replace(Regex("n"), curTryNumber.toString())

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

    private fun onClickAgain() {
        val intent = Intent(this, GameActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startActivity(intent)
    }

    private fun onClickInstruction() {
        val intent = Intent(this, MainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
        startActivity(intent)
    }

    companion object {
        @JvmStatic
        val KEY_ATTEMPT = "ATTEMPT"

        @JvmStatic
        val KEY_NUMBER = "NUMBER"
    }
}
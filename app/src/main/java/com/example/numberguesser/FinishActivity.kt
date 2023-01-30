package com.example.numberguesser

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.numberguesser.databinding.ActivityFinishBinding

class FinishActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFinishBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFinishBinding.inflate(layoutInflater).also { setContentView(it.root) }
        binding.againButton.setOnClickListener { onClickAgain() }
        binding.instructionButton.setOnClickListener { onClickInstruction() }


        val curTryNumber = intent.getIntExtra(KEY_ATTEMPT, -1)
        val curNumberVal = intent.getIntExtra(KEY_NUMBER, -1)

        binding.guessedNumberText.text = curNumberVal.toString()
        binding.attemptsSpentText.text =
            binding.attemptsSpentText.text.replace(Regex("n"), curTryNumber.toString())

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
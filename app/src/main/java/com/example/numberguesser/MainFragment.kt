package com.example.numberguesser

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.AbsoluteSizeSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.numberguesser.contract.navigator
import com.example.numberguesser.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentMainBinding.inflate(inflater)

        binding.instructionText1.text = formatInstructions()

        binding.startButton.setOnClickListener { onStartPressed() }

        return binding.root
    }

    private fun onStartPressed() {
        navigator().showGameScreen()
    }

    private fun formatInstructions(): Spannable {
        val spannable = SpannableStringBuilder(getString(R.string.instruction_1))
        spannable.setSpan(
            AbsoluteSizeSpan(22, true),
            17, 18, Spannable.SPAN_EXCLUSIVE_INCLUSIVE
        )

        spannable.setSpan(
            AbsoluteSizeSpan(22, true),
            22, 26, Spannable.SPAN_EXCLUSIVE_INCLUSIVE
        )
        return spannable
    }

}
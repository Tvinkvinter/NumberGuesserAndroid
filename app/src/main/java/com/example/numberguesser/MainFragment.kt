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
    private lateinit var binding: FragmentMainBinding

    private lateinit var options: Options
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        options = arguments?.getParcelable(KEY_OPTIONS)
            ?: throw IllegalArgumentException("Can't launch MainFragment without options")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater)

        (requireActivity() as MainActivity).showRangeItem()

        binding.startButton.setOnClickListener { onStartPressed() }
        updateInstructions()

        return binding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(KEY_OPTIONS, options)
    }

    private fun onStartPressed() {
        navigator().showGameScreen()
    }

    fun updateInstructions() {
        binding.instructionText1.text = formatInstructions()
    }

    private fun formatInstructions(): Spannable {
        val maxTries = countMaxTries(options.maxNumber)
        val text = getString(R.string.instruction_1, options.maxNumber, maxTries)
        var isNumber = false
        val numbersToSpan: MutableList<Int> = arrayListOf() //start1, end1, start2, end2, ...
        var curPairIndex = 0
        for ((index, letter) in text.withIndex()) {
            if (letter.isDigit()) {
                if (!isNumber) {
                    isNumber = true
                    numbersToSpan.add(index)
                    curPairIndex++
                }
            } else {
                if (isNumber) {
                    isNumber = false
                    numbersToSpan.add(index)
                    curPairIndex++
                }
            }
        }
        val spannable =
            SpannableStringBuilder(getString(R.string.instruction_1, options.maxNumber, maxTries))
        spannable.setSpan(
            AbsoluteSizeSpan(22, true),
            numbersToSpan[0], numbersToSpan[1], Spannable.SPAN_EXCLUSIVE_INCLUSIVE
        )

        spannable.setSpan(
            AbsoluteSizeSpan(22, true),
            numbersToSpan[2], numbersToSpan[3], Spannable.SPAN_EXCLUSIVE_INCLUSIVE
        )
        return spannable
    }

    companion object {
        @JvmStatic
        private val KEY_OPTIONS = "OPTIONS"

        fun newInstance(options: Options): MainFragment {
            val args = Bundle()
            args.putParcelable(KEY_OPTIONS, options)
            val fragment = MainFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
package com.example.numberguesser

import android.os.Bundle
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

        binding.startButton.setOnClickListener { onStartPressed() }

        binding.numberRangeSlider.value = (requireActivity() as MainActivity).preferences.getFloat(
            MainActivity.PREF_SLIDER_VAL,
            1000F
        )
        binding.numberRangeSlider.addOnChangeListener { _, value, _ ->
            (requireActivity() as MainActivity).preferences.edit()
                .putFloat(MainActivity.PREF_SLIDER_VAL, value).apply()
            binding.numberRangeText.text =
                getString(R.string.guessed_number_range, 1, value.toInt())
            binding.takeAttemptsText.text =
                getString(R.string.instruction_take_attempts, countMaxTries(value.toInt()))
            options.maxNumber = value.toInt()
        }

        binding.numberRangeText.text =
            getString(
                R.string.guessed_number_range,
                1, binding.numberRangeSlider.value.toInt()
            )
        binding.takeAttemptsText.text = getString(
            R.string.instruction_take_attempts,
            countMaxTries(binding.numberRangeSlider.value.toInt())
        )

        return binding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(KEY_OPTIONS, options)
    }

    private fun onStartPressed() {
        navigator().showGameScreen()
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
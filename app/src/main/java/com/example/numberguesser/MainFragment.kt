package com.example.numberguesser

import android.os.Bundle
import android.transition.TransitionInflater
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.example.numberguesser.databinding.FragmentMainBinding

class MainFragment : Fragment() {
    private lateinit var binding: FragmentMainBinding

    private lateinit var options: Options
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = TransitionInflater.from(context).inflateTransition(android.R.transition.move)
        options = arguments?.getParcelable(KEY_OPTIONS)
            ?: Options.DEFAULT
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainBinding.inflate(inflater)

        binding.startButton.setOnClickListener { onStartPressed() }

        binding.numberRangeSlider.value = (requireActivity() as MainActivity).preferences.getFloat(
            MainActivity.PREF_SLIDER_VAL,
            Options.DEFAULT.maxNumber.toFloat()
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
        options.tryNumber = 1
        options.curNumber = options.maxNumber / 2
        //(requireActivity() as MainActivity).shrinkPanel()
        findNavController().navigate(
            R.id.action_mainFragment_to_gameFragment,
            bundleOf(GameFragment.KEY_OPTIONS to options), null,
            FragmentNavigatorExtras(binding.topPanel to "topPanelNormal")
        )
    }

    companion object {
        const val KEY_OPTIONS = "OPTIONS"
    }
}
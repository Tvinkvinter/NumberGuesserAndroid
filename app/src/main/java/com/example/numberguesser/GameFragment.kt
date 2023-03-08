package com.example.numberguesser

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.numberguesser.contract.navigator
import com.example.numberguesser.databinding.FragmentGameBinding
import kotlin.math.pow

class GameFragment : Fragment() {

    private lateinit var binding: FragmentGameBinding

    private lateinit var options: Options

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        options = arguments?.getParcelable(KEY_OPTIONS)
            ?: throw IllegalArgumentException("Can't launch GameFragment without options")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGameBinding.inflate(inflater)

        binding.lessButton.setOnClickListener { onClickButtons(it) }
        binding.moreButton.setOnClickListener { onClickButtons(it) }
        binding.finishButton.setOnClickListener { onClickFinish() }

        binding.tryNumberText.text = getString(R.string.try_number, options.tryNumber)
        binding.number.text = options.curNumber.toString()
        return binding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(KEY_OPTIONS, options)
    }

    private fun onClickButtons(v: View) {
        options.tryNumber++
        when (v.id) {
            R.id.less_button -> options.curNumber = binarySearch(false)
            R.id.more_button -> options.curNumber = binarySearch(true)
        }
        binding.tryNumberText.text = getString(R.string.try_number, options.tryNumber)
        binding.number.text = options.curNumber.toString()
        if (options.tryNumber >= 10) onClickFinish()
    }

    private fun onClickFinish() {
        if (options.tryNumber >= 10) {
            requireActivity().supportFragmentManager.popBackStack()
        }

        navigator().showFinishScreen(options)
    }


    private fun binarySearch(nextIsMore: Boolean): Int {
        var nextNumber = 0.0
        val limitPow = 10
        nextNumber += if (nextIsMore) {
            options.curNumber + 2.0.pow(limitPow - options.tryNumber)
        } else {
            options.curNumber - 2.0.pow(limitPow - options.tryNumber)
        }
        return nextNumber.toInt()
    }

    companion object {
        @JvmStatic
        private val KEY_OPTIONS = "OPTIONS"

        fun newInstance(options: Options): GameFragment {
            val args = Bundle()
            args.putParcelable(KEY_OPTIONS, options)
            val fragment = GameFragment()
            fragment.arguments = args
            return fragment
        }
    }
}
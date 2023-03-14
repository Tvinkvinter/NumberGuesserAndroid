package com.example.numberguesser

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.numberguesser.contract.navigator
import com.example.numberguesser.databinding.FragmentGameBinding
import kotlin.math.roundToInt
import kotlin.properties.Delegates

class GameFragment : Fragment() {

    private lateinit var binding: FragmentGameBinding

    private lateinit var options: Options

    private var limitPow by Delegates.notNull<Int>()

    private var curRange by Delegates.notNull<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        options = arguments?.getParcelable(KEY_OPTIONS)
            ?: throw IllegalArgumentException("Can't launch GameFragment without options")
        limitPow = countMaxTries(options.maxNumber)
        curRange = options.curNumber
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGameBinding.inflate(inflater)

        (requireActivity() as MainActivity).hideRangeItem()

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
        when (v.id) {
            R.id.less_button -> onClickLessButton()
            R.id.more_button -> onClickMoreButton()
        }
        binding.tryNumberText.text = getString(R.string.try_number, options.tryNumber)
        binding.number.text = options.curNumber.toString()
        if (options.tryNumber >= limitPow) onClickFinish()
    }

    private fun onClickLessButton() {
        if (options.curNumber == 1) {
            Toast.makeText(
                requireActivity().applicationContext,
                "Не по правилам",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            options.tryNumber++
            binarySearch(false)
        }
    }

    private fun onClickMoreButton() {
        if (options.curNumber == options.maxNumber) {
            Toast.makeText(
                requireActivity().applicationContext,
                "Не по правилам",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            options.tryNumber++
            binarySearch(true)
        }
    }

    private fun onClickFinish() {
        if (options.tryNumber >= limitPow) {
            requireActivity().supportFragmentManager.popBackStack()
        }

        navigator().showFinishScreen(options)
    }


    private fun binarySearch(nextIsMore: Boolean) {
        curRange = (curRange.toFloat() / 2).roundToInt()
        Log.i("Range", "$curRange")
        options.curNumber += if (nextIsMore) {
            curRange
        } else {
            -curRange
        }
        if(options.curNumber < 1) options.curNumber = 1
        if(options.curNumber > options.maxNumber) options.curNumber = options.maxNumber
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
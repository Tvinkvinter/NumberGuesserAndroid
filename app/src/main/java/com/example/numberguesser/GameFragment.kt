package com.example.numberguesser

import android.app.AlertDialog
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.transition.TransitionInflater
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.example.numberguesser.databinding.FragmentGameBinding
import com.example.numberguesser.util.GameState
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.properties.Delegates

class GameFragment : Fragment() {

    private lateinit var binding: FragmentGameBinding

    private lateinit var options: Options

    private var history = mutableListOf<GameState>()

    private var limitPow by Delegates.notNull<Int>()

    private var curRange by Delegates.notNull<Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.move)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        history.clear()
        options = requireArguments().getParcelable(KEY_OPTIONS)!!
        limitPow = countMaxTries(options.maxNumber)
        if (options.tryNumber >= limitPow) {
            options.tryNumber = 1
            options.curNumber = options.maxNumber / 2
        }
        curRange = (options.maxNumber / 2.0.pow(options.tryNumber)).toInt()
        history.add(GameState(curRange, options.tryNumber, options.curNumber))

        binding = FragmentGameBinding.inflate(inflater)

        binding.prevAttemptButton.setOnClickListener {
            onPrevButton()
        }
        binding.openMenuButton.setOnClickListener {
            showHelpCardDialog()
        }

        binding.attemptsSpentText.text = getString(R.string.attempt_number, options.tryNumber)
        binding.currentNumber.text = options.curNumber.toString()

        binding.lessButton.setOnClickListener { onClickButtons(it) }
        binding.moreButton.setOnClickListener { onClickButtons(it) }
        binding.finishButton.setOnClickListener { onClickFinish() }
        return binding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(KEY_OPTIONS, options)
    }

    private fun onPrevButton() {
        history.removeLast()
        if (history.size == 1) binding.prevAttemptButton.visibility = View.GONE
        options.tryNumber = history.last().tryNumber
        options.curNumber = history.last().curNumber
        curRange = history.last().curRange
        binding.attemptsSpentText.text = getString(R.string.attempt_number, options.tryNumber)
        binding.currentNumber.text = options.curNumber.toString()
    }

    private fun onClickButtons(v: View) {
        when (v.id) {
            R.id.less_button -> onClickLessButton()
            R.id.more_button -> onClickMoreButton()
        }
        binding.attemptsSpentText.text = getString(R.string.attempt_number, options.tryNumber)
        binding.currentNumber.text = options.curNumber.toString()
        history.add(GameState(curRange, options.tryNumber, options.curNumber))
        binding.prevAttemptButton.visibility = View.VISIBLE
        if (options.tryNumber >= limitPow) onClickFinish()
    }

    private fun onClickLessButton() {
        if (options.curNumber == 1) {
            Toast.makeText(
                requireActivity().applicationContext,
                getString(R.string.less_button_warning, 1),
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
                getString(R.string.more_button_warning, options.maxNumber),
                Toast.LENGTH_SHORT
            ).show()
        } else {
            options.tryNumber++
            binarySearch(true)
        }
    }

    private fun onClickFinish() {
        findNavController().navigate(
            R.id.action_gameFragment_to_finishFragment,
            bundleOf(FinishFragment.KEY_OPTIONS to options)
        )
    }


    private fun binarySearch(nextIsMore: Boolean) {
        curRange = (curRange.toFloat() / 2).roundToInt()
        options.curNumber += if (nextIsMore) {
            curRange
        } else {
            -curRange
        }
        if (options.curNumber < 1) options.curNumber = 1
        if (options.curNumber > options.maxNumber) options.curNumber = options.maxNumber
    }

    private fun showHelpCardDialog() {
        val builder = AlertDialog.Builder(requireActivity(), R.style.AlertDialogTheme)
        val view = LayoutInflater.from(requireActivity()).inflate(
            R.layout.help_dialog_card, requireActivity().findViewById(R.id.dialog_card)
        )
        builder.setView(view)

        val alertDialog = builder.create()

        if (alertDialog.window != null) {
            alertDialog.window!!.setBackgroundDrawable(ColorDrawable(0))
        }
        (view.findViewById(R.id.dialog_ok_button) as Button).setOnClickListener {
            alertDialog.dismiss()
        }
        alertDialog.show()
    }

    companion object {
        const val KEY_OPTIONS = "OPTIONS"
    }
}
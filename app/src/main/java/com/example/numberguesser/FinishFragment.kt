package com.example.numberguesser

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.FragmentManager
import com.example.numberguesser.contract.navigator
import com.example.numberguesser.databinding.FragmentFinishBinding

class FinishFragment : Fragment() {

    private lateinit var binding: FragmentFinishBinding

    private lateinit var options: Options

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        options = arguments?.getParcelable(KEY_OPTIONS)
            ?: throw IllegalArgumentException("Can't launch FinishFragment without options")

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFinishBinding.inflate(inflater)

        (requireActivity() as MainActivity).hideRangeItem()

        val curTryNumber = options.tryNumber
        val curNumberVal = options.curNumber

        if (Math.random() > 0.5)
            binding.background.setImageDrawable(
                AppCompatResources.getDrawable(
                    requireContext(),
                    R.drawable.bg_finish_digits
                )
            )

        binding.againButton.setOnClickListener { onClickAgain() }
        binding.instructionButton.setOnClickListener { onClickInstruction() }

        binding.guessedNumberText.text = curNumberVal.toString()
        binding.attemptsSpentText.text = getString(R.string.attempts_spent, curTryNumber)

        return binding.root
    }

    private fun onClickAgain() {
        requireActivity().supportFragmentManager.popBackStack()
        requireActivity().supportFragmentManager.popBackStack()
        navigator().showGameScreen()
    }

    private fun onClickInstruction() {
        requireActivity().supportFragmentManager.popBackStack(
            null,
            FragmentManager.POP_BACK_STACK_INCLUSIVE
        )
    }

    companion object {

        @JvmStatic
        val KEY_OPTIONS = "OPTIONS"

        fun newInstance(options: Options): FinishFragment {
            val args = Bundle()
            args.putParcelable(KEY_OPTIONS, options)
            val fragment = FinishFragment()
            fragment.arguments = args
            return fragment
        }
    }


}
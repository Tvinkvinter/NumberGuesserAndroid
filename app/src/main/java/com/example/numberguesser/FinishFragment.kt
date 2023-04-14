package com.example.numberguesser

import android.os.Bundle
import android.transition.TransitionInflater
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavOptions
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.example.numberguesser.databinding.FragmentFinishBinding

class FinishFragment : Fragment() {

    private lateinit var binding: FragmentFinishBinding

    private lateinit var options: Options
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition =
            TransitionInflater.from(context).inflateTransition(android.R.transition.move)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        options = requireArguments().getParcelable(GameFragment.KEY_OPTIONS)!!
        binding = FragmentFinishBinding.inflate(inflater)

        val curTryNumber = options.tryNumber
        val curNumberVal = options.curNumber

        binding.againButton.setOnClickListener { onClickAgain() }
        binding.goToBeginButton.setOnClickListener { onClickGoToBegin() }

        binding.guessedNumber.text = curNumberVal.toString()
        binding.attemptsSpentText.text = getString(R.string.finish_attempts, curTryNumber)

        return binding.root
    }

    private fun onClickAgain() {
        options.curNumber = options.maxNumber / 2
        options.tryNumber = 1
        findNavController().popBackStack()
    }

    private fun onClickGoToBegin() {
        findNavController().navigate(
            R.id.mainFragment,
            null,
            NavOptions.Builder()
                .setPopUpTo(
                    R.id.mainFragment,
                    true
                ).build(),
            FragmentNavigatorExtras(binding.topPanel to "topPanelExpanded")
        )
    }

    companion object {
        const val KEY_OPTIONS = "OPTIONS"
    }


}
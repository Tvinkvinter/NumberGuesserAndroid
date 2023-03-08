package com.example.numberguesser.contract

import androidx.fragment.app.Fragment
import com.example.numberguesser.Options

fun Fragment.navigator(): Navigator {
    return requireActivity() as Navigator
}

interface Navigator {

    fun showMainScreen()

    fun showGameScreen()

    fun showFinishScreen(options: Options)

}

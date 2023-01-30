package com.example.numberguesser

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.numberguesser.databinding.ActivityGameBinding
import kotlin.math.pow

class GameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGameBinding
    val base = 2.0
    var curTryNumber = 1
    var limitPow = 10
    var curNumberVal = base.pow(limitPow).toInt()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameBinding.inflate(layoutInflater).also { setContentView(it.root) }

        binding.redButton.setOnClickListener { onClickButtons(it) }
        binding.greenButton.setOnClickListener { onClickButtons(it) }
        binding.finishButton.setOnClickListener { onClickFinish() }


        if (savedInstanceState == null) {
            curNumberVal = binarySearch(false)

        } else {
            curTryNumber = savedInstanceState.getInt(KEY_ATTEMPT)
            curNumberVal = savedInstanceState.getInt(KEY_NUMBER)
        }
        val newText =
            applicationContext.resources.getString(R.string.try_number) + " " + curTryNumber.toString()
        binding.tryNumber.text = newText
        binding.number.text = curNumberVal.toString()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_ATTEMPT, curTryNumber)
        outState.putInt(KEY_NUMBER, curNumberVal)
    }

    private fun onClickButtons(v: View) {
        curTryNumber++
        when (v.id) {
            R.id.red_button -> curNumberVal = binarySearch(false)
            R.id.green_button -> curNumberVal = binarySearch(true)
        }
        val newText =
            applicationContext.resources.getString(R.string.try_number) + " " + curTryNumber.toString()
        binding.tryNumber.text = newText
        binding.number.text = curNumberVal.toString()
        if(curTryNumber >= 10) onClickFinish()
    }

    private fun onClickFinish() {
        val intent = Intent(this, FinishActivity::class.java)
        intent.putExtra(FinishActivity.KEY_ATTEMPT, curTryNumber)
        intent.putExtra(FinishActivity.KEY_NUMBER, curNumberVal)
        startActivity(intent)
        if (curTryNumber >= 10) finish()
    }


    private fun binarySearch(nextIsMore: Boolean): Int {
        var nextNumber = 0.0
        nextNumber += if (nextIsMore) {
            curNumberVal + base.pow(limitPow - curTryNumber)
        } else {
            curNumberVal - base.pow(limitPow - curTryNumber)
        }
        return nextNumber.toInt()
    }

    companion object{
        @JvmStatic private val KEY_ATTEMPT = "ATTEMPT"
        @JvmStatic private val KEY_NUMBER = "NUMBER"
    }
}
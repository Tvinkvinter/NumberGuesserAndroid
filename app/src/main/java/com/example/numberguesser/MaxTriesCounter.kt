package com.example.numberguesser

import kotlin.math.pow

fun countMaxTries(maxNumber: Int): Int {
    var curDeg = 0
    while (2.0.pow(curDeg) < maxNumber) {
        curDeg++
    }
    return curDeg
}
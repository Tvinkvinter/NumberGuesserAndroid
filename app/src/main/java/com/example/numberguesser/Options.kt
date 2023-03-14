package com.example.numberguesser

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Options(
    var maxNumber: Int,
    var tryNumber: Int,
    var curNumber: Int
) : Parcelable {

    companion object {
        @JvmStatic val DEFAULT = Options(maxNumber = 1000, tryNumber = 1, curNumber = 500)
    }
}
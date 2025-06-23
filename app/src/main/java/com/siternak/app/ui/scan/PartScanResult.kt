package com.siternak.app.ui.scan

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PartScanResult(
    val mouthResult: Int,
    val tongueResult: Int?,
    val gumResult: Int?,
    val footResult: Int
) : Parcelable
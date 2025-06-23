package com.siternak.app.ui.scan

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PartClassification(
    val partName: String, // e.g., "Mulut", "Lidah"
    val result: String,   // e.g., "Normal", "Sedang", "Tinggi"
    val confidence: Float
) : Parcelable

// Merepresentasikan hasil deteksi keseluruhan
@Parcelize
data class DetectionResult(
    val overallPercentage: Int,
    val classifications: List<PartClassification>
) : Parcelable
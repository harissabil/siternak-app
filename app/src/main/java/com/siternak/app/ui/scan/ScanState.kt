package com.siternak.app.ui.scan

import java.io.File

// Tambahkan state untuk hasil dan navigasi
data class ScanState(
    val isLoading: Boolean = false,
    val mouthFile: File? = null,
    val tongueFile: File? = null,
    val salivaFile: File? = null,
    val footFile: File? = null,
    val detectionResult: DetectionResult? = null, // Untuk menampung hasil akhir
    val navigateToResult: Boolean = false // Trigger untuk navigasi
)

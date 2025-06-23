package com.siternak.app.ui.scan

import java.io.File

// Tambahkan state untuk hasil dan navigasi
data class ScanState(
    val isLoading: Boolean = false,
    val mouthFile: File? = null,
    val tongueFile: File? = null,
    val gumFile: File? = null,
    val footFile: File? = null,
    val scanResult: PartScanResult? = null, // Hasil scan dalam bentuk Int
    val navigateToQuestionnaire: Boolean = false // Trigger navigasi baru
)

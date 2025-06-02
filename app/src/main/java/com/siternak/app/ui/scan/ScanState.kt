package com.siternak.app.ui.scan

import java.io.File

data class ScanState(
    val isLoading: Boolean = false,
    val mouthFile: File? = null,
    val tongueFile: File? = null,
    val salivaFile: File? = null,
    val footFile: File? = null,
)

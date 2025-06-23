package com.siternak.app.data.ml

object PMKConfig {
    
    // Model settings
    const val MODEL_INPUT_SIZE = 224
    const val MODEL_INPUT_CHANNELS = 3
    const val NUM_CLASSES = 4
    
    // Class names (urutan sesuai training)
    val CLASS_NAMES = arrayOf(
        "0_sehat", "1_ringan", "2_sedang", "3_berat"
    )
    
    // Preprocessing constants (ImageNet normalization)
    const val MEAN_R = 0.485f
    const val MEAN_G = 0.456f
    const val MEAN_B = 0.406f
    const val STD_R = 0.229f
    const val STD_G = 0.224f
    const val STD_B = 0.225f
}
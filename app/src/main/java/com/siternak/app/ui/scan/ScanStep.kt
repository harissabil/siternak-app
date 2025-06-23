package com.siternak.app.ui.scan

// Tambahkan properti isOptional
enum class ScanStep(val title: String, val instruction: String, val isOptional: Boolean = false) {
    MOUTH("Mulut", "Silahkan foto bagian mulut hewan ternak anda!\nPastikan terlihat jelas!"),
    TONGUE("Lidah", "Silahkan foto bagian lidah hewan ternak anda!\n(Opsional)", isOptional = true),
    SALIVA("Gusi", "Silahkan foto bagian gusi hewan ternak anda!\n(Opsional)", isOptional = true),
    FOOT("Kaki", "Silahkan foto bagian kaki hewan ternak anda!\nPastikan terlihat jelas!")
}
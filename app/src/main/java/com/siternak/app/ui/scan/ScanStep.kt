package com.siternak.app.ui.scan

enum class ScanStep(val title: String, val instruction: String) {
    MOUTH("Mulut", "Silahkan foto bagian mulut hewan ternak anda!\nPastikan terlihat jelas!"),
    TONGUE("Lidah", "Silahkan foto bagian lidah hewan ternak anda!\nPastikan terlihat jelas!"),
    SALIVA("Air Liur", "Silahkan foto bagian air liur hewan ternak anda!\nPastikan terlihat jelas!"),
    FOOT("Kaki", "Silahkan foto bagian kaki hewan ternak anda!\nPastikan terlihat jelas!")
}
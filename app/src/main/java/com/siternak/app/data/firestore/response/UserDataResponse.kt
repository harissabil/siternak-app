package com.siternak.app.data.firestore.response

import androidx.annotation.Keep
import com.google.firebase.firestore.PropertyName
import com.siternak.app.domain.model.UserData

@Keep
data class UserDataResponse(
    val uid: String = "",
    val nama: String = "",
    @get:PropertyName("nomor_hp")
    @set:PropertyName("nomor_hp")
    var nomorHp: String = "",
    val provinsi: String = "",
    val kota: String = "",
    val kecamatan: String = "",
    val alamat: String = "",
) {
    fun toUserData(): UserData {
        return UserData(
            uid = uid,
            nama = nama,
            nomorHp = nomorHp,
            provinsi = provinsi,
            kota = kota,
            kecamatan = kecamatan,
            alamat = alamat
        )
    }
}

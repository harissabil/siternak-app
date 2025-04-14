package com.siternak.app.data.firestore.request

import androidx.annotation.Keep
import com.google.firebase.firestore.PropertyName
import com.siternak.app.domain.model.UserData

@Keep
data class UserDataRequest(
    val uid: String? = null,
    val nama: String,
    @get:PropertyName("nomor_hp")
    @set:PropertyName("nomor_hp")
    var nomorHp: String,
    val provinsi: String,
    val kota: String,
    val kecamatan: String,
    val alamat: String,
)

internal fun UserData.toUserDataRequest(): UserDataRequest {
    return UserDataRequest(
        uid = this.uid,
        nama = this.nama,
        nomorHp = this.nomorHp,
        provinsi = this.provinsi,
        kota = this.kota,
        kecamatan = this.kecamatan,
        alamat = this.alamat,
    )
}

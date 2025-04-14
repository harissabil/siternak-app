package com.siternak.app.data.firestore.request

import androidx.annotation.Keep
import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId
import com.google.firebase.firestore.PropertyName
import com.siternak.app.data.firestore.response.PostResponse
import com.siternak.app.domain.model.Post

@Keep
data class PostRequest(
    @DocumentId
    @get:PropertyName("post_id")
    @set:PropertyName("post_id")
    var postId: String? = "",

    @get:PropertyName("user_id")
    @set:PropertyName("user_id")
    var uid: String = "",

    @get:PropertyName("jenis_ternak")
    @set:PropertyName("jenis_ternak")
    var jenisTernak: String,

    @get:PropertyName("jumlah_ternak")
    @set:PropertyName("jumlah_ternak")
    var jumlahTernak: String,

    @get:PropertyName("jenis_aksi")
    @set:PropertyName("jenis_aksi")
    var jenisAksi: String,

    @get:PropertyName("keterangan_aksi")
    @set:PropertyName("keterangan_aksi")
    var keteranganAksi: String,

    @get:PropertyName("alamat_aksi")
    @set:PropertyName("alamat_aksi")
    var alamatAksi: String,

    val latitude: Double?,

    val longitude: Double?,

    @get:PropertyName("created_at")
    @set:PropertyName("created_at")
    var createdAt: Timestamp? = Timestamp.now(),

    @get:PropertyName("updated_at")
    @set:PropertyName("updated_at")
    var updatedAt: Timestamp? = null,

    val petugas: String? = null,

    val status: String? = null,
) {
    fun toPostResponse() = PostResponse(
        postId = postId.toString(),
        uid = uid,
        jenisTernak = jenisTernak,
        jumlahTernak = jumlahTernak,
        jenisAksi = jenisAksi,
        keteranganAksi = keteranganAksi,
        alamatAksi = alamatAksi,
        latitude = latitude,
        longitude = longitude,
        createdAt = createdAt,
        updatedAt = updatedAt,
        petugas = petugas,
        status = status
    )
}

internal fun Post.toPostRequest() = PostRequest(
    postId = postId,
    uid = uid,
    jenisTernak = jenisTernak,
    jumlahTernak = jumlahTernak,
    jenisAksi = jenisAksi,
    keteranganAksi = keteranganAksi,
    alamatAksi = alamatAksi,
    latitude = latitude,
    longitude = longitude,
    createdAt = createdAt,
    updatedAt = updatedAt,
    petugas = petugas,
    status = status
)
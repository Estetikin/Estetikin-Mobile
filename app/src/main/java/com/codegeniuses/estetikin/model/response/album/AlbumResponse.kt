package com.codegeniuses.estetikin.model.response.album

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AlbumResponse(
    val arrAlbum: List<ArrAlbumItem>,
    val error: Boolean,
    val message: String,
    val status: String
) : Parcelable
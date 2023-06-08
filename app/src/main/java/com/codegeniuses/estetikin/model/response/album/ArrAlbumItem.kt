package com.codegeniuses.estetikin.model.response.album

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ArrAlbumItem(
    val dateUpload: String,
    val link: String,
    val dummytext: String
) : Parcelable
package com.codegeniuses.estetikin.model.response.album

import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class AlbumItem(
	val dateUpload: String,
	val photoUrl: String,
	val recommendation: String
) : Parcelable
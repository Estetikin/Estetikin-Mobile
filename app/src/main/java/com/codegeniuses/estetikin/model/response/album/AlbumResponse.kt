package com.codegeniuses.estetikin.model.response.album

import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class AlbumResponse(
	val albumItems: List<AlbumItem>,
	val error: Boolean,
	val message: String,
	val status: String
) : Parcelable
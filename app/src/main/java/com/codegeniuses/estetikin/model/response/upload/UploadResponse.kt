package com.codegeniuses.estetikin.model.response.upload

import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class UploadResponse(
	val link: String,
	val error: Boolean,
	val message: String,
	val status: String
) : Parcelable
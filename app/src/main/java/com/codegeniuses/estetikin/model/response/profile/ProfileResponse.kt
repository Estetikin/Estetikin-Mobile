package com.codegeniuses.estetikin.model.response.profile

import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class ProfileResponse(
	val link: String,
	val error: Boolean,
	val message: String,
	val status: String
) : Parcelable
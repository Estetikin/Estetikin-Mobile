package com.codegeniuses.estetikin.model.response.profile

import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class GetProfileResponse(
	val nopicture: Int,
	val nickname: String,
	val error: Boolean,
	val message: String,
	val picture: String,
	val status: String
) : Parcelable
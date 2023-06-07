package com.codegeniuses.estetikin.model.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class ModuleResponse(
	val data: List<DataItem?>,
	val error: Boolean,
	val status: String
) : Parcelable
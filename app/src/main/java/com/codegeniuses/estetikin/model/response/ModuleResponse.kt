package com.codegeniuses.estetikin.model.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class ModuleResponse(
	val data: List<ModuleItem?>,
	val error: Boolean,
	val status: String
) : Parcelable
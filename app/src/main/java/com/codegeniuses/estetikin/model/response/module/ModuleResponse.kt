package com.codegeniuses.estetikin.model.response.module

import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class ModuleResponse(
	val data: List<DataItem>,
	val message: String,
	val error: Boolean
) : Parcelable
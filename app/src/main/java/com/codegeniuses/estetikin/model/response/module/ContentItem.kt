package com.codegeniuses.estetikin.model.response.module

import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class ContentItem(
	val description: String,
	val id: Int,
	val title: String,
	val url: String
) : Parcelable
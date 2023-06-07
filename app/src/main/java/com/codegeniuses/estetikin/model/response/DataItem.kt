package com.codegeniuses.estetikin.model.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class DataItem(
	val genre: String? = null,
	val description: String? = null,
	val id: Int? = null,
	val title: String? = null,
	val url: String? = null,
	val tag: String? = null,
	val content: List<ContentItem?>? = null
) : Parcelable
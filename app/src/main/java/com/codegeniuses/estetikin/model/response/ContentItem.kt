package com.codegeniuses.estetikin.model.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class ContentItem(
	val description: String? = null,
	val id: Int? = null,
	val title: String? = null,
	val url: String? = null
) : Parcelable
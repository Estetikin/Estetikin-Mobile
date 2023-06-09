package com.codegeniuses.estetikin.model.response.module

import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class DataItem(
	val genre: String,
	val description: String,
	val id: Int,
	val title: String,
	val url: String,
	val tag: String,
	val content: List<ContentItem>
) : Parcelable
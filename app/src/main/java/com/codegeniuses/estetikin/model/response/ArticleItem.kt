package com.codegeniuses.estetikin.model.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class ArticleItem(
	val author: String? = null,
	val title: String? = null,
	val url: String? = null
) : Parcelable
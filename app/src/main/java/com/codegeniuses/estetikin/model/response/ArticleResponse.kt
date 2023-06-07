package com.codegeniuses.estetikin.model.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable

@Parcelize
data class ArticleResponse(
	val data: List<ArticleItem>,
	val message: String,
	val error: Boolean
) : Parcelable
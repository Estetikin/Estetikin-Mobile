package com.codegeniuses.estetikin.model.response.article

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ArticleResponse(
    val data: List<ArticleItem>,
    val message: String,
    val error: Boolean
) : Parcelable
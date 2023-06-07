package com.codegeniuses.estetikin.model.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ArticleItem(
    val author: String? = null,
    val title: String? = null,
    val url: String? = null
) : Parcelable
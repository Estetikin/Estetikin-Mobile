package com.codegeniuses.estetikin.model.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ContentItem(
    val description: String? = null,
    val id: Int? = null,
    val title: String? = null,
    val url: String? = null
) : Parcelable
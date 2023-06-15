package com.codegeniuses.estetikin.model.response.module

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ContentItem(
    val description: String,
    val id: Int,
    val title: String,
    val url: String
) : Parcelable
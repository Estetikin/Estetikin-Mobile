package com.codegeniuses.estetikin.model.response.module

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

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
package com.codegeniuses.estetikin.model.response.module

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ModuleItem(
    val author: String? = null,
    val title: String? = null,
    val url: String? = null
) : Parcelable
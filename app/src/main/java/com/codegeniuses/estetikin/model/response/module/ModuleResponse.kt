package com.codegeniuses.estetikin.model.response.module

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ModuleResponse(
    val data: List<DataItem>,
    val message: String,
    val error: Boolean
) : Parcelable
package com.codegeniuses.estetikin.model.response.module

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ModuleResponse(
    val data: List<ModuleDetailItem?>,
    val error: Boolean,
    val status: String
) : Parcelable
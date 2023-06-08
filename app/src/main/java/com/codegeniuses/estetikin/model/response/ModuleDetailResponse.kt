package com.codegeniuses.estetikin.model.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ModuleDetailResponse(
    val data: List<ModuleDetailItem?>,
    val error: Boolean,
    val status: String
) : Parcelable
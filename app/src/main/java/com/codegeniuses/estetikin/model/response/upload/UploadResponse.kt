package com.codegeniuses.estetikin.model.response.upload

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UploadResponse(
    val link: String,
    val error: Boolean,
    val message: String,
    val status: String
) : Parcelable
package com.codegeniuses.estetikin.model.response.profile

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProfileResponse(
    val link: String,
    val error: Boolean,
    val message: String,
    val status: String
) : Parcelable
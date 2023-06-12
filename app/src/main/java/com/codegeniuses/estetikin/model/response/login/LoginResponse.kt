package com.codegeniuses.estetikin.model.response.login

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class LoginResponse(
    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("token")
    val token: String,

    @field:SerializedName("nickname")
    val nickname: String,
) : Parcelable
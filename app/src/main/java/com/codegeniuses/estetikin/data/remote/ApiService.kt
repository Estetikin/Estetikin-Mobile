package com.codegeniuses.estetikin.data.remote


import com.codegeniuses.estetikin.model.response.GeneralResponse
import com.codegeniuses.estetikin.model.response.album.AlbumResponse
import com.codegeniuses.estetikin.model.response.article.ArticleResponse
import com.codegeniuses.estetikin.model.response.login.LoginResponse
import com.codegeniuses.estetikin.model.response.module.ModuleResponse
import com.codegeniuses.estetikin.model.response.profile.GetProfileResponse
import com.codegeniuses.estetikin.model.response.profile.ProfileResponse
import com.codegeniuses.estetikin.model.response.upload.UploadResponse
import okhttp3.MultipartBody
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("auth/v1/register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("passwordConfirm") confirmPassword: String
        //nambah nickname
    ): GeneralResponse

    @FormUrlEncoded
    @POST("auth/v1/login")
    suspend fun login(
        @Field("email") username: String,
        @Field("password") password: String
    ): LoginResponse

    @GET("api/v1/module")
    suspend fun getAllModule(
        @Header("Authorization") token: String
    ): ModuleResponse

    @GET("articles/{type}")
    suspend fun getArticles(
        @Header("Authorization") token: String,
        @Path("type") type: String
    ): ArticleResponse

    @GET("api/v1/album")
    suspend fun getHistoryAlbum(
        @Header("Authorization") token: String
    ): AlbumResponse

    @Multipart
    @POST("api/v1/upload")
    suspend fun uploadImage(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
        @Part("class1") class1: Int,
        @Part("class2") class2: Int,
        @Part("class3") class3: Int,
        @Part("class4") class4: Int
    ): UploadResponse

    @GET("api/v1/profile")
    suspend fun getProfilePicture(
        @Header("Authorization") token: String
    ): GetProfileResponse

    @Multipart
    @POST("api/v1/uploadprofile")
    suspend fun uploadProfilePicture(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part
    ): ProfileResponse
}
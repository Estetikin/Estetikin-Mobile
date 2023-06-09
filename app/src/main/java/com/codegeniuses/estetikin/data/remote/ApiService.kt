package com.codegeniuses.estetikin.data.remote


import com.codegeniuses.estetikin.model.response.GeneralResponse
import com.codegeniuses.estetikin.model.response.album.AlbumResponse
import com.codegeniuses.estetikin.model.response.article.ArticleResponse
import com.codegeniuses.estetikin.model.response.login.LoginResponse
import com.codegeniuses.estetikin.model.response.module.ModuleResponse
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("auth/v1/register")
    suspend fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("passwordConfirm") confirmPassword: String
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
}
package com.example.test.api

import com.example.test.entry.LoginEntry
import com.example.test.entry.RegisterEntry
import com.example.test.entry.UploadEntry
import com.example.test.entry.UserEntry
import com.lsx.network.ApiResult
import com.lsx.network.retrofit
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

/**
 * @CreateDate:     2021/3/18
 * @Author:         LSX
 * @Description:
 */
object Api {
    val retrofitService: ApiService by lazy { retrofit.create(ApiService::class.java) }
}

interface ApiService {
    //注册
    @POST("/api/account/register")
    suspend fun register(@Body requestBody: RequestBody): ApiResult<RegisterEntry>

    //登陆
    @POST("/api/account/login")
    suspend fun login(@Body requestBody: RequestBody): ApiResult<LoginEntry>

    //找回密码
    @POST("/api/account/password/askrecovery")
    suspend fun askRecovery(@Body requestBody: RequestBody): ApiResult<RegisterEntry>

    //获取用户数据
    @GET("/api/account")
    suspend fun getUserInfo(@Header("Authorization") authorization:String): ApiResult<UserEntry>

    @POST("/api/account")
    suspend fun updateUsername(@Header("Authorization") authorization:String,
                               @Body requestBody: RequestBody): ApiResult<RegisterEntry>

    @POST("/api/account")
    suspend fun updatePassword(@Header("Authorization") authorization:String,
                               @Body requestBody: RequestBody): ApiResult<RegisterEntry>

    @POST("/api/account/update")
    suspend fun updateEmail(@Header("Authorization") authorization:String,
                               @Body requestBody: RequestBody): ApiResult<RegisterEntry>

    //更新用户信息
    @POST("/api/account/update")
    suspend fun upadteUserInfo(@Header("Authorization") authorization:String,
                               @Body requestBody: RequestBody): ApiResult<RegisterEntry>

    //更新用户信息
    @POST("/api/account/update")
    suspend fun updateUserPassword(@Header("Authorization") authorization:String,
                               @Body requestBody: RequestBody): ApiResult<RegisterEntry>
    //upload img or video
    @Multipart
    @POST("/api/pitch/video/position")
    suspend fun uploadFile(@Header("Authorization") authorization:String,
                           @Part file:MultipartBody.Part,@Part("file") name:RequestBody): ApiResult<UploadEntry>
}


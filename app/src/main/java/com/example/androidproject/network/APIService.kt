package com.example.androidproject.network

import com.example.androidproject.models.login.Login
import com.example.androidproject.models.login.data
import com.example.androidproject.models.login.loginData
import com.example.androidproject.models.signup.SignupRequest
import com.example.androidproject.models.signup.SignupResponse
import retrofit2.Call
import retrofit2.http.*

interface APIService {

    @POST("LOGIN.php")
    fun login(@Body login: Login): Call<loginData>?

    @POST("SIGNUP.php")
    fun signup(@Body signupRequest: SignupRequest): Call<SignupResponse>?


    @FormUrlEncoded
    @POST("SHAREDLISTSget.php")
    fun sharedListGet(
        @Field("user_id") user_id: Int?,
        @Field("format") format: String?
    ): Call<data>
}
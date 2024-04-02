package com.withpeace.withpeace.core.network.di.service

import com.skydoves.sandwich.ApiResponse
import com.withpeace.withpeace.core.network.di.request.SignUpRequest
import com.withpeace.withpeace.core.network.di.response.BaseResponse
import com.withpeace.withpeace.core.network.di.response.LoginResponse
import com.withpeace.withpeace.core.network.di.response.TokenResponse
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthService {

    @POST("/api/v1/auth/google")
    suspend fun googleLogin(
        @Header("Authorization")
        idToken: String,
    ): ApiResponse<BaseResponse<LoginResponse>>

    @POST("/api/v1/auth/refresh")
    suspend fun refreshAccessToken(
        @Header("Reauthorization") refreshToken: String,
    ): ApiResponse<BaseResponse<TokenResponse>>

    @POST("/api/v1/auth/logout")
    suspend fun logout(): ApiResponse<BaseResponse<String>>

    @POST("/api/v1/auth/register")
    suspend fun signUp(
        @Body signUpRequest: SignUpRequest,
    ): ApiResponse<BaseResponse<TokenResponse>>
}

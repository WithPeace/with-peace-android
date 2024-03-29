package com.withpeace.withpeace.core.network.di.service

import com.skydoves.sandwich.ApiResponse
import com.withpeace.withpeace.core.network.di.response.BaseResponse
import com.withpeace.withpeace.core.network.di.response.PostIdResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.PartMap

interface PostService {

    @Multipart
    @POST("/api/v1/posts/register")
    suspend fun registerPost(
        @PartMap postRequest: HashMap<String, @JvmSuppressWildcards RequestBody>,
        @Part images: List<MultipartBody.Part>,
    ): ApiResponse<BaseResponse<PostIdResponse>>
}

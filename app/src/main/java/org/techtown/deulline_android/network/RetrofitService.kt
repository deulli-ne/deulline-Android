package org.techtown.deulline_android.network

import org.techtown.deulline_android.network.dto.AdditionalInfoVO
import org.techtown.deulline_android.network.dto.ApiResponse
import retrofit2.Call
import org.techtown.deulline_android.network.dto.ExtraInfoVO
import org.techtown.deulline_android.network.dto.ProductInfoVO
import retrofit2.http.GET
import retrofit2.http.Path

interface RetrofitService {

    @GET("/extra/{productId}")
    fun getExtraInfo(@Path("productId") productId : Long) : Call<ApiResponse<ExtraInfoVO>>

    @GET("/product/{productId}")
    fun getProductInfo(@Path("productId") productId : Long) : Call<ApiResponse<ProductInfoVO>>

    @GET("/additional/{productId}")
    fun getAdditionalInfo(@Path("productId") productId: Long) : Call<ApiResponse<AdditionalInfoVO>>
}
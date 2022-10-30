package org.techtown.deulline_android.network.dto

import com.google.gson.annotations.SerializedName

data class ProductInfoVO (
    @SerializedName("productId")
    val productId : Long,

    @SerializedName("productName")
    val productName : String,

    @SerializedName("price")
    val price : Int,

    @SerializedName("reviewCount")
    val reviewCount : Int,

    @SerializedName("ranking")
    val ranking : Int
)
package org.techtown.deulline_android.network.dto

import com.google.gson.annotations.SerializedName

data class AdditionalInfoVO (
    @SerializedName("productId")
    val productId : Long,

    @SerializedName("additionalData")
    val additionalData : String
)
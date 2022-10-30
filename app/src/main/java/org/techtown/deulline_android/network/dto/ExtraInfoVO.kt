package org.techtown.deulline_android.network.dto

import com.google.gson.annotations.SerializedName

data class ExtraInfoVO(
    @SerializedName("id")
    val id : Long,

    @SerializedName("info")
    val info : String
)

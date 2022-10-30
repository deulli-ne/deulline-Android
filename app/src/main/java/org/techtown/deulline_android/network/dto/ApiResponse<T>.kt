package org.techtown.deulline_android.network.dto

import com.google.gson.annotations.SerializedName

// https://minchanyoun.tistory.com/44
class ApiResponse<T> {

    @SerializedName("statusCode")
    private var statusCode : Int = 0

    @SerializedName("responseMessage")
    private lateinit var responseMessage : String

    @SerializedName("data")
    private val data: T? = null

    fun getStatusCode() : Int{
        return statusCode;
    }

    fun getResponseMessage() : String {
        return responseMessage;
    }

    fun getData() : T {
        return data!!;
    }

    override fun toString() : String {
        return "status code : " +  getStatusCode() +
                "\nresponse message : " + getResponseMessage();
    }
}
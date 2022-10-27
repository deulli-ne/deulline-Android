package org.techtown.deulline_android.retrofit.dto;

import com.google.gson.annotations.SerializedName;

//https://krapoi.tistory.com/entry/안드로이드Kotlin-Retrofit2-사용하기
//https://dev-cho.tistory.com/27
//https://taewooblog.tistory.com/entry/안드로이드-retrofit-2-사용법-예제-레트로핏-2-Java
public class ApiResponse<T> {
    @SerializedName("statusCode")
    private int statusCode;

    @SerializedName("responseMessage")
    private String responseMessage;

    @SerializedName("data")
    private T data;

    public int getStatusCode() {
        return statusCode;
    }

    public String getResponseMessage() {
        return responseMessage;
    }

    public T getData() {
        return data;
    }

    public String toString() {
        return "status code : " + getStatusCode() +
                "\nresponse message : " + getResponseMessage();
    }
}
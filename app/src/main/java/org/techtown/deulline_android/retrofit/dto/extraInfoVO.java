package org.techtown.deulline_android.retrofit.dto;

import com.google.gson.annotations.SerializedName;

public class extraInfoVO {
    @SerializedName("id")
    private Long id;

    @SerializedName("info")
    private String info;

    public Long getId(){
        return id;
    }

    public String getInfo() {
        return info;
    }
}

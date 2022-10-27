package org.techtown.deulline_android.retrofit.dto;

import com.google.gson.annotations.SerializedName;

public class productInfoVO {
    @SerializedName("productId")
    private Long productId;

    @SerializedName("productName")
    private String productName;

    @SerializedName("price")
    private int price;

    @SerializedName("reviewCount")
    private int reviewCount;

    @SerializedName("ranking")
    private int ranking;

    public Long getProductId() {
        return productId;
    }

    public String getProductName() {
        return productName;
    }

    public int getPrice() {
        return price;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public int getRanking() {
        return ranking;
    }
}

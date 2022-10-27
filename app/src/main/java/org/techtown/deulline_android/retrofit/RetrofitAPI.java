package org.techtown.deulline_android.retrofit;

import org.techtown.deulline_android.retrofit.dto.ApiResponse;
import org.techtown.deulline_android.retrofit.dto.extraInfoVO;
import org.techtown.deulline_android.retrofit.dto.productInfoVO;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface RetrofitAPI {

    @GET("/product/{productId}")
    Call<ApiResponse<productInfoVO>> getProductInfo(@Path("productId") Long productId);

    @GET("extra/{productId}")
    Call<ApiResponse<extraInfoVO>> getExtraOcr(@Path("productId") Long productId);
}

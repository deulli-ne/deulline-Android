package org.techtown.deulline_android

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.annotations.SerializedName
import org.techtown.deulline_android.network.RetofitClient
import org.techtown.deulline_android.network.RetrofitService
import org.techtown.deulline_android.network.dto.AdditionalInfoVO
import org.techtown.deulline_android.network.dto.ApiResponse
import org.techtown.deulline_android.network.dto.ExtraInfoVO
import org.techtown.deulline_android.network.dto.ProductInfoVO
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit


class MainActivity : AppCompatActivity() {
    private lateinit var retrofit: Retrofit
    private lateinit var retrofitService : RetrofitService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        //상품 음성 입력(STT)




        //서버 연결
        initRetrofit()

        //통신 (상품 기본정보)
        getBasicInformation()

        //화면 열결
        val homeBtn = findViewById<ImageView>(R.id.home)
        homeBtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    //서버 연결
    private fun initRetrofit() {
        retrofit = RetofitClient.getInstance()
        retrofitService = retrofit.create(RetrofitService::class.java)
    }

    //통신: 상품 기본정보 가져오기
    //getProductInfo()의 매개변수 productId 갱신 필요, 2는 임의 값
    fun getBasicInformation() {
        retrofitService.getProductInfo(2)?.enqueue(object : Callback<ApiResponse<ProductInfoVO>> {
            override fun onResponse(call: Call<ApiResponse<ProductInfoVO>>, response: Response<ApiResponse<ProductInfoVO>>) {
                if(response.isSuccessful) {
                    //정상적으로 통신 성공
                    val result: ApiResponse<ProductInfoVO>? = response.body()
                    val data = result?.getData();

                    Log.d("ProductInfoVO", "onresponse 성공: "+ result?.toString())
                    Log.d("ProductInfoVO", "data : "+ data?.toString())
                    Log.d("ProductInfoVO", "productId : "+ data?.productId.toString())
                    Log.d("ProductInfoVO", "productName : "+ data?.productName.toString())
                    Log.d("ProductInfoVO", "price : "+ data?.price.toString())
                    Log.d("ProductInfoVO", "reviewCount : "+ data?.reviewCount.toString())
                    Log.d("ProductInfoVO", "ranking : "+ data?.ranking.toString())


                } else {
                    //통신 실패(응답코드 3xx, 4xx 등)
                    Log.d("YMC", "onResponse 실패" + response.errorBody().toString())
                }
            }

            override fun onFailure(call: Call<ApiResponse<ProductInfoVO>>, t: Throwable) {
                //통신 실패(인터넷 끊김, 예외 발생 등 시스템적인 이유)
                Log.d("YMC", "onFailure 에러: " + t.message.toString());
            }

        })
    }


}
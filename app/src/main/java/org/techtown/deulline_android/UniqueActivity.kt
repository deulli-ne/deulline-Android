package org.techtown.deulline_android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import org.techtown.deulline_android.network.RetofitClient
import org.techtown.deulline_android.network.RetrofitService
import org.techtown.deulline_android.network.dto.AdditionalInfoVO
import org.techtown.deulline_android.network.dto.ApiResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class UniqueActivity : AppCompatActivity() {
    private lateinit var retrofit: Retrofit
    private lateinit var retrofitService : RetrofitService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_unique_acrivity)

        //서버 연결
        initRetrofit()

        //통신 (상품 기본정보)
        getAdditionalInformation()

        //화면 연결
        val backBtn = findViewById<ImageView>(R.id.back)
        backBtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

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

    //통신: 상품 특이정보 가져오기
    //getAdditionalInfo()의 매개변수 productId 갱신 필요, 8은 임의 값
    fun getAdditionalInformation() {
        retrofitService.getAdditionalInfo(8)?.enqueue(object :
            Callback<ApiResponse<AdditionalInfoVO>> {
            override fun onResponse(call: Call<ApiResponse<AdditionalInfoVO>>, response: Response<ApiResponse<AdditionalInfoVO>>) {
                if(response.isSuccessful) {
                    //정상적으로 통신 성공
                    val result: ApiResponse<AdditionalInfoVO>? = response.body()
                    val data = result?.getData();

                    Log.d("AdditionalInfoVO", "onresponse 성공: "+ result?.toString())
                    Log.d("AdditionalInfoVO", "data : "+ data?.toString())
                    Log.d("AdditionalInfoVO", "productId : "+ data?.productId.toString())
                    Log.d("AdditionalInfoVO", "additionalData : "+ data?.additionalData.toString())


                } else {
                    //통신 실패(응답코드 3xx, 4xx 등)
                    response.code()
                    Log.d("YMC", "onResponse 실패" + response.errorBody().toString())
                }
            }

            override fun onFailure(call: Call<ApiResponse<AdditionalInfoVO>>, t: Throwable) {
                //통신 실패(인터넷 끊김, 예외 발생 등 시스템적인 이유)
                Log.d("YMC", "onFailure 에러: " + t.message.toString());
            }

        })
    }
}
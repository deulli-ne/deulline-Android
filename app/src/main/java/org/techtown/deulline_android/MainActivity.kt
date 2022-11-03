package org.techtown.deulline_android

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import org.techtown.deulline_android.network.RetofitClient
import org.techtown.deulline_android.network.RetrofitService
import org.techtown.deulline_android.network.dto.ApiResponse
import org.techtown.deulline_android.network.dto.ProductInfoVO
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit


class MainActivity : AppCompatActivity() {
    private lateinit var retrofit: Retrofit
    private lateinit var retrofitService: RetrofitService
    private lateinit var mNaverTTSTask : NaverTTSTask
    private lateinit var mTextString : Array<String>
    //상품정보
    private lateinit var productName : String
    private lateinit var productPrice : String
    private lateinit var productReviewCnt : String
    private lateinit var productRanking : String


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //상품 음성 입력(STT)
        var tv = "tv"


        //서버 연결
        initRetrofit()

        //통신 (상품 기본정보)
        getBasicInformation(tv)



        //TTS
        val button = findViewById<Button>(R.id.button)
        button.setOnClickListener {

            mTextString = arrayOf(
                "상품 정보입니다." + "상품 이름은 " + productName + "입니다. " + "상품 가격은 " + productPrice + "원 입니다. " +
                        "상품 리뷰수는" + productReviewCnt + "개 입니다. " + "상품 랭킹은" + productRanking + "등 입니다. "
            )
            mNaverTTSTask = NaverTTSTask(mTextString)
            //mNaverTTSTask.execute(mTextString)    //안됨
            mNaverTTSTask.execute(
                arrayOf(
                    "상품 정보입니다." + "상품 이름은 " + productName + "입니다. " + "상품 가격은 " + productPrice + "원 입니다. " +
                            "상품 리뷰수는" + productReviewCnt + "개 입니다. " + "상품 랭킹은" + productRanking + "등 입니다. "
                )
            )

            //20초뒤 화면전환
            Handler(Looper.getMainLooper()).postDelayed({
                val intent = Intent(this, UniqueActivity::class.java)
                startActivity(intent)
            }, 20000)
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


    //통신: 상품 기본정보 가져오기
    fun getBasicInformation(category: String) {
        retrofitService.getProductInfo(category)
            ?.enqueue(object : Callback<ApiResponse<ProductInfoVO>> {
                override fun onResponse(
                    call: Call<ApiResponse<ProductInfoVO>>,
                    response: Response<ApiResponse<ProductInfoVO>>
                ) {
                    if (response.isSuccessful) {
                        //정상적으로 통신 성공
                        val result: ApiResponse<ProductInfoVO>? = response.body()
                        val data = result?.getData();

                        Log.d("ProductInfoVO", "onresponse 성공: " + result?.toString())
                        Log.d("ProductInfoVO", "data : " + data?.toString())

                        productName = data?.productName.toString()
                        productPrice = data?.price.toString()
                        productReviewCnt = data?.reviewCount.toString()
                        productRanking = data?.ranking.toString()

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
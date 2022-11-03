package org.techtown.deulline_android

import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import org.techtown.deulline_android.network.RetofitClient
import org.techtown.deulline_android.network.RetrofitService
import org.techtown.deulline_android.network.dto.ApiResponse
import org.techtown.deulline_android.network.dto.ExtraInfoVO
import org.techtown.deulline_android.network.dto.ProductInfoVO
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit

class ExtraInfoActivity : AppCompatActivity() {
    private lateinit var retrofit: Retrofit
    private lateinit var retrofitService : RetrofitService
    private lateinit var mNaverTTSTask : NaverTTSTask
    private lateinit var mTextString : Array<String>
    private lateinit var additionalInfo : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_extra_info)

        //서버 연결
        initRetrofit()

        //통신 (OCR 전체 세부정보)
        getDetailInformation()

        //TTS
        val button = findViewById<Button>(R.id.button)
        button.setOnClickListener{
            mTextString = arrayOf("추가정보 입니다. " + additionalInfo + "입니다. ")
            mNaverTTSTask = NaverTTSTask(mTextString)
            //mNaverTTSTask.execute(mTextString)    //안됨
            mNaverTTSTask.execute(arrayOf("추가정보 입니다. " + additionalInfo + "입니다. "))

            //30초뒤 화면전환
            Handler(Looper.getMainLooper()).postDelayed({
                val intent = Intent(this, SelectActivity::class.java)
                startActivity(intent)
            }, 30000)
        }


        //화면 연결
        val backBtn = findViewById<ImageView>(R.id.back)
        backBtn.setOnClickListener {
            val intent = Intent(this, SelectActivity::class.java)
            startActivity(intent)
        }

        val homeBtn = findViewById<ImageView>(R.id.home)
        homeBtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }


    //서버 통신
    private fun initRetrofit() {
        retrofit = RetofitClient.getInstance()
        retrofitService = retrofit.create(RetrofitService::class.java)
    }

    //통신: 상품 기본정보 가져오기
    //getExtraInfo()의 매개변수 productId 갱신 필요, 3는 임의 값
    fun getDetailInformation() {
        retrofitService.getExtraInfo(1)?.enqueue(object : Callback<ApiResponse<ExtraInfoVO>> {
            override fun onResponse(call: Call<ApiResponse<ExtraInfoVO>>, response: Response<ApiResponse<ExtraInfoVO>>) {
                if(response.isSuccessful) {
                    //정상적으로 통신 성공
                    val result: ApiResponse<ExtraInfoVO>? = response.body()
                    val data = result?.getData();

                    Log.d("ExtraInfoVO", "onresponse 성공: "+ result?.toString())
                    Log.d("ExtraInfoVO", "data : "+ data?.toString())
                    additionalInfo = data?.info.toString()


                } else {
                    //통신 실패(응답코드 3xx, 4xx 등)
                    Log.d("YMC", "onResponse 실패" + response.errorBody().toString())
                }
            }

            override fun onFailure(call: Call<ApiResponse<ExtraInfoVO>>, t: Throwable) {
                //통신 실패(인터넷 끊김, 예외 발생 등 시스템적인 이유)
                Log.d("YMC", "onFailure 에러: " + t.message.toString());
            }

        })
    }


}
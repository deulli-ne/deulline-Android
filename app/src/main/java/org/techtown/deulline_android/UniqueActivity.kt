package org.techtown.deulline_android

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import org.techtown.deulline_android.databinding.ActivityUniqueBinding
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
    private lateinit var mNaverTTSTask : NaverTTSTask
    private lateinit var mTextString : Array<String>
    private lateinit var uniqueInfo : String
    private lateinit var speechRecognizer: SpeechRecognizer
    private lateinit var binding: ActivityUniqueBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_unique)

        //서버 연결
        initRetrofit()

        //통신 (상품 기본정보)
        getAdditionalInformation()

        //STT
        binding = ActivityUniqueBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        requestPermission()
        val STTButton = findViewById<Button>(R.id.STTButton)
        val textView = findViewById<TextView>(R.id.textView)


        // RecognizerIntent 생성
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, packageName)    // 여분의 키
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR")         // 언어 설정


        // <말하기> 버튼 눌러서 음성인식 시작
        binding.STTButton.setOnClickListener {
            // 새 SpeechRecognizer 를 만드는 팩토리 메서드
            speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this@UniqueActivity)
            speechRecognizer.setRecognitionListener(recognitionListener)    // 리스너 설정
            speechRecognizer.startListening(intent)                         // 듣기 시작
        }


        //TTS
        val button = findViewById<Button>(R.id.button)
        button.setOnClickListener {

            mTextString = arrayOf("상품에 대한 특이사항 입니다. 특이사항은 " + uniqueInfo + " 입니다.")
            mNaverTTSTask = NaverTTSTask(mTextString)
            mNaverTTSTask.execute(arrayOf("상품에 대한 특이사항 입니다. 특이사항은 " + uniqueInfo + " 입니다."))

            //TTS
            Handler().postDelayed({
                mTextString = arrayOf("해당 상품의 추가 정보를 듣고 싶으시다면 추가정보를 말씀해주세요")
                mNaverTTSTask = NaverTTSTask(mTextString)
                mNaverTTSTask.execute(arrayOf("해당 상품의 추가 정보를 듣고 싶으시다면 추가정보를 말씀해주세요"))
            }, 7000)

            //
            textView.text = "추가 정보"

        }




        //화면 연결
        val backBtn = findViewById<ImageView>(R.id.back)
        backBtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val homeBtn = findViewById<ImageView>(R.id.home)
        homeBtn.setOnClickListener {
            // ==== 페이지 이동 오류 수정
            if (textView.text.toString() == "추가정보" || textView.text.toString() == "추가 정보") {
                val intent = Intent(this, ExtraInfoActivity::class.java)
                startActivity(intent)
            } else {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }
    }

    //서버 연결
    private fun initRetrofit() {
        retrofit = RetofitClient.getInstance()
        retrofitService = retrofit.create(RetrofitService::class.java)
    }

    //통신: 상품 특이정보 가져오기
    fun getAdditionalInformation() {
        retrofitService.getAdditionalInfo(6)?.enqueue(object :
            Callback<ApiResponse<AdditionalInfoVO>> {
            override fun onResponse(call: Call<ApiResponse<AdditionalInfoVO>>, response: Response<ApiResponse<AdditionalInfoVO>>) {
                if(response.isSuccessful) {
                    //정상적으로 통신 성공
                    val result: ApiResponse<AdditionalInfoVO>? = response.body()
                    val data = result?.getData();

                    Log.d("AdditionalInfoVO", "onresponse 성공: "+ result?.toString())
                    Log.d("AdditionalInfoVO", "data : "+ data?.toString())
                    uniqueInfo = data?.additionalData.toString()


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

    // 권한 설정 메소드
    private fun requestPermission() {
        // 버전 체크, 권한 허용했는지 체크
        if (Build.VERSION.SDK_INT >= 23 &&
            ContextCompat.checkSelfPermission(this@UniqueActivity, Manifest.permission.RECORD_AUDIO)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this@UniqueActivity,
                arrayOf(Manifest.permission.RECORD_AUDIO), 0
            )
        }
    }

    // 리스너 설정
    private val recognitionListener: RecognitionListener = object : RecognitionListener {
        // 말하기 시작할 준비가되면 호출
        override fun onReadyForSpeech(params: Bundle) {
            Toast.makeText(applicationContext, "음성인식 시작", Toast.LENGTH_SHORT).show()
            binding.textView.text = "이제 말씀하세요!"
        }

        // 말하기 시작했을 때 호출
        override fun onBeginningOfSpeech() {
            binding.textView.text = "잘 듣고 있어요."
        }

        // 입력받는 소리의 크기를 알려줌
        override fun onRmsChanged(rmsdB: Float) {}

        // 말을 시작하고 인식이 된 단어를 buffer에 담음
        override fun onBufferReceived(buffer: ByteArray) {}

        // 말하기를 중지하면 호출
        override fun onEndOfSpeech() {
            binding.textView.text = "끝!"
        }

        // 오류 발생했을 때 호출
        override fun onError(error: Int) {
            val message = when (error) {
                SpeechRecognizer.ERROR_AUDIO -> "오디오 에러"
                SpeechRecognizer.ERROR_CLIENT -> "클라이언트 에러"
                SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "퍼미션 없음"
                SpeechRecognizer.ERROR_NETWORK -> "네트워크 에러"
                SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "네트웍 타임아웃"
                SpeechRecognizer.ERROR_NO_MATCH -> "찾을 수 없음"
                SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "RECOGNIZER 가 바쁨"
                SpeechRecognizer.ERROR_SERVER -> "서버가 이상함"
                SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "말하는 시간초과"
                else -> "알 수 없는 오류임"
            }
            binding.textView.text = "에러 발생: $message"
        }

        // 인식 결과가 준비되면 호출
        override fun onResults(results: Bundle) {
            // 말을 하면 ArrayList에 단어를 넣고 textView에 단어를 이어줌
            val matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
            for (i in matches!!.indices) binding.textView.text = matches[i]
        }

        // 부분 인식 결과를 사용할 수 있을 때 호출
        override fun onPartialResults(partialResults: Bundle) {}

        // 향후 이벤트를 추가하기 위해 예약
        override fun onEvent(eventType: Int, params: Bundle) {}
    }


}
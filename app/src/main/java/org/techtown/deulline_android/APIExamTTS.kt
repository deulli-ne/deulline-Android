package org.techtown.deulline_android

import android.media.MediaPlayer;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

//https://api.ncloud-docs.com/docs/ai-naver-clovavoice-ttspremium
//https://m.blog.naver.com/PostView.naver?isHttpsRedirect=true&blogId=jcosmoss&logNo=220963959630
class APIExamTTS {
    private val clientId = "6yn3c2ogrm" //애플리케이션 클라이언트 아이디값";
    private val clientSecret = "IQFkWlNH38ztsmaccxqGimlLDhsMALddT494huvg" //애플리케이션 클라이언트 시크릿값";

    fun main(args: Array<out Array<String?>?>) {
        val clientId = clientId
        val clientSecret = clientSecret
        try {
            Log.d("TTS", args[0]!![0].toString())
            val text: String = URLEncoder.encode(args[0].toString(), "UTF-8") // 13자
            val apiURL = "https://openapi.naver.com/v1/voice/tts.bin"
            val url = URL(apiURL)
            val con: HttpURLConnection = url.openConnection() as HttpURLConnection
            con.setRequestMethod("POST")
            con.setRequestProperty("X-Naver-Client-Id", clientId)
            con.setRequestProperty("X-Naver-Client-Secret", clientSecret)

            // post request
            val postParams = "speaker=mijin&speed=0&text=$text"
            con.setDoOutput(true)
            con.setDoInput(true)

            val wr = DataOutputStream(con.getOutputStream()) ///여기서 에러 난다?
            Log.d("TTS", wr.toString())

            wr.writeBytes(postParams)
            wr.flush()
            wr.close()
            val responseCode: Int = con.getResponseCode()
            val br: BufferedReader

            if (responseCode == 200) { // 정상 호출
                val `is`: InputStream = con.getInputStream()
                var read = 0
                val bytes = ByteArray(1024)
                //폴더 만듬. 없으면 새로 생성. 일단 Naver폴더에 저장
                val dir = File(Environment.getExternalStorageDirectory().toString() + "/", "Naver")
                if (!dir.exists()) {
                    dir.mkdirs()
                }

                val tempname = "naverttstemp" //하나의 파일명으로 덮어씀
                val f =
                    File(tempname + ".mp3")
                f.createNewFile()

                val outputStream: OutputStream = FileOutputStream(f)
                while (`is`.read(bytes).also { read = it } != -1) {
                    outputStream.write(bytes, 0, read)
                }
                `is`.close()

                //mp3파일 재생
                val Path_to_file: String = tempname + ".mp3"
                val audioPlay = MediaPlayer()

                audioPlay.setDataSource(Path_to_file)
                audioPlay.prepare()
                audioPlay.start()

            } else {  // 에러 발생
                br = BufferedReader(InputStreamReader(con.getErrorStream()))
                var inputLine: String?
                val response = StringBuffer()
                while (br.readLine().also { inputLine = it } != null) {
                    response.append(inputLine)
                }
                br.close()
            }
        } catch (e: Exception) {
            println(e)
        }
    }
}
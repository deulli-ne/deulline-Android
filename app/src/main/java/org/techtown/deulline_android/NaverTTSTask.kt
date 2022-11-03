package org.techtown.deulline_android

import android.os.AsyncTask

class NaverTTSTask(mTextString: Array<String>) : AsyncTask<Array<String?>?, Void?, String?>() {
    override fun doInBackground(vararg params: Array<String?>?): String? {
        //여기서 서버에 요청
        val tts = APIExamTTS()
        tts.main(params)
        return null
    }
    override fun onPostExecute(result: String?) {
        super.onPostExecute(result)
    }
}

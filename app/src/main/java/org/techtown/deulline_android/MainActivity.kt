package org.techtown.deulline_android

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.telecom.Call
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import org.techtown.deulline_android.retrofit.RetrofitClient
import org.techtown.deulline_android.retrofit.dto.ApiResponse
import org.techtown.deulline_android.retrofit.dto.productInfoVO

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        val homeBtn = findViewById<ImageView>(R.id.home)
        homeBtn.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}
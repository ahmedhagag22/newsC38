package com.route.newsappc38sat.ui.home.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.route.newsappc38sat.R
import com.route.newsappc38sat.ui.home.HomeActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        Handler(Looper.getMainLooper()).postDelayed(
            {

                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)

            }, 2000)
    }
}
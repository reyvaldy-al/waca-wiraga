package com.gachateam.wacawiraga.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.gachateam.wacawiraga.R
import com.gachateam.wacawiraga.ui.main.MainActivity

class SplashScreen : AppCompatActivity() {

    private var TIME_OUT:Long = 2000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        startSplashScreen()
    }

    private fun startSplashScreen() {
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }, TIME_OUT)
    }
}
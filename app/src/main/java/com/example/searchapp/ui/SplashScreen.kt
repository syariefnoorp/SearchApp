package com.example.searchapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import com.example.searchapp.R
import kotlinx.android.synthetic.main.activity_splash_screen.*

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        var counter = 4

        val countDownTimer = object : CountDownTimer(4000, 1000) {
            override fun onFinish() {
                counter = 0
            }

            override fun onTick(millisUntilFinished: Long) {
                val ticking = "Start in..$counter"
                textTicking.text = ticking
                --counter
            }
        }
        countDownTimer.start()

        Handler().postDelayed({
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            this.finish()

        }, 4000)
    }
}
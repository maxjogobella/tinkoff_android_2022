package com.example.myapplication

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        Thread {
            Thread.sleep(3000)
            val intent = MainActivity.newIntent(this)
            startActivity(intent)
        }.start()
    }
}
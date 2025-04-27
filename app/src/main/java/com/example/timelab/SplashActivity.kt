package com.example.timelab

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPref = getSharedPreferences("app_preferences", MODE_PRIVATE)
        val hasSeenSplash = sharedPref.getBoolean("has_seen_splash", false)

        if (hasSeenSplash) {
            // Пользователь уже видел SplashActivity — сразу в MainActivity
            startMainActivity()
            return
        }

        // Впервые — показываем Splash
        setContentView(R.layout.activity_splash)

        val startButton: Button = findViewById(R.id.start_button)
        val skipButton: Button = findViewById(R.id.skip_button)

        startButton.setOnClickListener {
            markSplashSeen()
            startSplashActivity2()
        }

        skipButton.setOnClickListener {
            markSplashSeen()
            startMainActivity()
        }
    }

    private fun markSplashSeen() {
        val sharedPref = getSharedPreferences("app_preferences", MODE_PRIVATE)
        sharedPref.edit().putBoolean("has_seen_splash", true).apply()
    }

    private fun startMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    }

    private fun startSplashActivity2() {
        val intent = Intent(this, SplashActivity2::class.java)
        startActivity(intent)
        finish()
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    }
}

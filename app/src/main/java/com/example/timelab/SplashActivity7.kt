package com.example.timelab

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SplashActivity7 : AppCompatActivity() {
    private lateinit var progressBar: ProgressBar
    private var currentStep: Int = 6

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash7) // Замените на имя вашего XML-файла

        progressBar = findViewById(R.id.progress_bar)

        val continueButton: Button = findViewById(R.id.continue_button)
        continueButton.setOnClickListener {
            nextStep()
            startMain()
        }
    }

    private fun nextStep() {
        if (currentStep < 7) {
            currentStep++
            progressBar.progress = currentStep
        }
    }
    private fun startMain() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish() // Завершаем текущую активити
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out) // Добавляем анимацию перехода
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out) // Добавляем анимацию возврата
    }
}
package com.example.timelab

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity

class SplashActivity2 : AppCompatActivity() {
    private lateinit var progressBar: ProgressBar
    private var currentStep: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash2) // Замените на имя вашего XML-файла

        progressBar = findViewById(R.id.progress_bar)

        val continueButton: Button = findViewById(R.id.continue_button)
        continueButton.setOnClickListener {
            nextStep() // Обновляем прогресс перед переходом
            startSplashActivity3()
        }
    }

    private fun nextStep() {
        if (currentStep < 7) {
            currentStep++
            progressBar.progress = currentStep
        }
    }

    private fun startSplashActivity3() {
        val intent = Intent(this, SplashActivity3::class.java)
        startActivity(intent)
        finish() // Завершаем текущую активити
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out) // Добавляем анимацию перехода
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out) // Добавляем анимацию возврата
    }
}

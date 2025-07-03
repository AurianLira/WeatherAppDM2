package com.example.weatherappdm2

import android.app.Application
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class WeatherAppDM2 : Application() {
    override fun onCreate() {
        super.onCreate()

        Firebase.auth.addAuthStateListener { firebaseAuth ->
            if (firebaseAuth.currentUser != null) {
                goToMain()
            } else {
                goToLogin()
            }
        }
    }

    private fun goToMain() {
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = FLAG_ACTIVITY_SINGLE_TOP or FLAG_ACTIVITY_NEW_TASK
        }
        startActivity(intent)
    }

    private fun goToLogin() {
        val intent = Intent(this, LoginActivity::class.java).apply {
            flags = FLAG_ACTIVITY_SINGLE_TOP or FLAG_ACTIVITY_NEW_TASK
        }
        startActivity(intent)
    }
}
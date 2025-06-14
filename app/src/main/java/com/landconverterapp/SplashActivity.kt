package com.landconverterapp

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

/**
 * Splash Screen Activity - Shows app logo, name, and version
 * Then automatically navigates to the main calculator screen
 */
class SplashActivity : AppCompatActivity() {

    // Duration to display splash screen (in milliseconds)
    private val SPLASH_DISPLAY_LENGTH = 2500L // 2.5 seconds (L makes it a Long)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set the layout for this activity
        setContentView(R.layout.activity_splash)

        // Hide the action bar for full screen experience
        supportActionBar?.hide()

        // Set up timer to automatically move to main activity
        Handler(Looper.getMainLooper()).postDelayed({
            // Create intent to start MainActivity
            val intent = Intent(this@SplashActivity, MainActivity::class.java)
            startActivity(intent)

            // Close splash activity so user can't go back to it
            finish()

        }, SPLASH_DISPLAY_LENGTH)
    }

    // Optional: Handle back button during splash (prevent going back)
    override fun onBackPressed() {
        super.onBackPressed()
        // Do nothing - disable back button during splash
        // This prevents user from accidentally closing the app
    }
}
package com.example.lifecycledemo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    private val TAG = "Lifecycle"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Log.d(TAG, "MainActivity: onCreate() - Activity wird erstellt")

        val btnGoToSecond = findViewById<Button>(R.id.btnGoToSecond)
        btnGoToSecond.setOnClickListener {
            val intent = Intent(this, SecondActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        Log.i(TAG, "MainActivity: onStart() - Activity wird sichtbar")
    }

    override fun onResume() {
        super.onResume()
        Log.i(TAG, "MainActivity: onResume() - Activity ist im Vordergrund")
    }

    override fun onPause() {
        super.onPause()
        Log.i(TAG, "MainActivity: onPause() - Activity verliert Fokus")
    }

    override fun onStop() {
        super.onStop()
        Log.w(TAG, "MainActivity: onStop() - Activity nicht mehr sichtbar")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e(TAG, "MainActivity: onDestroy() - Activity wird zerstört")
    }

    override fun onRestart() {
        super.onRestart()
        Log.i(TAG, "MainActivity: onRestart() - Activity wird neu gestartet")
    }
}

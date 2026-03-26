package com.example.lifecycledemo

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

class SecondActivity : AppCompatActivity() {

    private val TAG = "Lifecycle"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
        Log.d(TAG, "SecondActivity: onCreate() - Activity wird erstellt")
    }

    override fun onStart() {
        super.onStart()
        Log.i(TAG, "SecondActivity: onStart() - Activity wird sichtbar")
    }

    override fun onResume() {
        super.onResume()
        Log.i(TAG, "SecondActivity: onResume() - Activity ist im Vordergrund")
    }

    override fun onPause() {
        super.onPause()
        Log.i(TAG, "SecondActivity: onPause() - Activity verliert Fokus")
    }

    override fun onStop() {
        super.onStop()
        Log.w(TAG, "SecondActivity: onStop() - Activity nicht mehr sichtbar")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e(TAG, "SecondActivity: onDestroy() - Activity wird zerstört")
    }

    override fun onRestart() {
        super.onRestart()
        Log.i(TAG, "SecondActivity: onRestart() - Activity wird neu gestartet")
    }
}

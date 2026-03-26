package com.example.a2_1_2

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class SecondActivity : AppCompatActivity() {

    private val TAG = "Lifecycle"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "SecondActivity: onCreate")
        enableEdgeToEdge()
        setContentView(R.layout.activity_second)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    override fun onStart() {
        super.onStart()
        Log.i(TAG, "SecondActivity: onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.i(TAG, "SecondActivity: onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.i(TAG, "SecondActivity: onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.i(TAG, "SecondActivity: onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.w(TAG, "SecondActivity: onDestroy")
    }

    override fun onRestart() {
        super.onRestart()
        Log.i(TAG, "SecondActivity: onRestart")
    }
}

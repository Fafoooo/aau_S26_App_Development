package com.example.intentdemo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText

class IntentMainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intent_main)

        val etAddress = findViewById<TextInputEditText>(R.id.etAddress)
        val btnSend = findViewById<Button>(R.id.btnSendAddress)

        btnSend.setOnClickListener {
            val address = etAddress.text.toString()

            // Explicit Intent: Startet eine bestimmte Activity in unserer App
            val intent = Intent(this, MapActivity::class.java)
            intent.putExtra("ADDRESS", address)
            startActivity(intent)
        }
    }
}

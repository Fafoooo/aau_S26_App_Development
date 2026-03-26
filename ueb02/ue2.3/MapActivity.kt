package com.example.intentdemo

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MapActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        val tvAddress = findViewById<TextView>(R.id.tvAddress)
        val btnMap = findViewById<Button>(R.id.btnViewOnMap)

        // Empfangene Adresse aus dem Intent-Extra auslesen
        val address = intent.getStringExtra("ADDRESS") ?: "Keine Adresse"
        tvAddress.text = "Adresse: $address"

        btnMap.setOnClickListener {
            // Implicit Intent: Wir sagen NICHT welche App es öffnen soll.
            // Android sucht selbst eine passende App (Google Maps, etc.)
            val geoUri = Uri.parse("geo:0,0?q=${Uri.encode(address)}")
            val mapIntent = Intent(Intent.ACTION_VIEW, geoUri)

            // Prüfen ob eine App installiert ist die geo: URIs handeln kann
            if (mapIntent.resolveActivity(packageManager) != null) {
                startActivity(mapIntent)
            }
        }
    }
}

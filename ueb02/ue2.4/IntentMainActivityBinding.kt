package com.example.intentdemo

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.intentdemo.databinding.ActivityIntentMainBinding

class IntentMainActivity : AppCompatActivity() {

    // View Binding: Statt findViewById() verwenden wir die generierte Binding-Klasse
    private lateinit var binding: ActivityIntentMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Schritt 1: Binding inflaten
        binding = ActivityIntentMainBinding.inflate(layoutInflater)

        // Schritt 2: Root-View als Content setzen
        setContentView(binding.root)

        // Schritt 3: Direkt auf Views zugreifen — kein findViewById() nötig!
        binding.btnSendAddress.setOnClickListener {
            val address = binding.etAddress.text.toString()
            val intent = Intent(this, MapActivity::class.java)
            intent.putExtra("ADDRESS", address)
            startActivity(intent)
        }
    }
}

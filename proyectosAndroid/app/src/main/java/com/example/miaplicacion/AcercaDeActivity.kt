package com.example.miaplicacion

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.miaplicacion.databinding.ActivityAcercaDeBinding

class AcercaDeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAcercaDeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityAcercaDeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //val btnVolverAcerca = findViewById<Button>(R.id.btnVolverAcerca)
        //CON ESTE BOTON VOLVEMOS A LA PÁGINA PRINCIPAL

        binding.btnVolverAcerca.setOnClickListener {
            val volverAcercaDeIntent = Intent(this, MainActivity::class.java)
            startActivity(volverAcercaDeIntent)
        }
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.textArea)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
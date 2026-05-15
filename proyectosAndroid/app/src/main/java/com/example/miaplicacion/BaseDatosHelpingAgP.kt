package com.example.miaplicacion

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.miaplicacion.databinding.ActivityBaseDatosHelpingAgPBinding
import java.util.zip.Inflater

class BaseDatosHelpingAgP : AppCompatActivity() {
    private lateinit var binding: ActivityBaseDatosHelpingAgPBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityBaseDatosHelpingAgPBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.txtVolverInicio.setOnClickListener{
            val txtVolverIntent = Intent(this, MainActivity::class.java)
            startActivity(txtVolverIntent)
        }
        binding.btnRegistroTrab.setOnClickListener {
            val regTrabajador = Intent(this, ListaDeRegistroTrab::class.java)
            startActivity(regTrabajador)
        }
        binding.btnRegistroAbuelito.setOnClickListener {
            val regAbuelito = Intent (this, abuelosRegistrados::class.java)
            startActivity(regAbuelito)
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.textArea)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
}
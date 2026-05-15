package com.example.miaplicacion

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2

class OnboardingActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private lateinit var dotsLayout: LinearLayout
    private lateinit var btnNext: Button
    private lateinit var btnSkip: TextView

    private val slides = listOf(
        SlideData(
            "Bienvenido a Helping a GrandPa!",
            "La aplicación que conecta a personas mayores con sus cuidadores y familiares, pensada para ser fácil de usar.",
            "👋"
        ),
        SlideData(
            "Recordatorio de medicación",
            "Programa las tomas de tus medicamentos y recibe alertas para no olvidar ninguna dosis.",
            "💊"
        ),
        SlideData(
            "SOS Emergencia",
            "Ante cualquier emergencia, pulsa el botón SOS y enviaremos tu ubicación a tus contactos de confianza.",
            "🆘"
        ),
        SlideData(
            "Familia y salud",
            "Comparte tu calendario, historial de salud y mantén a tu círculo familiar informado en todo momento.",
            "👨‍👩‍👧‍👦"
        )
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)

        viewPager = findViewById(R.id.viewPager)
        dotsLayout = findViewById(R.id.dotsLayout)
        btnNext = findViewById(R.id.btnNext)
        btnSkip = findViewById(R.id.btnSkip)

        val adapter = OnboardingAdapter(slides)
        viewPager.adapter = adapter

        setupDots(0)
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                setupDots(position)
                btnNext.text = if (position == slides.size - 1) "Comenzar" else "Siguiente"
            }
        })

        btnNext.setOnClickListener {
            if (viewPager.currentItem < slides.size - 1) {
                viewPager.currentItem++
            } else {
                marcarVisto()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }

        btnSkip.setOnClickListener {
            marcarVisto()
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }

    private fun setupDots(active: Int) {
        dotsLayout.removeAllViews()
        slides.indices.forEach { i ->
            val dot = TextView(this).apply {
                text = if (i == active) "●" else "○"
                textSize = 24f
                setTextColor(
                    if (i == active) resources.getColor(R.color.primary, theme)
                    else resources.getColor(R.color.on_surface_variant, theme)
                )
                setPadding(8, 0, 8, 0)
            }
            dotsLayout.addView(dot)
        }
    }

    private fun marcarVisto() {
        getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
            .edit()
            .putBoolean(KEY_ONBOARDING_VISTO, true)
            .apply()
    }

    companion object {
        private const val PREFS_NAME = "onboarding_prefs"
        private const val KEY_ONBOARDING_VISTO = "onboarding_visto"

        fun necesitaMostrar(context: Context): Boolean {
            return !context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
                .getBoolean(KEY_ONBOARDING_VISTO, false)
        }
    }
}

data class SlideData(val titulo: String, val descripcion: String, val emoji: String)

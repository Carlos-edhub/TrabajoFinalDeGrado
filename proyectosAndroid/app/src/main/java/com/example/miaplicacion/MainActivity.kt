package com.example.miaplicacion

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.miaplicacion.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    private val permisosNotifLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { _ -> }

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)

        if (OnboardingActivity.necesitaMostrar(this)) {
            startActivity(Intent(this, OnboardingActivity::class.java))
            finish()
            return
        }

        enableEdgeToEdge()

        if (!PermissionHelper.hasNotificationPermission(this)) {
            permisosNotifLauncher.launch(PermissionHelper.NOTIFICATION_PERMISSION)
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        actualizarUI()

        binding.cardServicio.setOnClickListener {
            if (SessionManager.isLoggedIn()) {
                startActivity(Intent(this, ServicioActivity::class.java))
            } else {
                Toast.makeText(this, "Inicia sesión o regístrate para solicitar servicios", Toast.LENGTH_LONG).show()
            }
        }

        binding.cardTrabaja.setOnClickListener {
            startActivity(Intent(this, TrabajoActivity::class.java))
        }

        binding.cardRegistro.setOnClickListener {
            if (SessionManager.isLoggedIn()) {
                startActivity(Intent(this, PerfilActivity::class.java))
            } else {
                startActivity(Intent(this, RegistroActivity::class.java))
            }
        }

        binding.cardMedicacion.setOnClickListener {
            if (SessionManager.isLoggedIn()) {
                startActivity(Intent(this, MedicacionActivity::class.java))
            } else {
                Toast.makeText(this, "Inicia sesión para usar recordatorio de medicación", Toast.LENGTH_LONG).show()
            }
        }

        binding.cardSalud.setOnClickListener {
            if (SessionManager.isLoggedIn()) {
                startActivity(Intent(this, SaludActivity::class.java))
            } else {
                Toast.makeText(this, "Inicia sesión para usar historial de salud", Toast.LENGTH_LONG).show()
            }
        }

        binding.cardCalendario.setOnClickListener {
            if (SessionManager.isLoggedIn()) {
                startActivity(Intent(this, CalendarioActivity::class.java))
            } else {
                Toast.makeText(this, "Inicia sesión para usar el calendario", Toast.LENGTH_LONG).show()
            }
        }

        binding.cardFamilia.setOnClickListener {
            if (SessionManager.isLoggedIn()) {
                startActivity(Intent(this, FamiliaActivity::class.java))
            } else {
                Toast.makeText(this, "Inicia sesión para usar el círculo familiar", Toast.LENGTH_LONG).show()
            }
        }

        binding.btnEmergencia.setOnClickListener {
            if (SessionManager.isLoggedIn()) {
                startActivity(Intent(this, SosActivity::class.java))
            } else {
                Toast.makeText(this, "Inicia sesión para usar la función SOS", Toast.LENGTH_LONG).show()
            }
        }

        binding.txtLoginlink.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        binding.txtRegisterlink.setOnClickListener {
            startActivity(Intent(this, RegistroActivity::class.java))
        }

        binding.txtRegistrarMayor.setOnClickListener {
            startActivity(Intent(this, RegistrarAbuelitoActivity::class.java))
        }

        binding.txtAcercaDe.setOnClickListener {
            startActivity(Intent(this, AcercaDeActivity::class.java))
        }

        binding.txtHistorial.setOnClickListener {
            startActivity(Intent(this, HistorialActivity::class.java))
        }

        binding.txtOficina.setOnClickListener {
            startActivity(Intent(this, BaseDatosHelpingAgP::class.java))
        }

        binding.txtLogout.setOnClickListener {
            SessionManager.logout()
            Toast.makeText(this, "Sesión cerrada", Toast.LENGTH_LONG).show()
            actualizarUI()
        }

        ViewCompat.setOnApplyWindowInsetsListener(binding.textArea) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun actualizarUI() {
        if (SessionManager.isLoggedIn()) {
            val nombre = SessionManager.getUserName()
            binding.txtWelcome.text = "Bienvenido, $nombre"
            binding.txtWelcome.visibility = android.view.View.VISIBLE
            binding.barraLogin.visibility = android.view.View.GONE
            binding.txtLogout.visibility = android.view.View.VISIBLE
            binding.txtRegistrarMayor.visibility = android.view.View.VISIBLE
            binding.txtHistorial.visibility = android.view.View.VISIBLE
            binding.cardRegistroTexto.text = "Mi Perfil"
            binding.cardRegistroSub.text = "Ver y editar tu información"
        } else {
            binding.txtWelcome.visibility = android.view.View.GONE
            binding.barraLogin.visibility = android.view.View.VISIBLE
            binding.txtLogout.visibility = android.view.View.GONE
            binding.txtRegistrarMayor.visibility = android.view.View.GONE
            binding.txtHistorial.visibility = android.view.View.GONE
            binding.cardRegistroTexto.text = "Crear cuenta"
            binding.cardRegistroSub.text = "Regístrate para solicitar o trabajar"
        }
    }
}

package com.example.miaplicacion

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import android.telephony.SmsManager
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationServices

object SosHelper {

    fun lanzarSos(context: Context, contactos: List<ContactoEmergencia>) {
        if (contactos.isEmpty()) {
            Toast.makeText(context, "No hay contactos de emergencia. Añade al menos uno.", Toast.LENGTH_LONG).show()
            return
        }

        obtenerUbicacion(context) { ubicacion ->
            val mensaje = buildString {
                append("¡EMERGENCIA! Soy ${
                    try { SessionManager.getUserName() } catch (e: Exception) { "un usuario" }
                } y necesito ayuda urgente.")
                if (ubicacion != null) {
                    append("\n\nMi ubicación: https://maps.google.com/?q=${ubicacion.latitude},${ubicacion.longitude}")
                }
            }

            for (contacto in contactos) {
                enviarSms(context, contacto.telefono, mensaje)
                enviarWhatsApp(context, contacto.telefono, mensaje)
                llamar(context, contacto.telefono)
            }

            Toast.makeText(context, "SOS enviado a ${contactos.size} contacto(s)", Toast.LENGTH_LONG).show()
        }
    }

    private fun enviarWhatsApp(context: Context, telefono: String, mensaje: String) {
        try {
            val uri = Uri.parse("https://api.whatsapp.com/send?phone=$telefono&text=${Uri.encode(mensaje)}")
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = uri
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            if (intent.resolveActivity(context.packageManager) != null) {
                context.startActivity(intent)
            }
        } catch (_: Exception) { }
    }

    private fun enviarSms(context: Context, telefono: String, mensaje: String) {
        try {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.SEND_SMS)
                == PackageManager.PERMISSION_GRANTED
            ) {
                SmsManager.getDefault().sendTextMessage(telefono, null, mensaje, null, null)
            } else {
                val intent = Intent(Intent.ACTION_VIEW).apply {
                    data = Uri.parse("sms:$telefono")
                    putExtra("sms_body", mensaje)
                }
                context.startActivity(intent)
            }
        } catch (e: Exception) {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("sms:$telefono")
                putExtra("sms_body", mensaje)
            }
            context.startActivity(intent)
        }
    }

    private fun llamar(context: Context, telefono: String) {
        try {
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE)
                == PackageManager.PERMISSION_GRANTED
            ) {
                val intent = Intent(Intent.ACTION_CALL).apply {
                    data = Uri.parse("tel:$telefono")
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
                context.startActivity(intent)
            } else {
                val intent = Intent(Intent.ACTION_DIAL).apply {
                    data = Uri.parse("tel:$telefono")
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK
                }
                context.startActivity(intent)
            }
        } catch (e: Exception) {
            val intent = Intent(Intent.ACTION_DIAL).apply {
                data = Uri.parse("tel:$telefono")
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        }
    }

    private fun obtenerUbicacion(context: Context, onResult: (Location?) -> Unit) {
        try {
            val fusedClient = LocationServices.getFusedLocationProviderClient(context)
            if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
            ) {
                fusedClient.lastLocation.addOnSuccessListener { location ->
                    onResult(location)
                }.addOnFailureListener {
                    onResult(null)
                }
            } else {
                onResult(null)
            }
        } catch (e: Exception) {
            onResult(null)
        }
    }
}

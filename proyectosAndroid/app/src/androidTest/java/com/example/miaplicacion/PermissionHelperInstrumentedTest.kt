package com.example.miaplicacion

import android.Manifest
import android.content.pm.PackageManager
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PermissionHelperInstrumentedTest {

    @Test
    fun sosPermissions_incluyePermisosNecesarios() {
        val permissions = PermissionHelper.SOS_PERMISSIONS
        assertTrue(permissions.contains(Manifest.permission.SEND_SMS))
        assertTrue(permissions.contains(Manifest.permission.CALL_PHONE))
        assertTrue(permissions.contains(Manifest.permission.ACCESS_FINE_LOCATION))
    }

    @Test
    fun notificationPermission_soloEnTiramisuOsuperior() {
        val permissions = PermissionHelper.NOTIFICATION_PERMISSION
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
            assertTrue(permissions.contains(Manifest.permission.POST_NOTIFICATIONS))
        } else {
            assertTrue(permissions.isEmpty())
        }
    }

    @Test
    fun hasSosPermissions_RecienInstalado_devuelveFalse() {
        val context = ApplicationProvider.getApplicationContext<android.content.Context>()
        assertFalse(PermissionHelper.hasSosPermissions(context))
    }

    @Test
    fun hasNotificationPermission_enDispositivoAntiguo_devuelveTrue() {
        val context = ApplicationProvider.getApplicationContext<android.content.Context>()
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.TIRAMISU) {
            assertTrue(PermissionHelper.hasNotificationPermission(context))
        }
    }
}

# 5. Implementación

## 5.1 Tecnologías Utilizadas

| Tecnología | Versión | Propósito |
|-----------|---------|-----------|
| Kotlin | 2.0.21 | Lenguaje de programación |
| Android Gradle Plugin | 8.13.1 | Compilación y build |
| Room | 2.6.1 | Base de datos local SQLite |
| WorkManager | 2.10.0 | Trabajos en segundo plano |
| Firebase BoM | 33.12.0 | Plataforma cloud |
| Google Location | 21.3.0 | Servicios de localización GPS |
| Material 3 | — | Componentes de interfaz |
| SplashScreen | 1.0.1 | Pantalla de bienvenida |
| ViewPager2 | — | Onboarding deslizable |

## 5.2 Estructura del Proyecto

El proyecto contiene **62 archivos Kotlin** organizados en:

### Modelos (9 entidades Room)
- `UserEntity.kt` — Usuarios
- `Medicacion.kt` — Medicamentos
- `ContactoEmergencia.kt` — Contactos SOS
- `RegistroSalud.kt` — Registros de salud
- `EventoCalendario.kt` — Eventos
- `MiembroFamiliar.kt` — Familiares
- `Abuelito.kt` — Mayores registrados
- `ServicioHistorial.kt` — Historial servicios
- `Trabajador.kt` — Trabajadores/cuidadores

### DAOs (9 interfaces)
Cada entidad tiene su DAO con operaciones CRUD y consultas Flow.

### Actividades (24 pantallas)
Las actividades principales se organizan por funcionalidad:
- **Autenticación**: LoginActivity, RegistroActivity, PerfilActivity
- **Principal**: MainActivity (hub), OnboardingActivity
- **Medicación**: MedicacionActivity, AgregarMedicacionActivity (add/edit)
- **SOS**: SosActivity, AgregarContactoActivity
- **Salud**: SaludActivity, AgregarSaludActivity, SaludGraficaActivity
- **Calendario**: CalendarioActivity, AgregarEventoActivity
- **Familia**: FamiliaActivity, AgregarMiembroActivity
- **Servicios**: ServicioActivity, TrabajoActivity, HistorialActivity

### Helpers (7 utilidades)
- `SessionManager` — Gestión de sesión SharedPreferences
- `NotificacionHelper` — 3 canales de notificación
- `PermissionHelper` — Permisos runtime
- `SosHelper` — SMS + WhatsApp + llamada + GPS
- `CsvExporter` — Exportación CSV con FileProvider
- `LineChartView` — Gráfica de línea Canvas personalizada
- `TrabajadorManager` — Lista en memoria de trabajadores

### Firebase (3 clases)
- `FirebaseManager` — Singleton de Firebase Auth + Firestore
- `FirestoreSyncHelper` — Sincronización bidireccional Room-Firestore
- `FCMService` — Notificaciones push Firebase Cloud Messaging

### Background (5 clases)
- `HelpingApp` — Application class con inicializaciones
- `MedicacionWorkScheduler` — Programador WorkManager
- `MedicacionWorker` — Worker de recordatorio
- `AlarmaMedicacionReceiver` — BroadcastReceiver legacy
- `BootReceiver` — Reprogramación al reiniciar

## 5.3 Funcionalidades Clave

### Recordatorio de Medicación (WorkManager)
```
Usuario → AgregarMedicacionActivity → MedicacionDao.insert()
         → MedicacionWorkScheduler.programar()
         → OneTimeWorkRequest<MedicacionWorker>
         → NotificacionHelper.mostrarNotificacion()

BootReceiver (ACTION_BOOT_COMPLETED)
         → Recorre medicaciones activas
         → Reprograma cada una
```

### SOS Multicanal
```
Usuario → SosActivity → PermissionHelper
         → Confirmación diálogo
         → SosHelper.lanzarSos()
         → obtenerUbicacion() [FusedLocationProviderClient]
         → enviarSms() [SmsManager]
         → enviarWhatsApp() [Intent ACTION_VIEW]
         → llamar() [Intent ACTION_CALL]
```

### Gráficas de Salud (Canvas)
```
SaludGraficaActivity → LineChartView (Custom View)
         → onDraw() Canvas
         → Dibuja ejes, puntos, línea
         → Escala automática
         → Colores por tipo
```

### Sincronización Firestore
```
AgregarEvento → EventoCalendarioDao.insert()
             → FirestoreSyncHelper.syncEvento()
             → DocumentReference.set()

SnapshotListener → Detecta cambios remotos
                 → Guarda localmente
```

## 5.4 Accesibilidad

La interfaz se ha diseñado específicamente para personas mayores:

- **Texto grande**: Mínimo 18sp para cuerpo, 36sp para encabezados
- **Alto contraste**: Negro sobre blanco, azul oscuro como primario
- **Botones grandes**: 64dp mínimo de altura, 80dp el de emergencia
- **Espaciado generoso**: Padding de 24dp en tarjetas
- **Iconos claros**: Vectoriales, con colores distintivos por funcionalidad
- **Onboarding**: 4 slides explicativos al primer inicio
- **Animaciones suaves**: Transiciones slide para no desorientar

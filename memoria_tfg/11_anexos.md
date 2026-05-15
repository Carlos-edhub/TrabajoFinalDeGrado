# Anexos

## Anexo A: Guía de Instalación

### Requisitos
- Dispositivo Android con versión 8.1 (API 27) o superior
- Permisos: SMS, llamada, ubicación, notificaciones (se solicitan en runtime)
- Conexión a internet (opcional, para sincronización Firebase)

### Pasos
1. Descargar el APK de la aplicación
2. Permitir instalación de orígenes desconocidos si es necesario
3. Abrir la aplicación y completar el onboarding
4. Registrarse o iniciar sesión
5. Configurar contactos de emergencia y medicaciones

## Anexo B: Enlaces

- **Repositorio del proyecto**: [URL del repositorio]
- **Landing page**: [URL de la landing page]
- **APK de la aplicación**: [URL de descarga]

## Anexo C: Lista de Archivos del Proyecto

### Android App (62 archivos Kotlin)
```
app/src/main/java/com/example/miaplicacion/
├── Abuelito.kt
├── AbuelitoDao.kt
├── AcercaDeActivity.kt
├── AgregarContactoActivity.kt
├── AgregarEventoActivity.kt
├── AgregarMedicacionActivity.kt
├── AgregarMiembroActivity.kt
├── AgregarSaludActivity.kt
├── AlarmaMedicacionReceiver.kt
├── AppDatabase.kt
├── BaseDatosHelpingAgP.kt
├── BootReceiver.kt
├── CalendarioActivity.kt
├── ContactoEmergencia.kt
├── ContactoEmergenciaAdapter.kt
├── ContactoEmergenciaDao.kt
├── CsvExporter.kt
├── EventoCalendario.kt
├── EventoCalendarioAdapter.kt
├── EventoCalendarioDao.kt
├── FamiliaActivity.kt
├── FCMService.kt
├── FirebaseManager.kt
├── FirestoreSyncHelper.kt
├── HelpingApp.kt
├── HistorialActivity.kt
├── LineChartView.kt
├── ListaDeRegistroTrab.kt
├── LoginActivity.kt
├── MainActivity.kt
├── Medicacion.kt
├── MedicacionAdapter.kt
├── MedicacionActivity.kt
├── MedicacionDao.kt
├── MedicacionWorkScheduler.kt
├── MedicacionWorker.kt
├── MiembroFamiliar.kt
├── MiembroFamiliarAdapter.kt
├── MiembroFamiliarDao.kt
├── NotificacionHelper.kt
├── OnboardingAdapter.kt
├── OnboardingActivity.kt
├── PerfilActivity.kt
├── PermissionHelper.kt
├── RegistrarAbuelitoActivity.kt
├── RegistroActivity.kt
├── RegistroSalud.kt
├── RegistroSaludAdapter.kt
├── RegistroSaludDao.kt
├── SaludActivity.kt
├── SaludGraficaActivity.kt
├── ServicioActivity.kt
├── ServicioHistorial.kt
├── ServicioHistorialDao.kt
├── SessionManager.kt
├── SosActivity.kt
├── SosHelper.kt
├── Trabajador.kt
├── TrabajadorDao.kt
├── TrabajadorManager.kt
├── TrabajoActivity.kt
├── UserDao.kt
└── UserEntity.kt
```

### Landing Page
```
landing-page/
├── dist/                          → Build de producción
├── public/
│   └── favicon.svg
├── src/
│   ├── App.jsx                    → Componente principal
│   ├── App.css
│   ├── index.css                  → Estilos con animaciones
│   └── main.jsx
├── index.html
├── package.json
└── vite.config.js
```

### Memoria TFG
```
memoria_tfg/
├── 01_portada.md
├── 02_resumen.md
├── 03_introduccion.md
├── 04_estado_del_arte.md
├── 05_planificacion.md
├── 06_analisis_diseno.md
├── 07_implementacion.md
├── 08_resultados.md
├── 09_conclusiones.md
├── 10_bibliografia.md
└── 11_anexos.md
```

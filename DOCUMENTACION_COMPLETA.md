# Helping a GrandPa! — Documentación del Proyecto

## 1. Base de Datos

**Tecnología:** Room (SQLite) — integrado en Android, sin servidor externo.

**Ubicación en el dispositivo:**
```
/data/data/com.example.miaplicacion/databases/helping_grandpa_db
```

**Tablas creadas:**

| Tabla | Descripción |
|-------|-------------|
| `usuarios` | Usuarios registrados (nombre, email, contraseña, rol) |
| `trabajadores` | Solicitudes de trabajo |
| `abuelitos` | Mayores registrados para cuidado |
| `servicios_historial` | Historial de servicios solicitados |

**Archivos clave:**
- `AppDatabase.kt` — Singleton de la BD (versión 3, migración destructiva)
- `UserEntity.kt` / `UserDao.kt` — Usuarios
- `Abuelito.kt` / `AbuelitoDao.kt` — Mayores
- `ServicioHistorial.kt` / `ServicioHistorialDao.kt` — Historial
- `Trabajador.kt` / `TrabajadorDao.kt` — Trabajadores

---

## 2. Funcionalidades Implementadas (paso a paso)

### ✅ Paso 1 — Pantalla de Login con sesión
- `SessionManager.kt` — Guarda sesión en SharedPreferences (persistente aunque cierres la app)
- `LoginActivity.kt` — Email + contraseña, consulta BD local
- Al abrir la app, si no hay sesión → redirige al Login
- Si hay sesión → muestra "Bienvenido, [nombre]"
- Opción "Cerrar sesión" visible solo cuando estás logueado

### ✅ Paso 2 — Perfiles de usuario (roles)
- Registro con selector de rol: **Usuario (Mayor)** / **Cuidador** / **Profesional**
- El rol se guarda en la tabla `usuarios`
- Preparado para adaptar la UI según el rol

### ✅ Paso 3 — Pantalla "Mi Perfil"
- `PerfilActivity.kt` — Ver y editar nombre, apellidos, email, fecha, rol
- Guarda cambios en BD y actualiza la sesión
- La tarjeta de la pantalla principal cambia de "Crear cuenta" a "Mi Perfil" según sesión

### ✅ Paso 4 — Abuelito funcional (entidad Room + formulario)
- `Abuelito.kt` — Entidad con: nombre, apellidos, edad, dirección, teléfono, necesidades
- `RegistrarAbuelitoActivity.kt` — Formulario completo para registrar un mayor
- `abuelosRegistrados.kt` — RecyclerView que lista todos los mayores registrados (datos en vivo desde Room)
- Enlace "Registrar un mayor" visible solo con sesión activa

### ✅ Paso 5 — Historial de servicios
- `ServicioHistorial.kt` — Guarda cada solicitud: horas médicas, compras, total, precio, fecha
- `ServicioActivity.kt` actualizado — Al solicitar un servicio se guarda automáticamente en BD
- `HistorialActivity.kt` — RecyclerView con todo el historial del usuario logueado

### Funcionalidades anteriores (base)
- Pantalla principal con tarjetas premium (MaterialCardView 16dp radius)
- Botón SOS Emergencia (placeholder)
- Formulario de servicios (citas médicas + compras, cálculo de precio)
- Formulario "Trabaja con nosotros"
- Pantalla "Acerca de"
- Modo Desarrollador con acceso a BD y abuelitos registrados
- Paleta de colores tipo iOS (azul #007AFF, verde #34C759, rojo #FF3B30)

---

## 3. Arquitectura del Proyecto

```
proyectosAndroid/
├── app/
│   ├── build.gradle.kts          # Dependencias: Room 2.6.1, Material3, ViewBinding
│   └── src/main/
│       ├── AndroidManifest.xml    # 11 actividades registradas
│       ├── java/com/example/miaplicacion/
│       │   ├── MainActivity.kt           # Pantalla principal con sesión
│       │   ├── LoginActivity.kt          # Login
│       │   ├── RegistroActivity.kt       # Registro con selector de rol
│       │   ├── PerfilActivity.kt         # Mi Perfil
│       │   ├── ServicioActivity.kt       # Solicitar servicio
│       │   ├── TrabajoActivity.kt        # Trabaja con nosotros
│       │   ├── AcercaDeActivity.kt       # Acerca de
│       │   ├── HistorialActivity.kt      # Historial de servicios
│       │   ├── RegistrarAbuelitoActivity.kt  # Registrar mayor
│       │   ├── abuelosRegistrados.kt     # Lista de mayores
│       │   ├── BaseDatosHelpingAgP.kt    # Modo desarrollador
│       │   ├── SessionManager.kt         # Gestión de sesión
│       │   ├── AppDatabase.kt            # Room Database (singleton)
│       │   ├── UserEntity.kt / UserDao.kt
│       │   ├── Abuelito.kt / AbuelitoDao.kt
│       │   ├── ServicioHistorial.kt / ServicioHistorialDao.kt
│       │   └── Trabajador.kt / TrabajadorDao.kt
│       └── res/
│           ├── layout/                   # 11 archivos XML
│           ├── values/
│           │   ├── colors.xml            # Paleta premium iOS
│           │   ├── themes.xml            # Tema Material3 + estilos
│           │   └── strings.xml
│           └── drawable/                 # Imágenes y shapes
├── gradle/
│   └── libs.versions.toml        # Catálogo de versiones
└── build.gradle.kts              # Configuración raíz
```

---

## 4. Mejoras de diseño implementadas

| Mejora | Estado | Detalle |
|--------|--------|---------|
| **Toolbar profesional** | ✅ | Reemplazados todos los botones "Volver" por MaterialToolbar con botón de retroceso y título |
| **Modo oscuro** | ✅ | Tema nocturno automático vía `values-night/themes.xml` con colores oscuros premium |
| **Splash Screen** | ✅ | Pantalla de bienvenida con icono y color primario al abrir la app |
| **DatePicker** | ✅ | Campos de fecha ahora abren calendario visual en vez de escribir a mano |
| **Animaciones** | ✅ | Transiciones slide-in/out entre pantallas (300ms) |
| **RecyclerViews Material** | ✅ | Listas de abuelitos e historial con MaterialCardView, iconos y diseño premium |
| **Iconos en tarjetas** | ✅ | Círculos de color con iconos en cada tarjeta de la pantalla principal |
| **Tipografía** | ✅ | Estilos de texto Material3 con line-spacing, kerning y jerarquía visual |

### Antes vs. Después

| Aspecto | Antes | Después |
|---------|-------|---------|
| Navegación | Botón "Volver" genérico | Toolbar con flecha y título |
| Temas | Solo modo claro | Claro + Oscuro automático |
| Campos fecha | Escribir a mano | DatePicker visual |
| Transiciones | Instantáneas | Slide animado |
| Listas | `simple_list_item_2` genérico | Cards personalizadas con iconos |
| Tarjetas inicio | Solo texto | Icono + texto + flecha |

## 5. Próximas mejoras recomendadas

| Mejora | Descripción | Dificultad |
|--------|-------------|------------|
| **Bottom Navigation** | Barra inferior con 3-4 pestañas (Inicio, Perfil, Historial) | Media |
| **Material You** | Colores que se adaptan al fondo de pantalla (Android 12+) | Baja |
| **Shimmer / Lottie** | Animaciones de carga y estados vacíos | Media |
| **Notificaciones** | Alertas locales para recordatorio de servicios | Media |
| **Filtros/búsqueda** | Buscar abuelitos o servicios por nombre/fecha | Media |
| **Exportar BD** | Opción para copiar la BD a Descargas | Baja |

---

## 5. Compilación y ejecución

La app compila correctamente con:
- **minSdk:** 27 (Android 8.1)
- **targetSdk:** 36 (Android 16)
- **Kotlin:** 2.0.21
- **AGP:** 8.13.1
- **Gradle:** 8.13

Para compilar:
```bash
cd proyectosAndroid
gradlew assembleDebug
```

El APK generado está en:
```
app/build/outputs/apk/debug/app-debug.apk
```

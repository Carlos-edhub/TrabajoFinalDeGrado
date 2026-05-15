# 3. Planificación y Metodología

## 3.1 Metodología

Se ha seguido una metodología ágil adaptada al trabajo individual, con ciclos de desarrollo iterativos de una semana de duración. Cada iteración incluye:

1. **Planificación**: Definición de objetivos de la iteración
2. **Diseño**: Diseño de la solución
3. **Implementación**: Codificación de la funcionalidad
4. **Pruebas**: Verificación del funcionamiento
5. **Revisión**: Evaluación del resultado

## 3.2 Cronograma

| Fase | Semanas | Tareas |
|------|---------|--------|
| **Fase 1: Planificación** | Semana 1-2 | Análisis de requisitos, diseño de arquitectura, selección de tecnologías |
| **Fase 2: Core** | Semana 3-5 | Base de datos Room, autenticación, sesión, navegación principal |
| **Fase 3: Medicación** | Semana 6-7 | CRUD medicación, WorkManager, notificaciones, BootReceiver |
| **Fase 4: SOS** | Semana 8-9 | Contactos emergencia, SMS, llamada, GPS, WhatsApp, permisos |
| **Fase 5: Salud** | Semana 10-11 | Registros salud, CSV, gráficas personalizadas |
| **Fase 6: Calendario y Familia** | Semana 12-13 | CRUD eventos/miembros, sincronización Firestore |
| **Fase 7: Firebase** | Semana 14 | Firebase Auth, Firestore, FCM, google-services.json |
| **Fase 8: UI/UX** | Semana 15 | Temas accesibles, iconos vectoriales, onboarding |
| **Fase 9: Pruebas** | Semana 16 | Tests unitarios, tests instrumentados, build final |
| **Fase 10: Memoria** | Semana 17-18 | Documentación, landing page, memoria TFG |

## 3.3 Recursos

### Hardware
- Ordenador con Windows 11, 16GB RAM
- Dispositivo Android físico para pruebas (API 27+)
- Emulador Android

### Software
- Android Studio Ladybug Feature Drop 2024.3.1
- JDK 17
- Gradle 8.13
- Firebase Console
- Git para control de versiones
- Node.js y npm para landing page

### Herramientas de desarrollo
- Kotlin 2.0.21
- Android SDK (minSdk 27, targetSdk 36)
- Firebase BoM 33.12.0
- Room 2.6.1
- WorkManager 2.10.0

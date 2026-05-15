# 7. Conclusiones y Trabajo Futuro

## 7.1 Conclusiones

El desarrollo de **Helping a GrandPa!** ha demostrado que es posible crear una aplicación Android integral que cubra las necesidades fundamentales de cuidado de personas mayores en una sola herramienta accesible y fiable.

### Logros Principales
1. **Integración completa**: Cinco funcionalidades en una sola app con navegación unificada.
2. **Fiabilidad**: WorkManager garantiza recordatorios incluso tras reinicios.
3. **Accesibilidad**: Interfaz diseñada específicamente para el público objetivo.
4. **Offline-first**: Todas las funciones principales funcionan sin conexión.
5. **Código limpio**: 62 clases Kotlin organizadas y documentadas.
6. **Tests**: 20 tests (10 unit + 10 instrumentados) todos pasando.

### Dificultades Encontradas
1. **Permisos runtime Android**: La gestión de permisos para SMS, llamada y ubicación requiere múltiples flujos y comprobaciones.
2. **WorkManager vs AlarmManager**: La migración a WorkManager para recordatorios persistentes requirió rediseño del sistema de alarmas.
3. **Firebase offline**: La configuración de Firebase para funcionar en modo offline sin google-services.json real fue un desafío.
4. **Canvas personalizado**: El desarrollo de LineChartView desde cero requirió cálculos matemáticos precisos para escalado y posicionamiento.

## 7.2 Trabajo Futuro

### Mejoras Previstas
1. **Detección de caídas**: Usar sensores del dispositivo para detectar caídas automáticamente.
2. **Wear OS**: Versión para smartwatch con visualización rápida de medicación y SOS.
3. **Recordatorios por voz**: Síntesis de voz para leer recordatorios en voz alta.
4. **Informes médicos**: Generación de PDF con historial de salud para compartir con médicos.
5. **Notificaciones programadas**: Recordatorios de citas médicas con geolocalización.
6. **Modo emergencia automático**: Detección de inactividad prolongada y activación automática de SOS.
7. **Soporte multilingüe**: Además de español, añadir inglés y otros idiomas.
8. **Widgets**: Widget de escritorio con próxima medicación y botón SOS rápido.

### Publicación
- Generar APK firmado para distribución
- Publicar en Google Play Store
- Landing page promocional en GitHub Pages

## 7.3 Reflexión Personal

Este proyecto ha supuesto un desafío integral que ha puesto a prueba los conocimientos adquiridos durante el ciclo formativo. Desde la arquitectura de bases de datos con Room, pasando por la programación reactiva con Flow, hasta la integración con servicios cloud de Firebase, cada fase ha aportado un aprendizaje significativo.

El enfoque en la accesibilidad para personas mayores ha sido especialmente gratificante, al comprobar cómo la tecnología puede mejorar la calidad de vida de este colectivo.

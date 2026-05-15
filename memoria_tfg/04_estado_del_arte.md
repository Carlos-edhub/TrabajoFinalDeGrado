# 2. Estado del Arte

## 2.1 Análisis de Soluciones Existentes

Existen diversas aplicaciones en el mercado que abordan necesidades similares:

### Aplicaciones de Recordatorio de Medicación

| Aplicación | Puntos Fuertes | Debilidades |
|-----------|---------------|-------------|
| Medisafe | Recordatorios visuales, informes | Interfaz en inglés, sin SOS |
| Pill Reminder | Simple, notificaciones | Sin cloud, sin familia |
| MyTherapy | Gamificación, informes | Compleja para mayores |

### Aplicaciones de Seguridad/Emergencia

| Aplicación | Puntos Fuertes | Debilidades |
|-----------|---------------|-------------|
| R-U there | Detección de caídas | Suscripción de pago |
| AlertCops | Conexión con policía | No enfocada a mayores |
| Life360 | Localización familiar | Sin botón SOS directo |

### Aplicaciones de Salud

| Aplicación | Puntos Fuertes | Debilidades |
|-----------|---------------|-------------|
| Google Fit | Integración con wearables | Sin registro manual detallado |
| Samsung Health | Múltiples métricas | Interfaz densa, letra pequeña |
| Mi Salud | En español, sencilla | Sin gráficas, sin exportación |

### Conclusiones del Análisis

Ninguna aplicación existente integra **todas** las funcionalidades necesarias en una sola herramienta adaptada específicamente a personas mayores:
- Recordatorio de medicación persistente
- SOS multicanal con ubicación
- Seguimiento de salud con gráficas
- Calendario compartido familiar
- Círculo familiar con roles
- Interfaz accesible (texto grande, alto contraste)
- Funcionamiento offline

## 2.2 Tecnologías del Ecosistema Android

### Kotlin
Lenguaje oficial de Android desde 2019. Ofrece:
- Interoperabilidad total con Java
- Corrutinas para programación asíncrona
- Null safety
- Data classes para modelos de datos
- Extension functions

### Jetpack Room
Biblioteca de persistencia que proporciona una capa de abstracción sobre SQLite:
- Compilación de consultas SQL en tiempo de compilación
- Integración con LiveData y Flow
- Soporte para corrutinas
- Migraciones de esquema

### Firebase
Plataforma de Google para desarrollo de aplicaciones:
- **Authentication**: Autenticación de usuarios
- **Cloud Firestore**: Base de datos NoSQL en tiempo real
- **Cloud Messaging**: Notificaciones push
- Integración nativa con Android

### WorkManager
Biblioteca para trabajos en segundo plano:
- Garantiza la ejecución incluso si la app se cierra
- Compatible con Doze mode y batería
- Trabajos persistentes que sobreviven a reinicios
- API unificada para versiones antiguas y modernas

### Material Design 3
Sistema de diseño de Google:
- Componentes adaptables
- Temas dinámicos (Material You)
- Principios de accesibilidad integrados
- Animaciones y transiciones

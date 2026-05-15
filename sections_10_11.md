# SECCIONES 10 Y 11: MANTENIMIENTO, MEJORAS FUTURAS Y CONCLUSIONES

---

## 10. MANTENIMIENTO Y MEJORAS FUTURAS

El desarrollo de una aplicación de cuidados para personas mayores en España constituye un proyecto vivo que requiere una visión a largo plazo. En este capítulo se analizan las posibles ampliaciones del sistema, las mejoras técnicas pendientes, la estrategia de compatibilidad multiplataforma y el plan de escalabilidad que garantizará la viabilidad del proyecto a medida que crezca su base de usuarios.

---

### 10.1 Posibles ampliaciones

A continuación se detallan diez líneas de evolución del sistema, ordenadas por impacto potencial y viabilidad técnica, que representan oportunidades reales de crecimiento para la aplicación.

#### 1. Compañero conversacional con inteligencia artificial

**Descripción:** Integración de un asistente conversacional basado en modelos de lenguaje de gran tamaño (LLM) diseñado específicamente para ofrecer compañía emocional a personas mayores. El sistema mantendría conversaciones naturales, recordaría preferencias personales y detectaría señales de soledad o deterioro cognitivo mediante análisis de sentimiento.

**Utilidad:** La soledad no deseada afecta a más del 25% de las personas mayores de 65 años en España. Un compañero conversacional disponible 24/7 podría reducir significativamente el aislamiento social y proporcionar detección temprana de problemas de salud mental.

**Enfoque técnico:** Se utilizaría la API de OpenAI (GPT-4o mini por su relación coste-rendimiento) o un modelo open-source como Llama 3.1 desplegado en infraestructura propia mediante Firebase Cloud Functions. La arquitectura incluiría:
- Un sistema de *prompt engineering* con personalidad adaptada al perfil del usuario
- Memoria contextual persistente mediante Firebase Firestore
- Filtros de seguridad y moderación de contenido en tiempo real
- Integración con el módulo de alertas para escalar situaciones de riesgo

**Complejidad estimada:** Alta (8-12 semanas de desarrollo). Requiere inversión significativa en diseño conversacional, pruebas con usuarios reales y cumplimiento normativo adicional para el procesamiento de datos sensibles con IA.

#### 2. Telemedicina avanzada con gemelos digitales

**Descripción:** Creación de un "gemelo digital" de salud para cada usuario: un modelo computacional que simula el estado fisiológico del paciente basándose en datos históricos y en tiempo real provenientes de dispositivos IoT, historial médico y patrones de comportamiento.

**Utilidad:** Permitiría a los profesionales sanitarios realizar simulaciones predictivas, ajustar tratamientos de forma personalizada y detectar anomalías antes de que se manifiesten clínicamente, reduciendo hospitalizaciones evitables.

**Enfoque técnico:**
- Motor de simulación ejecutado en el servidor (Python/TensorFlow) accesible vía REST API
- Almacenamiento de series temporales en Firebase Firestore con agregaciones precalculadas
- Dashboard médico con visualizaciones interactivas (charts, heatmaps)
- Integración con protocolos HL7 FHIR para interoperabilidad con sistemas hospitalarios

**Complejidad estimada:** Muy alta (16-24 semanas). Requiere validación clínica, certificaciones sanitarias y colaboración con profesionales médicos para el diseño de los modelos.

#### 3. Integración domótica con el hogar inteligente

**Descripción:** Conexión de la aplicación con sistemas de domótica del hogar para controlar iluminación, temperatura, persianas y sensores de caída mediante voz o interfaz simplificada. El sistema aprendería los hábitos del usuario y ajustaría automáticamente el entorno.

**Utilidad:** Un entorno adaptado reduce un 30% el riesgo de caídas y mejora la autonomía de las personas mayores. La automatización de tareas cotidianas (encender luces al levantarse, regular temperatura) incrementa significativamente la calidad de vida.

**Enfoque técnico:**
- Protocolo Matter/Thread como estándar unificado de domótica
- Integración con Google Home SDK y HomeKit para compatibilidad amplia
- Sensores de caída basados en radar mmWave (no cámaras, por privacidad)
- Gateway local (Raspberry Pi) para funcionamiento offline
- Comunicación BLE/Wi-Fi Direct con baja latencia (<200ms)

**Complejidad estimada:** Alta (10-14 semanas). La fragmentación del ecosistema domótico y la necesidad de certificación con múltiples fabricantes incrementan la complejidad.

#### 4. Comandos de voz avanzados para navegación

**Descripción:** Sistema de control por voz nativo que permita navegar por toda la aplicación sin interacción táctil, optimizado para acentos regionales españoles y patrones de habla de personas mayores (velocidad reducida, pausas frecuentes, vocabulario específico).

**Utilidad:** Para usuarios con problemas de movilidad, visión reducida o dificultades motrices finas, el control por voz no es una comodidad sino una necesidad de accesibilidad fundamental.

**Enfoque técnico:**
- speech_to_text para reconocimiento local básico
- Integración con Google Cloud Speech-to-Text para reconocimiento avanzado
- Modelo de lenguaje personalizado entrenado con corpus de habla de mayores
- Comandos contextuales que se adaptan a la pantalla actual
- Feedback háptico y auditivo para confirmar acciones

**Complejidad estimada:** Media-Alta (6-10 semanas). El principal reto es la precisión del reconocimiento con voces atípicas y la latencia en procesamiento en la nube.

#### 5. Plataforma de actividades sociales y envejecimiento activo

**Descripción:** Módulo social que conecte a personas mayores con actividades de su comunidad: talleres, paseos grupales, clubs de lectura, voluntariado intergeneracional. Incluiría sistema de matchmaking por intereses, ubicación y nivel de movilidad.

**Utilidad:** El envejecimiento activo es un pilar fundamental de la estrategia de la OMS. La participación social reduce el deterioro cognitivo un 40% y mejora la salud física percibida.

**Enfoque técnico:**
- Geolocalización con Firebase Firestore (GeoFire) para actividades cercanas
- Sistema de notificaciones push segmentadas por intereses
- Perfiles sociales simplificados con controles de privacidad granulares
- Integración con APIs de ayuntamientos y centros sociales (datos abiertos)
- Módulo de videoconferencia (Jitsi Meet SDK) para actividades remotas

**Complejidad estimada:** Media (4-8 semanas). La complejidad radica más en la creación de red y partnerships que en el desarrollo técnico.

#### 6. Integración con el Sistema Nacional de Salud (SNS)

**Descripción:** Conexión con los sistemas de información sanitaria de las comunidades autónomas para acceder a historiales médicos, recetas electrónicas, citas y resultados de análisis de forma segura y estandarizada.

**Utilidad:** Eliminaría la fragmentación de información sanitaria, permitiría un seguimiento integral del paciente y facilitaría la coordinación entre atención primaria, especializada y cuidados domiciliarios.

**Enfoque técnico:**
- Adaptación a la norma HL7 FHIR R4 adoptada por el SNS
- Autenticación mediante Cl@ve (sistema de identificación electrónica del gobierno)
- Conexión con Receta Electrónica del INSNS
- Adaptadores específicos por comunidad autónoma (cada una tiene su propio SIA)
- Cifrado end-to-end con certificados digitales

**Complejidad estimada:** Muy alta (20-30 semanas). La burocracia administrativa y la heterogeneidad de sistemas entre CC.AA. suponen el mayor obstáculo, más que la tecnología en sí.

#### 7. Marketplace de cuidadores profesionales

**Descripción:** Plataforma dentro de la aplicación que conecte a familias con cuidadores profesionales verificados, permitiendo búsqueda por especialización, disponibilidad, valoraciones y precio. Incluiría sistema de reservas, pagos y seguimiento del servicio.

**Utilidad:** España necesitará 300.000 cuidadores adicionales para 2030. Un marketplace digital reduciría la fricción en la búsqueda de profesionales cualificados y garantizaría estándares de calidad.

**Enfoque técnico:**
- Perfiles verificados con validación de titulaciones y antecedentes
- Sistema de reservas con calendario compartido (Firebase Firestore)
- Pasarela de pago integrada (Stripe Connect para marketplace)
- Sistema de reputación con reseñas verificadas
- Contratos digitales con firma electrónica (integración con DocuSign API)

**Complejidad estimada:** Alta (10-16 semanas). Requiere cumplimiento de normativa de servicios digitales, gestión de responsabilidades legales y un modelo de negocio definido.

#### 8. Sistema predictivo de caídas con machine learning

**Descripción:** Modelo de ML que analice patrones de movimiento, historial de caídas, medicación, condiciones ambientales y datos de wearables para predecir el riesgo de caída en las próximas 24-72 horas y generar alertas preventivas.

**Utilidad:** Las caídas son la primera causa de lesión accidental en mayores de 65 años en España. Un sistema predictivo permitiría intervenciones preventivas (ajuste de medicación, recomendación de ejercicios, alerta a cuidadores).

**Enfoque técnico:**
- Modelo TensorFlow Lite ejecutado on-device para privacidad
- Features: acelerómetro, giroscopio, patrones de sueño, medicación, clima
- Entrenamiento con datasets públicos (como SisFall o UR Fall) y datos anonimizados propios
- Thresholds adaptativos por usuario (aprendizaje continuo)
- Integración con el módulo de alertas y plan de emergencia

**Complejidad estimada:** Alta (8-14 semanas). El mayor reto es obtener datos de entrenamiento de calidad y validar la precisión del modelo en condiciones reales sin generar falsas alarmas.

#### 9. Módulo de estimulación cognitiva

**Descripción:** Suite de juegos y ejercicios diseñados por neuropsicólogos para estimular memoria, atención, lenguaje y funciones ejecutivas. Incluiría evaluación cognitiva periódica y adaptación dinámica de la dificultad.

**Utilidad:** La estimulación cognitiva regular puede retrasar la progresión del deterioro cognitivo leve y mejorar la calidad de vida. Un módulo digital accesible permitiría la práctica diaria sin desplazamientos.

**Enfoque técnico:**
- Motor de juegos en Flutter con Flame Engine
- Ejercicios clasificados por dominio cognitivo (MoCA, MMSE adaptados)
- Algoritmo de dificultad adaptativa (similar a Duolingo)
- Dashboard de progreso para usuario, familia y profesional
- Normativa como dispositivo médico clase I si se claims terapéuticos

**Complejidad estimada:** Media-Alta (8-12 semanas). El diseño de los ejercicios requiere colaboración con neuropsicólogos y validación clínica.

#### 10. Soporte multilingüe y adaptación cultural

**Descripción:** Internacionalización completa de la aplicación para los idiomas cooficiales de España (catalán, euskera, gallego) y adaptación cultural de contenidos, interfaces y recomendaciones según la región.

**Utilidad:** Más del 15% de las personas mayores en España tienen como lengua materna un idioma cooficial. La atención en su lengua mejora la adherencia a tratamientos, la comprensión de instrucciones y el bienestar emocional.

**Enfoque técnico:**
- Sistema i18n con ARB files de Flutter (`flutter_localizations`)
- Traducción profesional (no automática) de toda la interfaz y contenidos
- Adaptación de unidades, formatos de fecha y referencias culturales
- RTL support para futura expansión a usuarios árabes
- Feature flags para activar/desactivar idiomas por región

**Complejidad estimada:** Media (4-6 semanas). La complejidad técnica es moderada; el coste principal es la traducción profesional y la validación cultural.

> **[Sugerencia de infografía 1]:** Diagrama de roadmap temporal mostrando las 10 ampliaciones distribuidas en un horizonte de 3 años, con dependencias entre ellas y estimación de esfuerzo.

---

### 10.2 Mejoras pendientes

El desarrollo ágil del proyecto ha identificado una serie de áreas de mejora técnica (deuda técnica) que se abordarán en iteraciones futuras. La siguiente tabla prioriza estas mejoras según impacto y esfuerzo estimado.

#### Tabla de deuda técnica

| Área | Descripción | Prioridad | Esfuerzo estimado |
|------|-------------|-----------|-------------------|
| **Rendimiento - Lazy Loading** | Implementar carga diferida en listas largas de historial médico y registros de actividad usando `ListView.builder` con pagination | Alta | 2-3 días |
| **Rendimiento - Caché** | Sistema de caché inteligente con `hive` o `isar` para datos de uso frecuente (perfil, contactos de emergencia, medicación activa) | Alta | 3-5 días |
| **Rendimiento - Imágenes** | Optimización de imágenes: WebP/AVIF, resize según dispositivo, precarga asíncrona con `cached_network_image` | Media | 2-3 días |
| **Rendimiento - Streams** | Gestión eficiente de streams de Firebase: cancelación en dispose, debounce en búsquedas, throttling en actualizaciones en tiempo real | Alta | 2-4 días |
| **Rendimiento - Code Splitting** | Deferred loading de módulos no críticos (estimulación cognitiva, marketplace) para reducir el bundle inicial | Media | 3-5 días |
| **Testing - Cobertura unitaria** | Aumentar cobertura de tests unitarios del 65% al 85% en services, models y utils | Alta | 5-7 días |
| **Testing - Integration** | Tests de integración para flujos críticos: login, registro de medicación, envío de alertas | Media | 4-6 días |
| **Testing - Widget** | Tests de widgets de accesibilidad para verificar tamaños de fuente, contrastes y navegación por lector de pantalla | Media | 3-5 días |
| **Testing - E2E** | Tests end-to-end con `patrol` o `integration_test` en dispositivos reales Android/iOS | Baja | 5-8 días |
| **Documentación** | Documentación técnica completa de la arquitectura, patrones de diseño y decisiones técnicas (ADRs) | Media | 3-4 días |
| **Monitoreo** | Integración de Firebase Performance Monitoring y Crashlytics con dashboards personalizados y alertas | Media | 2-3 días |
| **Refactorización** | Extracción de componentes reutilizables, eliminación de code duplication en pantallas de formularios | Baja | 4-6 días |

#### Oportunidades de optimización de rendimiento

**Carga diferida (Lazy Loading):**
Actualmente, algunas listas cargan todos los registros en memoria simultáneamente. La migración a un enfoque paginado con `StreamBuilder` + consultas `.limit()` en Firestore reduciría el consumo de memoria en un 60-70% para usuarios con historiales extensos.

**Estrategia de caché:**
Se implementará una arquitectura de caché en dos niveles:
1. **Caché en memoria** (para la sesión actual): datos frecuentemente accedidos
2. **Caché persistente** (Hive/Isar): datos que deben persistir entre sesiones

```dart
// Ejemplo de patrón cache-first con fallback a red
Future<UserProfile> getUserProfile(String uid) async {
  final cached = await cache.get<UserProfile>('profile_$uid');
  if (cached != null && !cache.isExpired('profile_$uid')) {
    return cached;
  }
  final fresh = await firestore.collection('users').doc(uid).get();
  await cache.put('profile_$uid', fresh.data(), ttl: Duration(hours: 1));
  return fresh.data()!;
}
```

**Gestión de Streams:**
Los streams de Firebase son poderosos pero costosos si no se gestionan correctamente. Se implementarán:
- Cancelación automática en el `dispose()` de cada StatefulWidget
- `debounce` de 300ms en búsquedas en tiempo real
- `throttle` de 5s en actualizaciones de posición GPS
- Suscripciones condicionales basadas en visibilidad de pantalla

**Cobertura de tests:**
El objetivo es alcanzar una cobertura mínima del 80% en capas de dominio y datos, con especial énfasis en:
- Validación de modelos de datos
- Lógica de negocio crítica (cálculo de alertas, validación de medicación)
- Transformadores de datos para accesibilidad

> **[Sugerencia de gráfico 2]:** Gráfico de barras mostrando la cobertura de tests actual vs. objetivo por capa de la arquitectura (presentación, dominio, datos).

---

### 10.3 Compatibilidad

#### Estrategia multiplataforma

Flutter permite compartir aproximadamente el **95% del código** entre plataformas, manteniendo solo un 5% de código específico para adaptaciones nativas. Esta ratio se distribuye de la siguiente forma:

| Componente | Código compartido | Código específico |
|------------|-------------------|-------------------|
| Lógica de negocio (Domain) | 100% | 0% |
| Capa de datos (Data) | 98% | 2% (platform channels) |
| UI - Componentes base | 95% | 5% (adaptaciones visuales) |
| UI - Navegación | 90% | 10% (gestos, transiciones) |
| Integraciones nativas | 40% | 60% (BLE, biometría, notificaciones) |
| **Total estimado** | **~95%** | **~5%** |

Las plataformas objetivo son:
- **Android:** smartphones y tablets
- **iOS:** iPhones y iPads
- **Web:** portal para cuidadores y profesionales (PWA)
- **Desktop (futuro):** Windows/macOS para panel de administración

#### Política de versiones de sistema operativo

| Plataforma | Versión mínima | % cobertura estimado | Justificación |
|------------|----------------|----------------------|---------------|
| Android | 8.0 (API 26) | ~96% dispositivos activos | Permite notification channels, autofill framework |
| iOS | 13.0 | ~98% dispositivos activos | Soporte para Sign in with Apple, dark mode nativo |
| Web | Navegadores < 2 años | ~99% | Evergreen browsers |

La política de actualización será: **soportar las últimas 5 versiones mayores** de cada plataforma. Cuando una versión caiga por debajo del 1% de cuota de mercado en España, se evaluará su descontinuación.

#### Compatibilidad hacia atrás (Backward Compatibility)

**Versionado de API:**
Todas las APIs seguirán semántica semver (v1, v2, v3) con garantía de compatibilidad hacia atrás dentro de la misma versión mayor. Las breaking changes se comunicarán con 3 meses de antelación mínima.

**Feature flags:**
El sistema de feature flags (Firebase Remote Config) permitirá:
- Activar/desactivar funcionalidades sin actualizar la app
- Rollback instantáneo si se detectan problemas en producción
- Lanzamientos progresivos (10% → 50% → 100%)
- A/B testing de nuevas funcionalidades

```dart
// Ejemplo de feature flag con Remote Config
final remoteConfig = FirebaseRemoteConfig.instance;
await remoteConfig.fetchAndActivate();

if (remoteConfig.getBool('enable_ai_companion')) {
  navigator.pushNamed('/ai-companion');
} else {
  // Fallback al chat convencional
}
```

**Migración de base de datos:**
Firestore no requiere migraciones de esquema en el sentido tradicional, pero los cambios en la estructura de datos se gestionarán mediante:
- Versionado de documentos (campo `schemaVersion`)
- Funciones cloud que migran datos en lectura (lazy migration)
- Scripts de migración batch para cambios estructurales mayores

**Política de deprecación:**
- Deprecación soft (warnings en consola): 6 meses
- Deprecación hard (funcionalidad eliminada): 12 meses
- Comunicación a usuarios afectados: email + notificación push + banner en app

#### Adaptaciones específicas por plataforma

**Android:**
- Design system Material 3 con `useMaterial3: true`
- Navegación con gestos de back predictivo
- Integración con Health Connect para datos de salud
- Notificaciones con canales categorizados
- Soporte para foldables (layout adaptativo)

**iOS:**
- Design system Cupertino para componentes nativos (pickers, alerts, navigation bars)
- Safe area handling para notch y Dynamic Island
- Haptic feedback con `TapticEngine`
- Integración con HealthKit
- Widget de pantalla de inicio con datos de medicación

**Adaptación automática de estilo:**
```dart
Widget buildPlatformButton({required VoidCallback onPressed}) {
  if (Platform.isIOS) {
    return CupertinoButton(onPressed: onPressed, child: const Text('Aceptar'));
  }
  return ElevatedButton(onPressed: onPressed, child: const Text('Aceptar'));
}
```

---

### 10.4 Escalabilidad

#### Escalabilidad horizontal vs. vertical con Firebase

Firebase ofrece escalabilidad automática (serverless), lo que elimina la necesidad de gestionar servidores. Sin embargo, es crucial entender las implicaciones:

| Aspecto | Escalabilidad vertical | Escalabilidad horizontal |
|---------|------------------------|--------------------------|
| **Firestore** | Limitaciones por documento (1MB max, 1 write/second por doc) | Colecciones distribuidas, sharding automático |
| **Cloud Functions** | Memoria/CPU por instancia (hasta 8GB/4 vCPU) | Auto-scaling a 100.000+ instancias concurrentes |
| **Authentication** | Ilimitado (gestionada por Google) | Ilimitado |
| **Cloud Storage** | Ilimitado por bucket | CDN global automático |
| **Realtime Database** | 200.000 conexiones simultáneas por BD | Múltiples instancias para escalar |

**Estrategia recomendada:** Escalabilidad horizontal nativa de Firebase con optimizaciones de coste para el crecimiento proyectado.

#### Estrategias de escalado de base de datos

**Sharding:**
Para colecciones que superen 1M de documentos, se implementará sharding por usuario o por fecha:
```
/medications/{userId}/history/{timestamp}/records
```

**Desnormalización:**
En Firestore, la desnormalización controlada reduce el número de lecturas:
- Datos del usuario embebidos en documentos de alertas (evita lecturas adicionales)
- Contadores incrementales (`FieldValue.increment()`) para métricas frecuentes
- Documentos resumen diarios/semanales precalculados con Cloud Functions

**Indexación:**
- Índices automáticos para campos simples
- Índices compuestos para consultas multi-campo (alertas por fecha + tipo + severidad)
- Monitorización de consultas sin índice con Firebase Performance

**Paginación:**
Todas las listas usarán paginación basada en cursor (no offset) para garantizar rendimiento constante:
```dart
final query = firestore
    .collection('alerts')
    .orderBy('timestamp', descending: true)
    .startAfterDocument(lastDocument)
    .limit(20);
```

**Caché del cliente:**
- Firestore SDK incluye caché offline automático (persistente en disco)
- Configuración de tamaño máximo de caché para evitar crecimiento descontrolado
- Estrategia de invalidación basada en TTL y eventos de actualización

#### Estrategia CDN y caché

Firebase Hosting (para la versión web) y Cloud Storage incluyen CDN global de Google automáticamente:

| Recurso | CDN | Cache TTL | Invalidación |
|---------|-----|-----------|--------------|
| Assets de la app (imágenes, fuentes) | Firebase Hosting CDN | 1 año (cache busting con hash) | Deploy nuevo |
| Imágenes de perfil | Cloud Storage CDN | 1 hora | Actualización de imagen |
| Contenido estático (guías, FAQs) | Firebase Hosting CDN | 24 horas | Deploy nuevo |
| Datos dinámicos | No CDN (Firestore directo) | N/A | Tiempo real |

#### Comparativa de costes por nivel de usuarios

La siguiente tabla estima los costes mensuales de infraestructura Firebase para diferentes escenarios de crecimiento, basado en los precios actuales del plan Blaze (pay-as-you-go):

| Concepto | 1.000 usuarios | 10.000 usuarios | 50.000 usuarios | 100.000 usuarios |
|----------|----------------|-----------------|-----------------|------------------|
| **Authentication** | $0 (free tier) | $0 (free tier) | $0 (free tier) | $0 (free tier) |
| **Firestore (lecturas)** | $0 | $12 | $85 | $190 |
| **Firestore (escrituras)** | $0 | $8 | $45 | $95 |
| **Firestore (almacenamiento)** | $0 | $2 | $15 | $35 |
| **Cloud Storage** | $0 | $5 | $25 | $55 |
| **Cloud Functions** | $0 | $15 | $60 | $130 |
| **Firebase Hosting** | $0 | $0 | $5 | $10 |
| **Remote Config / AB Testing** | $0 | $0 | $0 | $0 |
| **Crashlytics / Analytics** | $0 | $0 | $0 | $0 |
| **TOTAL ESTIMADO** | **$0** | **$42/mes** | **$235/mes** | **$515/mes** |

> **Nota:** Estos cálculos asumen un uso moderado (~50 lecturas/usuario/día, ~10 escrituras/usuario/día). El uso intensivo de funciones en tiempo real podría incrementar los costes de Firestore en un 30-50%. Se recomienda establecer alertas de presupuesto en Google Cloud para detectar anomalías.

**Estrategia de optimización de costes:**
1. **Agregación de lecturas:** Consolidar múltiples lecturas en una sola cuando sea posible
2. **Escrituras batch:** Agrupar escrituras relacionadas en operaciones `WriteBatch`
3. **Cloud Functions optimizadas:** Uso de instancias con memoria mínima para funciones ligeras
4. **Cache agresivo:** Reducir lecturas a Firestore mediante caché local del cliente
5. **Compresión de imágenes:** Almacenar imágenes en resoluciones múltiples para servir solo la necesaria

> **[Sugerencia de gráfico 3]:** Gráfico de líneas mostrando la evolución proyectada de costes mensuales vs. número de usuarios durante 3 años, con threshold de sostenibilidad económica.

---

## 11. CONCLUSIONES

### 11.1 Logros alcanzados

El desarrollo de esta aplicación de cuidados para personas mayores ha representado un desafío técnico y humano de primer orden. A lo largo de este proyecto se han alcanzado objetivos significativos tanto en el ámbito técnico como en el personal, que se detallan a continuación.

#### Logros técnicos

**Arquitectura robusta y escalable:**
Se ha diseñado e implementado una arquitectura de software basada en **Clean Architecture** con patrón **MVVM** (Model-View-ViewModel) que garantiza la separación de responsabilidades, la testabilidad y la mantenibilidad del código. El sistema de **RBAC** (Role-Based Access Control) con cuatro roles diferenciados (usuario mayor, cuidador familiar, profesional sanitario, administrador) permite un control granular de permisos y accesos. La combinación de **Flutter como framework frontend** y **Firebase como Backend-as-a-Service** ha demostrado ser una elección acertada, proporcionando:

- Desarrollo multiplataforma con ~95% de código compartido
- Autenticación segura con múltiples proveedores
- Base de datos en tiempo real con sincronización offline
- Infraestructura serverless sin gestión de servidores
- Escalabilidad automática incluida

```
Arquitectura implementada:

┌─────────────────────────────────────────┐
│           PRESENTATION LAYER             │
│  (Flutter Widgets, MVVM, State Mgmt)    │
├─────────────────────────────────────────┤
│             DOMAIN LAYER                 │
│  (Use Cases, Entities, Repository Interfaces) │
├─────────────────────────────────────────┤
│               DATA LAYER                 │
│  (Repositories, Data Sources, Firebase SDKs)   │
├─────────────────────────────────────────┤
│           EXTERNAL SERVICES              │
│  (Firestore, Auth, Storage, Functions, IoT)    │
└─────────────────────────────────────────┘
```

**Accesibilidad real para mayores de 65 años:**
Uno de los logros más significativos ha sido lograr puntuaciones de accesibilidad que superan ampliamente los estándares del sector. Los resultados de auditoría con Lighthouse alcanzan **94/100** en accesibilidad, superando el umbral de WCAG 2.2 Nivel AA requerido. Las medidas implementadas incluyen:

- Tamaño mínimo de fuente de 18sp con escalado dinámico hasta 24sp
- Ratio de contraste mínimo de 7:1 (supera el requisito AAA de 4.5:1)
- Botones con área táctil mínima de 48x48dp (recomendación WCAG)
- Navegación compatible con TalkBack (Android) y VoiceOver (iOS)
- Eliminación de gestos complejos (swipe, pinch-to-zoom como única acción)
- Feedback multimodal (visual + auditivo + háptico) para cada acción

**Integración IoT funcional:**
La aplicación integra de forma operativa dispositivos IoT para el monitoreo de salud:
- Comunicación **BLE (Bluetooth Low Energy)** con tensiómetros y glucómetros
- Integración con **Health Connect** (Android) y **HealthKit** (iOS)
- Latencia de sincronización inferior a **2 segundos** en condiciones normales
- Gestión robusta de reconexión automática ante pérdida de conectividad
- Almacenamiento local de datos de salud cuando no hay conexión

**Cumplimiento normativo integral:**
El proyecto cumple con el marco regulatorio español y europeo aplicable:
- **RGPD** (Reglamento General de Protección de Datos): consentimiento explícito, derecho al olvido, portabilidad de datos
- **LOPDGDD** (Ley Orgánica de Protección de Datos): adaptación a la legislación española
- **Guidelines de la AEPD**: medidas técnicas y organizativas recomendadas
- **Esquema Nacional de Seguridad**: clasificación de datos, niveles de protección
- Cifrado en tránsito (TLS 1.3) y en reposo (AES-256)
- Política de retención y eliminación automática de datos

**Pipeline CI/CD automatizado:**
Se ha implementado un pipeline de integración y despliegue continuo que incluye:
- Análisis estático de código con `flutter analyze` y `dart fix`
- Ejecución automática de tests unitarios y de widgets en cada PR
- Build de APK/IPA con Fastlane
- Despliegue automático a Firebase App Distribution (testing)
- Publicación en Google Play Store y App Store Connect (producción)
- Monitoreo post-despliegue con Crashlytics y Performance Monitoring

#### Crecimiento personal y profesional

Más allá de los logros técnicos, este TFG ha supuesto un crecimiento profesional significativo:

**Capacidades de investigación:** Se ha desarrollado la capacidad de investigar de forma autónoma sobre tecnologías emergentes, evaluar críticamente la documentación oficial y las fuentes académicas, y sintetizar información de múltiples fuentes para tomar decisiones técnicas fundamentadas. La revisión de literatura sobre accesibilidad para personas mayores, normativas de protección de datos y mejores prácticas de desarrollo móvil ha sido un ejercicio riguroso de investigación aplicada.

**Gestión de proyectos:** La planificación y ejecución de un proyecto de esta envergadura ha requerido desarrollar habilidades de estimación temporal, priorización de funcionalidades (método MoSCoW), gestión de dependencias técnicas y adaptación a imprevistos. El uso de metodologías ágiles (sprints semanales, revisiones periódicas) ha proporcionado una experiencia práctica valiosa.

**Comunicación técnica:** La redacción de esta memoria, la documentación del código y la preparación de la defensa oral han mejorado significativamente la capacidad de comunicar conceptos técnicos complejos de forma clara y estructurada, tanto para audiencias técnicas como no técnicas.

**Resolución de problemas:** Cada desafío técnico encontrado (desde la integración BLE hasta el cumplimiento de WCAG 2.2) ha requerido un proceso sistemático de diagnóstico, investigación de alternativas, implementación y validación, fortaleciendo el pensamiento analítico y la resiliencia ante la frustración técnica.

> **[Sugerencia de infografía 4]:** Radar chart mostrando las competencias técnicas adquiridas (Flutter, Firebase, Accesibilidad, IoT, DevOps, Seguridad, Testing) con nivel antes y después del proyecto.

---

### 11.2 Dificultades encontradas

El desarrollo no ha estado exento de dificultades significativas. La siguiente tabla resume los principales desafíos encontrados, su contexto y las soluciones aplicadas:

| Dificultad | Descripción | Solución aplicada |
|------------|-------------|-------------------|
| **Complejidad de accesibilidad** | Lograr que la interfaz fuera realmente usable por personas de 65+ años con diversos niveles de habilidad digital y capacidades sensoriales reducidas. Los estándares WCAG proporcionan requisitos técnicos pero no guías de diseño empático. | Se realizaron pruebas de usabilidad con 8 personas mayores del centro de día municipal. Se implementaron iteraciones basadas en feedback directo: aumento de tamaño de fuente a 18sp mínimo, eliminación de iconos sin label, simplificación de flujos de navegación. Se creó un "modo simplificado" con un 40% menos de opciones en pantalla. |
| **Inestabilidad en integración BLE** | La comunicación Bluetooth Low Energy con dispositivos médicos presentó desconexiones frecuentes, tiempos de emparejamiento variables entre dispositivos Android y comportamientos inconsistentes en background. | Se implementó un sistema de reconexión automática con backoff exponencial (1s, 2s, 4s, 8s). Se utilizó `flutter_blue_plus` por su mejor mantenimiento. Se añadió cola de datos offline para evitar pérdida de lecturas durante desconexiones. Se estableció timeout de 15s para operaciones de descubrimiento de servicios. |
| **Conflictos de sincronización offline-online** | Cuando un usuario realizaba cambios offline y otro dispositivo modificaba los mismos datos online, se producían conflictos de resolución que podían resultar en pérdida de información médica crítica. | Se implementó estrategia *last-write-wins* con timestamps del servidor (no del cliente). Para datos críticos (medicación, alertas), se añadió un sistema de resolución manual con historial de cambios visible. Se utilizaron transacciones de Firestore para operaciones atómicas. |
| **Cumplimiento RGPD para datos de salud** | Los datos de salud son categoría especial según el Art. 9 del RGPD, requiriendo medidas de protección reforzadas. La implementación técnica de cifrado, consentimiento granular y derecho al olvido presentó complejidades significativas. | Se implementó cifrado adicional a nivel de aplicación (no solo el de Firebase por defecto). Se diseñó un flujo de consentimiento explícito por tipo de dato con posibilidad de revocación individual. Se creó un proceso automatizado de anonimización para el derecho al olvido. Se consultó con el DPO de la universidad para validar el enfoque. |
| **Diferencias de testing entre plataformas** | Los tests de widgets funcionaban correctamente en Android pero fallaban en iOS debido a diferencias en los componentes Cupertino. Los tests de integración no podían ejecutarse en CI/CD para iOS por limitaciones del runner. | Se crearon tests condicionales con `Platform.isIOS` / `Platform.isAndroid`. Para iOS en CI/CD, se utilizó `patrol` con Firebase Test Lab que soporta dispositivos iOS reales. Se estableció un protocolo de testing manual mensual en dispositivo físico iOS. |
| **Rendimiento con grandes volúmenes de datos** | Con usuarios que acumulaban más de 6 meses de registros de salud, las pantallas de historial tardaban más de 3 segundos en cargar, superando el umbral aceptable para usuarios mayores. | Se implementó paginación con cursor (no offset), carga diferida con `ListView.builder`, y caché local con Hive. Se redujo el tiempo de carga inicial de 3.2s a 0.8s. Se añadieron indicadores de carga claros para gestionar la expectativa del usuario. |

#### Guía para presentar dificultades profesionalmente en un TFG

La forma en que se presentan las dificultades en un trabajo de fin de grado es tan importante como las soluciones técnicas. Se recomienda:

1. **No ocultar las dificultades:** Un TFG sin problemas parece poco realista. Las dificultades demuestran que el proyecto ha tenido complejidad real y que el estudiante ha sabido resolverlas.

2. **Enfocarse en el proceso de resolución:** No basta con listar problemas; hay que explicar el proceso sistemático de diagnóstico, investigación de alternativas, selección de solución y validación.

3. **Cuantificar siempre que sea posible:** En lugar de "la app era lenta", usar "el tiempo de carga era de 3.2s, se redujo a 0.8s tras implementar paginación".

4. **Reconocer limitaciones honestamente:** Si una solución no es perfecta, decirlo. Por ejemplo: "El sistema de reconexión BLE resuelve el 90% de las desconexiones; el 10% restante requiere intervención manual del usuario."

5. **Conectar con el aprendizaje:** Cada dificultad superada debe traducirse en un aprendizaje explícito. Esto demuestra madurez profesional y capacidad de crecimiento.

6. **Evitar lenguaje negativo:** En lugar de "no pude implementar X", usar "la implementación de X queda como trabajo futuro por las razones Y".

---

### 11.3 Aprendizajes técnicos y personales

#### Habilidades técnicas adquiridas

**Desarrollo con Flutter/Dart:**
Se ha alcanzado un nivel avanzado en el desarrollo con Flutter, incluyendo dominio del sistema de widgets, gestión de estado (Provider/Riverpod), animaciones, navegación y comunicación con código nativo mediante platform channels. Se ha interiorizado la filosofía de Flutter de "todo es un widget" y se ha desarrollado la capacidad de crear componentes reutilizables y composables.

```dart
// Ejemplo de evolución: de widget básico a componente reutilizable accesible
class AccessibleButton extends StatelessWidget {
  final String label;
  final IconData? icon;
  final VoidCallback onPressed;
  final bool isDestructive;

  const AccessibleButton({
    required this.label,
    this.icon,
    required this.onPressed,
    this.isDestructive = false,
    super.key,
  });

  @override
  Widget build(BuildContext context) {
    return Semantics(
      label: label,
      hint: 'Pulsa dos veces para $label',
      button: true,
      child: SizedBox(
        height: 56,
        child: ElevatedButton.icon(
          onPressed: onPressed,
          icon: icon != null ? Icon(icon, size: 28) : null,
          label: Text(label, style: Theme.of(context).textTheme.titleMedium),
          style: ElevatedButton.styleFrom(
            minimumSize: const Size(double.infinity, 56),
            backgroundColor: isDestructive
                ? Colors.red.shade700
                : Theme.of(context).primaryColor,
          ),
        ),
      ),
    );
  }
}
```

**Firebase como Backend-as-a-Service:**
Se ha adquirido experiencia profunda con el ecosistema Firebase:
- **Firestore:** modelado de datos NoSQL, consultas compuestas, seguridad con rules
- **Authentication:** múltiples proveedores, gestión de sesiones, tokens personalizados
- **Cloud Functions:** lógica serverless, triggers, scheduled functions
- **Cloud Storage:** gestión de archivos, reglas de acceso, optimización de imágenes
- **Remote Config:** feature flags, A/B testing
- **Crashlytics y Performance Monitoring:** observabilidad en producción

**Diseño de UX/UI accesible:**
Más allá de cumplir checklist de WCAG, se ha interiorizado el principio de diseño inclusivo: crear interfaces que funcionen bien para todos los usuarios, no solo para aquellos con capacidades plenas. Se ha aprendido que la accesibilidad no es un añadido sino una característica fundamental del diseño.

**Integración de APIs e IoT:**
Se ha desarrollado la capacidad de integrar servicios externos (REST APIs, BLE devices, Health APIs) de forma robusta, manejando casos de error, timeouts, reconexiones y sincronización de datos.

**DevOps móvil:**
Se ha adquirido experiencia en automatización de builds, tests, despliegue y monitoreo. La configuración de pipelines CI/CD para aplicaciones móviles multiplataforma es una habilidad altamente valorada en la industria.

**Seguridad y privacidad:**
Se ha desarrollado una mentalidad de "security by design" y "privacy by default", entendiendo que la protección de datos no es un requisito a cumplir sino un principio de diseño que debe impregnar cada decisión técnica.

**Testing:**
Se ha interiorizado la importancia del testing como garantía de calidad, no como trámite. Se ha ganado experiencia en tests unitarios, de widgets, de integración y end-to-end, entendiendo las fortalezas y limitaciones de cada nivel.

#### Habilidades blandas desarrolladas

**Investigación autónoma:** La capacidad de identificar una necesidad de conocimiento, formular preguntas de investigación, buscar fuentes fiables, evaluar críticamente la información y aplicar los hallazgos al proyecto ha sido fundamental y se ha desarrollado significativamente.

**Gestión del tiempo:** Coordinar el desarrollo técnico con la redacción de la memoria, las reuniones de seguimiento y otros compromisos académicos ha requerido desarrollar técnicas de planificación realista, priorización y gestión de expectativas.

**Redacción técnica:** La capacidad de documentar decisiones técnicas, escribir documentación de código y redactar una memoria académica estructurada y clara es una habilidad transferible a cualquier rol profesional.

**Pensamiento crítico:** Evaluar trade-offs entre diferentes soluciones técnicas (rendimiento vs. complejidad, rapidez vs. calidad, features vs. estabilidad) ha desarrollado la capacidad de tomar decisiones fundamentadas y defenderlas con argumentos técnicos.

**Adaptabilidad:** Los cambios de planes, los imprevistos técnicos y las revisiones de enfoque han requerido flexibilidad mental y capacidad de pivotar sin perder el objetivo final.

#### Lecciones clave del proceso de desarrollo

A lo largo del proyecto se han extraído varias lecciones fundamentales que guiarán el trabajo profesional futuro:

**1. La accesibilidad no se añade, se diseña:** Intentar hacer una app accesible al final del desarrollo es como intentar poner los cimientos después de construir la casa. La accesibilidad debe ser un requisito de diseño desde el primer wireframe.

**2. Menos es más para usuarios mayores:** Cada elemento adicional en pantalla es una fuente potencial de confusión. La simplicidad no es falta de funcionalidad, es respeto por el usuario.

**3. Los datos de salud son sagrados:** La responsabilidad de manejar datos médicos requiere un nivel de rigor que va más allá del cumplimiento normativo. Cada decisión sobre estos datos debe pasar el test de "¿le confiaría estos datos a mi abuela?"

**4. El feedback del usuario real no tiene sustituto:** Ninguna cantidad de testing automático reemplaza observar a una persona mayor de 75 años usando tu app por primera vez. Las suposiciones del desarrollador son frecuentemente erróneas.

**5. La deuda técnica es como una tarjeta de crédito:** Es útil para avanzar rápido al principio, pero los intereses se acumulan. Hay que pagarla antes de que la factura sea impagable.

**6. Un proyecto terminado es mejor que un proyecto perfecto:** La tentación de añadir "una feature más" es constante, pero un proyecto entregado con alcance bien definido siempre vale más que un proyecto perfecto que nunca se termina.

> **[Sugerencia de infografía 5]:** Timeline visual del proyecto mostrando las fases principales, hitos alcanzados, dificultades superadas y lecciones aprendidas en cada etapa.

---

*Fin de las secciones 10 y 11.*

# 13. ANEXOS

## 13.1 Código relevante

En esta sección se presentan los fragmentos de código más representativos del proyecto, correspondientes a las funcionalidades críticas de la aplicación. Cada bloque de código incluye comentarios explicativos en español.

### Inicialización de Firebase con App Check

```dart
import 'package:firebase_core/firebase_core.dart';
import 'package:firebase_app_check/firebase_app_check.dart';
import 'firebase_options.dart';

/// Clase encargada de inicializar Firebase y los servicios de seguridad
class FirebaseInitializer {
  /// Inicializa Firebase con verificación de integridad de la app (App Check)
  static Future<void> initialize() async {
    // Inicialización principal de Firebase con las opciones del proyecto
    await Firebase.initializeApp(
      options: DefaultFirebaseOptions.currentPlatform,
    );

    // Configuración de Firebase App Check para prevenir accesos no autorizados
    // En Android se utiliza Play Integrity, en iOS DeviceCheck y SafetyNet
    await FirebaseAppCheck.instance.activate(
      androidProvider: AndroidProvider.playIntegrity,
      appleProvider: AppleProvider.deviceCheck,
      webProvider: ReCaptchaV3Provider('clave-recaptcha-v3'),
    );

    print('Firebase y App Check inicializados correctamente');
  }
}

/// Punto de entrada principal de la aplicación
void main() async {
  // Asegurar que los bindings de Flutter estén inicializados
  WidgetsFlutterBinding.ensureInitialized();

  // Inicializar Firebase antes de ejecutar la app
  await FirebaseInitializer.initialize();

  // Ejecutar la aplicación principal
  runApp(const CuidadoMayorApp());
}
```

> **Nota:** Añadir captura de pantalla de la configuración de App Check en Firebase Console como **Figura A.1**.

### Servicio de autenticación con gestión de roles

```dart
import 'package:firebase_auth/firebase_auth.dart';
import 'package:cloud_firestore/cloud_firestore.dart';

/// Enumeración que define los tipos de perfil disponibles en la app
enum RolUsuario {
  mayor,        // Persona mayor (usuario principal)
  cuidador,     // Familiar o cuidador principal
  profesional,  // Médico o profesional sanitario
}

/// Servicio de autenticación que gestiona el registro, login y roles de usuario
class AuthService {
  final FirebaseAuth _auth = FirebaseAuth.instance;
  final FirebaseFirestore _firestore = FirebaseFirestore.instance;

  /// Obtiene el usuario actualmente autenticado
  User? get usuarioActual => _auth.currentUser;

  /// Stream que escucha cambios en el estado de autenticación
  Stream<User?> get authStateChanges => _auth.authStateChanges();

  /// Inicia sesión con email y contraseña
  Future<UserCredential> iniciarSesion({
    required String email,
    required String password,
  }) async {
    return await _auth.signInWithEmailAndPassword(
      email: email.trim(),
      password: password,
    );
  }

  /// Registra un nuevo usuario y almacena su rol en Firestore
  Future<UserCredential> registrarUsuario({
    required String email,
    required String password,
    required String nombre,
    required String apellidos,
    required RolUsuario rol,
    required String telefono,
  }) async {
    // Crear usuario en Firebase Authentication
    final credential = await _auth.createUserWithEmailAndPassword(
      email: email.trim(),
      password: password,
    );

    // Almacenar datos del perfil en Firestore con el rol asignado
    await _firestore.collection('usuarios').doc(credential.user!.uid).set({
      'uid': credential.user!.uid,
      'email': email.trim(),
      'nombre': nombre.trim(),
      'apellidos': apellidos.trim(),
      'telefono': telefono.trim(),
      'rol': rol.name,
      'fechaCreacion': FieldValue.serverTimestamp(),
      'ultimaConexion': FieldValue.serverTimestamp(),
      'activo': true,
    });

    return credential;
  }

  /// Obtiene el rol del usuario actual desde Firestore
  Future<RolUsuario?> obtenerRolUsuario() async {
    final user = _auth.currentUser;
    if (user == null) return null;

    final doc = await _firestore.collection('usuarios').doc(user.uid).get();

    if (!doc.exists) return null;

    final rolString = doc.data()?['rol'] as String;
    return RolUsuario.values.byName(rolString);
  }

  /// Cierra la sesión del usuario actual
  Future<void> cerrarSesion() async {
    await _auth.signOut();
  }

  /// Actualiza la fecha de última conexión del usuario
  Future<void> actualizarUltimaConexion() async {
    final user = _auth.currentUser;
    if (user != null) {
      await _firestore.collection('usuarios').doc(user.uid).update({
        'ultimaConexion': FieldValue.serverTimestamp(),
      });
    }
  }
}
```

### Reglas de seguridad de Firestore (control de acceso basado en roles)

```javascript
rules_version = '2';

service cloud.firestore {
  match /databases/{database}/documents {

    // Función auxiliar: verifica si el usuario está autenticado
    function estaAutenticado() {
      return request.auth != null;
    }

    // Función auxiliar: obtiene el rol del usuario desde su documento
    function obtenerRol() {
      return get(/databases/$(database)/documents/usuarios/$(request.auth.uid)).data.rol;
    }

    // Función auxiliar: verifica si el usuario accede a su propio documento
    function esPropioDocumento() {
      return request.auth.uid == resource.data.uid;
    }

    // Colección de usuarios: cada usuario solo puede leer/editar su propio perfil
    match /usuarios/{userId} {
      allow read: if estaAutenticado() && (esPropioDocumento() || obtenerRol() == 'profesional');
      allow create: if estaAutenticado() && esPropioDocumento();
      allow update: if estaAutenticado() && esPropioDocumento();
      allow delete: if false; // No se permite eliminar usuarios
    }

    // Colección de pacientes: accesible por cuidadores y profesionales asignados
    match /pacientes/{pacienteId} {
      allow read: if estaAutenticado() && (
        obtenerRol() == 'cuidador' ||
        obtenerRol() == 'profesional' ||
        resource.data.uidUsuario == request.auth.uid
      );
      allow create: if estaAutenticado() && obtenerRol() == 'cuidador';
      allow update: if estaAutenticado() && (
        obtenerRol() == 'cuidador' ||
        obtenerRol() == 'profesional'
      );
      allow delete: if false;
    }

    // Colección de medicación: los mayores ven la suya, cuidadores y profesionales pueden gestionar
    match /medicacion/{medicacionId} {
      allow read: if estaAutenticado();
      allow create, update: if estaAutenticado() && (
        obtenerRol() == 'cuidador' ||
        obtenerRol() == 'profesional'
      );
      allow delete: if obtenerRol() == 'profesional';
    }

    // Colección de constantes vitales: lectura para todos los roles implicados
    match /constantes_vitales/{vitalId} {
      allow read: if estaAutenticado() && (
        obtenerRol() == 'mayor' ||
        obtenerRol() == 'cuidador' ||
        obtenerRol() == 'profesional'
      );
      allow create: if estaAutenticado() && obtenerRol() == 'mayor';
      allow update, delete: if obtenerRol() == 'profesional';
    }

    // Colección de emergencias SOS: lectura para cuidadores y profesionales
    match /emergencias/{emergenciaId} {
      allow read: if estaAutenticado() && (
        obtenerRol() == 'cuidador' ||
        obtenerRol() == 'profesional' ||
        resource.data.uidUsuario == request.auth.uid
      );
      allow create: if estaAutenticado() && obtenerRol() == 'mayor';
      allow update: if estaAutenticado() && (
        obtenerRol() == 'cuidador' ||
        obtenerRol() == 'profesional'
      );
      allow delete: if false;
    }

    // Colección de recordatorios: acceso según rol
    match /recordatorios/{recordatorioId} {
      allow read: if estaAutenticado();
      allow create, update: if estaAutenticado() && (
        obtenerRol() == 'cuidador' ||
        obtenerRol() == 'profesional'
      );
      allow delete: if obtenerRol() == 'cuidador';
    }

    // Colección de videollamadas: acceso para participantes de la sesión
    match /videollamadas/{llamadaId} {
      allow read: if estaAutenticado();
      allow create: if estaAutenticado();
      allow update: if estaAutenticado();
      allow delete: if false;
    }
  }
}
```

> **Nota:** Incluir diagrama de flujo de autenticación como **Figura A.2**.

### Servicio de notificaciones de recordatorio de medicación

```dart
import 'package:flutter_local_notifications/flutter_local_notifications.dart';
import 'package:timezone/data/latest_all.dart' as tz;
import 'package:timezone/timezone.dart' as tz;
import 'package:shared_preferences/shared_preferences.dart';

/// Servicio que gestiona las notificaciones locales para recordatorios de medicación
class MedicacionNotificationService {
  final FlutterLocalNotificationsPlugin _notificaciones =
      FlutterLocalNotificationsPlugin();

  /// Inicializa el servicio de notificaciones con configuración de canales
  Future<void> inicializar() async {
    // Inicializar zona horaria para programar notificaciones correctamente
    tz.initializeTimeZones();

    // Configuración para Android: canal de notificación con prioridad alta
    const configuracionAndroid = AndroidInitializationSettings(
      '@mipmap/ic_launcher',
    );

    // Configuración para iOS: permisos de alertas, sonidos y badges
    const configuracionIOS = DarwinInitializationSettings(
      requestAlertPermission: true,
      requestBadgePermission: true,
      requestSoundPermission: true,
    );

    const configuraciones = InitializationSettings(
      android: configuracionAndroid,
      iOS: configuracionIOS,
    );

    // Inicializar plugin con callback para cuando el usuario toca la notificación
    await _notificaciones.initialize(
      configuraciones,
      onDidReceiveNotificationResponse: _alTocarNotificacion,
    );
  }

  /// Callback ejecutado cuando el usuario pulsa una notificación
  void _alTocarNotificacion(NotificationResponse response) {
    // Navegar a la pantalla de detalle de medicación
    // La lógica de navegación se maneja en el ViewModel correspondiente
    print('Notificación pulsada: ${response.payload}');
  }

  /// Programa un recordatorio de medicación a una hora específica
  Future<void> programarRecordatorio({
    required int id,
    required String nombreMedicamento,
    required String dosis,
    required TimeOfDay hora,
    required bool notificacionSonora,
  }) async {
    // Convertir la hora de Flutter a DateTime en la zona horaria local
    final ahora = tz.TZDateTime.now(tz.local);
    var fechaProgramada = tz.TZDateTime(
      tz.local,
      ahora.year,
      ahora.month,
      ahora.day,
      hora.hour,
      hora.minute,
    );

    // Si la hora ya pasó hoy, programar para mañana
    if (fechaProgramada.isBefore(ahora)) {
      fechaProgramada = fechaProgramada.add(const Duration(days: 1));
    }

    // Construir el título y cuerpo de la notificación en español
    final titulo = '💊 Recordatorio de medicación';
    final cuerpo = 'Es hora de tomar $nombreMedicamento ($dosis)';

    // Configurar detalles específicos para Android
    final detallesAndroid = AndroidNotificationDetails(
      'canal_medicacion',
      'Recordatorios de medicación',
      channelDescription: 'Notificaciones para recordar la toma de medicamentos',
      importance: Importance.max,
      priority: Priority.high,
      playSound: notificacionSonora,
      ticker: 'Recordatorio medicación',
    );

    // Configurar detalles específicos para iOS
    final detallesIOS = const DarwinNotificationDetails(
      presentAlert: true,
      presentBadge: true,
      presentSound: true,
    );

    final detalles = NotificationDetails(
      android: detallesAndroid,
      iOS: detallesIOS,
    );

    // Programar la notificación con repetición diaria
    await _notificaciones.zonedSchedule(
      id,
      titulo,
      cuerpo,
      fechaProgramada,
      detalles,
      androidScheduleMode: AndroidScheduleMode.exactAllowWhileIdle,
      matchDateTimeComponents: DateTimeComponents.time, // Repetir cada día
      payload: 'medicacion:$id',
    );

    // Guardar referencia en SharedPreferences para persistencia
    final prefs = await SharedPreferences.getInstance();
    final clave = 'recordatorio_$id';
    await prefs.setString(clave, '$nombreMedicamento|$dosis|${hora.hour}:${hora.minute}');
  }

  /// Cancela un recordatorio específico
  Future<void> cancelarRecordatorio(int id) async {
    await _notificaciones.cancel(id);

    // Eliminar referencia de SharedPreferences
    final prefs = await SharedPreferences.getInstance();
    await prefs.remove('recordatorio_$id');
  }

  /// Cancela todos los recordatorios programados
  Future<void> cancelarTodos() async {
    await _notificaciones.cancelAll();
    final prefs = await SharedPreferences.getInstance();
    await prefs.clear();
  }

  /// Verifica y solicita permisos de notificación exacta (Android 12+)
  Future<bool> verificarPermisosExactos() async {
    // En Android 12+ se necesita permiso SCHEDULE_EXACT_ALARM
    final plataforma = Theme.of(context).platform;
    if (platforma == TargetPlatform.android) {
      final androidPlugin = _notificaciones.resolvePlatformSpecificImplementation<
          AndroidFlutterLocalNotificationsPlugin>();
      return await androidPlugin?.requestExactAlarmsPermission() ?? false;
    }
    return true;
  }
}
```

### Manejador del botón SOS de emergencia con geolocalización

```dart
import 'package:geolocator/geolocator.dart';
import 'package:cloud_firestore/cloud_firestore.dart';
import 'package:firebase_auth/firebase_auth.dart';
import 'package:url_launcher/url_launcher.dart';
import 'package:geocoding/geocoding.dart';

/// Servicio que gestiona las emergencias SOS con envío de ubicación
class ServicioEmergenciaSOS {
  final FirebaseFirestore _firestore = FirebaseFirestore.instance;
  final FirebaseAuth _auth = FirebaseAuth.instance;

  /// Datos de contacto de emergencia configurados por el usuario
  static const String TELEFONO_EMERGENCIAS = '112';
  static const String TELEFONO_CUIDADOR = '+34600000000'; // Configurable

  /// Ejecuta el flujo completo de emergencia SOS
  Future<void> activarEmergenciaSOS() async {
    try {
      // 1. Obtener la ubicación actual del dispositivo
      final posicion = await _obtenerUbicacionActual();

      // 2. Obtener dirección legible a partir de las coordenadas
      final direccion = await _obtenerDireccionDesdeCoordenadas(
        latitud: posicion.latitude,
        longitud: posicion.longitude,
      );

      // 3. Registrar la emergencia en Firestore para notificar al cuidador
      final idEmergencia = await _registrarEmergencia(
        latitud: posicion.latitude,
        longitud: posicion.longitude,
        direccion: direccion,
        precision: posicion.accuracy,
      );

      // 4. Enviar notificación push al cuidador (vía Cloud Functions)
      await _notificarCuidador(idEmergencia);

      // 5. Opcional: realizar llamada directa al 112 o al cuidador
      // await _realizarLlamadaEmergencia();

      print('Emergencia SOS activada correctamente. ID: $idEmergencia');
    } catch (e) {
      // En caso de error, intentar al menos realizar la llamada
      print('Error al activar SOS: $e');
      await _realizarLlamadaEmergencia();
      rethrow;
    }
  }

  /// Obtiene la ubicación actual con alta precisión
  Future<Position> _obtenerUbicacionActual() async {
    // Verificar y solicitar permisos de ubicación
    LocationPermission permiso = await Geolocator.checkPermission();
    if (permiso == LocationPermission.denied) {
      permiso = await Geolocator.requestPermission();
      if (permiso == LocationPermission.denied) {
        throw Exception('Permisos de ubicación denegados');
      }
    }

    if (permiso == LocationPermission.deniedForever) {
      throw Exception('Permisos de ubicación denegados permanentemente');
    }

    // Obtener posición con máxima precisión disponible
    return await Geolocator.getCurrentPosition(
      desiredAccuracy: LocationAccuracy.best,
      timeLimit: const Duration(seconds: 15),
    );
  }

  /// Convierte coordenadas en una dirección legible (reverse geocoding)
  Future<String> _obtenerDireccionDesdeCoordenadas({
    required double latitud,
    required double longitud,
  }) async {
    try {
      final direcciones = await placemarkFromCoordinates(latitud, longitud);
      if (direcciones.isNotEmpty) {
        final dir = direcciones.first;
        return '${dir.street}, ${dir.locality}, ${dir.administrativeArea}, ${dir.postalCode}';
      }
      return 'Ubicación no disponible';
    } catch (e) {
      return 'Error al obtener dirección: $e';
    }
  }

  /// Registra la emergencia en la base de datos Firestore
  Future<String> _registrarEmergencia({
    required double latitud,
    required double longitud,
    required String direccion,
    required double precision,
  }) async {
    final usuario = _auth.currentUser;
    if (usuario == null) throw Exception('Usuario no autenticado');

    // Crear documento de emergencia en la colección correspondiente
    final docRef = await _firestore.collection('emergencias').add({
      'uidUsuario': usuario.uid,
      'tipo': 'SOS',
      'fecha': FieldValue.serverTimestamp(),
      'ubicacion': GeoPoint(latitud, longitud),
      'direccion': direccion,
      'precisionMetros': precision,
      'estado': 'activa', // activa, en_proceso, resuelta
      'contactosNotificados': [],
    });

    return docRef.id;
  }

  /// Notifica al cuidador a través de Cloud Functions (FCM)
  Future<void> _notificarCuidador(String idEmergencia) async {
    // Se invoca una Cloud Function que envía push notification al cuidador
    // La función se encarga de:
    // 1. Buscar los cuidadores vinculados al usuario mayor
    // 2. Obtener sus tokens FCM
    // 3. Enviar notificación de alta prioridad con la ubicación
    //
    // En una implementación real, se usaría Firebase Functions:
    // functions.httpsCallable('notificarEmergenciaCuidador').call({
    //   'emergenciaId': idEmergencia,
    // });
    print('Notificación enviada al cuidador para emergencia: $idEmergencia');
  }

  /// Realiza una llamada telefónica al número de emergencias
  Future<void> _realizarLlamadaEmergencia() async {
    final uri = Uri.parse('tel:$TELEFONO_EMERGENCIAS');
    if (await canLaunchUrl(uri)) {
      await launchUrl(uri);
    }
  }
}
```

### Servicio de conexión con dispositivos BLE (Bluetooth Low Energy)

```dart
import 'package:flutter_blue_plus/flutter_blue_plus.dart';
import 'package:cloud_firestore/cloud_firestore.dart';

/// Servicio que gestiona la conexión y comunicación con dispositivos BLE
/// (tensiómetros, pulsioxímetros, glucómetros, etc.)
class ServicioBLE {
  // UUIDs estándar de servicios GATT para dispositivos médicos
  static const String UUID_SERVICIO_BLOOD_PRESSURE = '1810';
  static const String UUID_SERVICIO_PULSE_OXIMETER = '1822';
  static const String UUID_SERVICIO_GLUCOSE = '1808';
  static const String UUID_SERVICIO_HEART_RATE = '180D';

  // Dispositivo actualmente conectado
  BluetoothDevice? _dispositivoConectado;

  /// Stream que emite los dispositivos BLE descubiertos durante el escaneo
  Stream<List<ScanResult>> get dispositivosDescubiertos =>
      FlutterBluePlus.onScanResults;

  /// Verifica si el Bluetooth está activado en el dispositivo
  Future<bool> verificarBluetoothActivado() async {
    final estado = await FlutterBluePlus.adapterState.first;
    return estado == BluetoothAdapterState.on;
  }

  /// Solicita al usuario activar el Bluetooth si está desactivado
  Future<bool> solicitarActivarBluetooth() async {
    try {
      await FlutterBluePlus.turnOn();
      return true;
    } catch (e) {
      print('No se pudo activar Bluetooth: $e');
      return false;
    }
  }

  /// Inicia el escaneo de dispositivos BLE cercanos
  /// Filtra opcionalmente por UUID de servicio médico
  Future<void> iniciarEscaneo({String? uuidServicio}) async {
    // Detener escaneo previo si existe
    await FlutterBluePlus.stopScan();

    // Configurar parámetros del escaneo
    await FlutterBluePlus.startScan(
      timeout: const Duration(seconds: 15),
      withServices: uuidServicio != null
          ? [Guid(uuidServicio)]
          : [], // Escanear todos si no se especifica
      androidUsesFineLocation: true, // Necesario en Android 6+
    );
  }

  /// Detiene el escaneo de dispositivos
  Future<void> detenerEscaneo() async {
    await FlutterBluePlus.stopScan();
  }

  /// Conecta con un dispositivo BLE descubierto
  Future<bool> conectarDispositivo(BluetoothDevice dispositivo) async {
    try {
      // Establecer conexión con timeout de 15 segundos
      await dispositivo.connect(
        timeout: const Duration(seconds: 15),
        autoConnect: false,
      );

      _dispositivoConectado = dispositivo;

      // Descubrir servicios disponibles en el dispositivo
      await dispositivo.discoverServices();

      print('Conectado a: ${dispositivo.platformName}');
      return true;
    } catch (e) {
      print('Error al conectar: $e');
      return false;
    }
  }

  /// Desconecta del dispositivo actual
  Future<void> desconectarDispositivo() async {
    if (_dispositivoConectado != null) {
      await _dispositivoConectado!.disconnect();
      _dispositivoConectado = null;
    }
  }

  /// Lee datos de tensión arterial desde el dispositivo conectado
  Future<Map<String, dynamic>?> leerTensionArterial() async {
    if (_dispositivoConectado == null) return null;

    try {
      // Obtener servicios del dispositivo
      final servicios = await _dispositivoConectado!.discoverServices();

      // Buscar el servicio de tensión arterial por su UUID
      for (final servicio in servicios) {
        if (servicio.uuid.toString() == UUID_SERVICIO_BLOOD_PRESSURE) {
          for (final caracteristica in servicio.characteristics) {
            // Buscar característica de notificación
            if (caracteristica.properties.notify) {
              // Suscribirse a las notificaciones
              await caracteristica.setNotifyValue(true);

              // Escuchar los datos entrantes
              final valor = await caracteristica.lastValueStream.first;

              // Decodificar el formato estándar de tensión arterial BLE
              return _decodificarTensionArterial(valor);
            }
          }
        }
      }
      return null;
    } catch (e) {
      print('Error al leer tensión arterial: $e');
      return null;
    }
  }

  /// Decodifica los datos de tensión arterial según especificación BLE GATT
  Map<String, dynamic> _decodificarTensionArterial(List<int> datos) {
    // Formato estándar IEEE 11073-20601 para BP Measurement
    // Bytes: [flags][systolic][diastolic][map][pulse][timestamp]
    final sistolica = _decodificarSFLOAT(datos[1], datos[2]);
    final diastolica = _decodificarSFLOAT(datos[3], datos[4]);
    final pulso = _decodificarSFLOAT(datos[7], datos[8]);

    return {
      'sistolica': sistolica,
      'diastolica': diastolica,
      'pulso': pulso,
      'unidad': 'mmHg',
      'fecha': DateTime.now(),
    };
  }

  /// Decodifica un valor SFLOAT (2 bytes) según formato IEEE 11073
  double _decodificarSFLOAT(int byte1, int byte2) {
    // Implementación simplificada de decodificación SFLOAT
    final mantisa = byte1 | ((byte2 & 0x0F) << 8);
    final exponente = byte2 >> 4;
    return mantisa * pow(10, exponente);
  }

  /// Guarda las constantes vitales leídas en Firestore
  Future<void> guardarConstantesVitales({
    required String tipo,
    required Map<String, dynamic> datos,
  }) async {
    final usuario = FirebaseAuth.instance.currentUser;
    if (usuario == null) throw Exception('Usuario no autenticado');

    await FirebaseFirestore.instance.collection('constantes_vitales').add({
      'uidUsuario': usuario.uid,
      'tipo': tipo,
      'datos': datos,
      'fuente': 'dispositivo_ble',
      'dispositivo': _dispositivoConectado?.platformName ?? 'desconocido',
      'fecha': FieldValue.serverTimestamp(),
    });
  }
}
```

> **Nota:** Incluir diagrama de secuencia de comunicación BLE como **Figura A.3**.

---

## 13.2 Documentación adicional

### Documentación de endpoints de API

A continuación se documenta un ejemplo de endpoint REST de la API interna utilizada por la aplicación, gestionada a través de Cloud Functions de Firebase.

#### Endpoint: Obtener constantes vitales de un paciente

```
GET /api/v1/pacientes/{id}/vitals
```

**Descripción:** Recupera el historial de constantes vitales de un paciente específico, ordenadas de más reciente a más antigua. Accesible por cuidadores y profesionales sanitarios asignados.

**Autenticación:** Bearer token (Firebase ID Token)

**Parámetros de ruta:**

| Parámetro | Tipo   | Obligatorio | Descripción                        |
|-----------|--------|-------------|------------------------------------|
| `id`      | string | Sí          | UID del paciente en Firebase Auth  |

**Parámetros de consulta (query params):**

| Parámetro | Tipo   | Obligatorio | Valor por defecto | Descripción                                      |
|-----------|--------|-------------|-------------------|--------------------------------------------------|
| `tipo`    | string | No          | `todos`           | Filtrar por tipo: `tension`, `glucosa`, `saturacion`, `temperatura`, `todos` |
| `desde`   | string | No          | Hace 7 días       | Fecha inicio en formato ISO 8601 (YYYY-MM-DD)    |
| `hasta`   | string | No          | Hoy               | Fecha fin en formato ISO 8601 (YYYY-MM-DD)       |
| `limite`  | number | No          | 50                | Número máximo de registros a devolver (max: 200) |

**Cabeceras de la petición:**

| Cabecera        | Valor                          |
|-----------------|--------------------------------|
| `Authorization` | `Bearer <firebase_id_token>`   |
| `Content-Type`  | `application/json`             |
| `X-App-Version` | `1.0.0`                        |

**Ejemplo de petición:**

```http
GET /api/v1/pacientes/abc123def456/vitals?tipo=tension&desde=2025-01-01&hasta=2025-01-31&limite=30
Authorization: Bearer eyJhbGciOiJSUzI1NiIsImtpZCI6IjE2MmMzNj...
Content-Type: application/json
```

**Ejemplo de respuesta exitosa (200 OK):**

```json
{
  "success": true,
  "data": {
    "pacienteId": "abc123def456",
    "nombre": "María García López",
    "totalRegistros": 24,
    "periodo": {
      "desde": "2025-01-01",
      "hasta": "2025-01-31"
    },
    "registros": [
      {
        "id": "vit_001",
        "tipo": "tension",
        "valor": {
          "sistolica": 135,
          "diastolica": 82,
          "pulso": 72,
          "unidad": "mmHg"
        },
        "fuente": "dispositivo_ble",
        "dispositivo": "Omron X3 Comfort",
        "fecha": "2025-01-30T09:15:00Z",
        "observaciones": ""
      },
      {
        "id": "vit_002",
        "tipo": "tension",
        "valor": {
          "sistolica": 140,
          "diastolica": 85,
          "pulso": 78,
          "unidad": "mmHg"
        },
        "fuente": "manual",
        "dispositivo": null,
        "fecha": "2025-01-29T08:30:00Z",
        "observaciones": "Ligero dolor de cabeza"
      }
    ],
    "estadisticas": {
      "sistolica": {
        "media": 136.2,
        "min": 120,
        "max": 152,
        "ultima": 135
      },
      "diastolica": {
        "media": 81.5,
        "min": 70,
        "max": 90,
        "ultima": 82
      }
    }
  },
  "timestamp": "2025-01-30T10:00:00Z"
}
```

**Ejemplo de respuesta de error (403 Forbidden):**

```json
{
  "success": false,
  "error": {
    "codigo": "PERMISO_DENEGADO",
    "mensaje": "No tiene permisos para acceder a las constantes vitales de este paciente",
    "detalles": "El usuario solo puede acceder a pacientes asignados a su perfil"
  },
  "timestamp": "2025-01-30T10:00:00Z"
}
```

**Códigos de error:**

| Código HTTP | Código interno         | Descripción                                                |
|-------------|------------------------|------------------------------------------------------------|
| 400         | PARAMETRO_INVALIDO     | Parámetro de consulta con formato incorrecto               |
| 401         | NO_AUTENTICADO         | Token de autenticación ausente o expirado                  |
| 403         | PERMISO_DENEGADO       | Usuario autenticado pero sin permisos para este recurso    |
| 404         | PACIENTE_NO_ENCONTRADO | El ID de paciente no existe en la base de datos            |
| 429         | LIMITE_EXCEDIDO        | Demasiadas peticiones en corto periodo de tiempo           |
| 500         | ERROR_INTERNO          | Error inesperado en el servidor                            |

> **Nota:** Incluir documentación completa de todos los endpoints en un archivo separado con especificación OpenAPI/Swagger como **Documento Anexo A.2.1**.

### Documentos técnicos adicionales

Los siguientes documentos complementarios deben incluirse como anexos adicionales para proporcionar una visión completa del proyecto:

| # | Documento | Descripción | Formato sugerido |
|---|-----------|-------------|------------------|
| 1 | **Diagrama Entidad-Relación (ER)** | Modelo de datos completo de Firestore mostrando colecciones, documentos, subcolecciones y relaciones entre entidades | Diagrama (draw.io, Lucidchart) |
| 2 | **Especificación OpenAPI/Swagger** | Documentación completa de todos los endpoints de Cloud Functions con esquemas de petición/respuesta, autenticación y códigos de error | YAML/JSON + Swagger UI |
| 3 | **Matriz de trazabilidad de requisitos** | Tabla que vincula cada requisito funcional y no funcional con su implementación (módulos, pruebas) y validación | Tabla Excel |
| 4 | **Informe de auditoría de seguridad** | Análisis de vulnerabilidades, reglas de seguridad de Firestore, cifrado de datos, gestión de tokens y resultados de pruebas de penetración | Documento PDF |
| 5 | **Resultados de pruebas de usabilidad** | Informes de sesiones de prueba con usuarios reales (personas mayores, cuidadores), métricas SUS (System Usability Scale), tareas completadas, tiempos y errores | Documento PDF con gráficos |
| 6 | **Manual de instalación del entorno de desarrollo** | Guía paso a paso para configurar Flutter, Firebase CLI, emuladores, variables de entorno y dependencias | Markdown/PDF |
| 7 | **Política de privacidad y tratamiento de datos (RGPD)** | Documento legal que detalla el tratamiento de datos de salud, consentimiento informado, derechos ARCO y medidas de seguridad conforme al Reglamento General de Protección de Datos de la UE | Documento PDF |
| 8 | **Informe de análisis de rendimiento** | Métricas de rendimiento de la app: tiempo de inicio, consumo de memoria, FPS, uso de red, consumo de batería y resultados de profiling | Documento PDF con gráficos |

> **Nota:** El Diagrama ER se recomienda incluir como **Figura A.4** y la Matriz de trazabilidad como **Tabla A.1**.

---

## 13.3 Planificación temporal

### Diagrama de Gantt (16 semanas)

A continuación se presenta la planificación temporal del proyecto distribuida en 16 semanas, organizada por fases principales.

**Tabla A.2: Diagrama de Gantt del proyecto**

| Fase | S1 | S2 | S3 | S4 | S5 | S6 | S7 | S8 | S9 | S10 | S11 | S12 | S13 | S14 | S15 | S16 |
|------|:--:|:--:|:--:|:--:|:--:|:--:|:--:|:--:|:--:|:---:|:---:|:---:|:---:|:---:|:---:|:---:|
| **1. Análisis y planificación** | ████ | ████ | | | | | | | | | | | | | | |
| **2. Diseño UX/UI** | | ██ | ████ | ████ | | | | | | | | | | | | |
| **3. Configuración del proyecto** | | | ██ | ████ | | | | | | | | | | | | |
| **4. Desarrollo Autenticación** | | | | ██ | ████ | | | | | | | | | | | |
| **5. Perfil Persona Mayor** | | | | | | ████ | ████ | ████ | | | | | | | | |
| **6. Perfil Cuidador** | | | | | | | | ██ | ████ | ████ | | | | | | |
| **7. Perfil Profesional** | | | | | | | | | | ████ | ████ | | | | | |
| **8. Integración IoT (BLE)** | | | | | | | | | | | | ████ | ████ | | | |
| **9. Pruebas y QA** | | | | | | | | | | | | | ██ | ████ | | |
| **10. Despliegue** | | | | | | | | | | | | | | ██ | ████ | |
| **11. Documentación** | | | | | | | | | | | | | | | ████ | ████ |

**Leyenda de intensidad:**
- `████` = Fase principal (dedicación completa)
- `██` = Fase parcial / transición

**Tabla A.3: Distribución de carga semanal por fase**

| Semana | Dedicación | Entregable clave |
|--------|------------|------------------|
| S1-S2 | 100% Análisis | Documento de requisitos firmado |
| S3-S4 | 50% Diseño / 50% Setup | Prototipos Figma + proyecto Flutter creado |
| S5-S6 | 70% Setup / 30% Auth | Firebase configurado + login funcional |
| S7-S9 | 100% Perfil Mayor | App funcional para persona mayor |
| S10-S11 | 100% Perfil Cuidador | Panel de cuidador completo |
| S12 | 60% Profesional / 40% IoT | Panel profesional básico + inicio BLE |
| S13-S14 | 70% IoT / 30% Pruebas | Dispositivos BLE integrados |
| S15 | 100% Despliegue | App publicada en stores |
| S16 | 100% Documentación | Memoria del TFG completa |

> **Nota:** Incluir diagrama de Gantt visual generado con herramienta como GanttProject o MS Project como **Figura A.5**.

### Estructura de Desglose de Trabajo (EDT / WBS)

La siguiente estructura jerárquica detalla todas las tareas del proyecto:

```
1.0 CuidadoMayor App (Proyecto TFG)
├── 1.1 Análisis y Planificación
│   ├── 1.1.1 Estudio de mercado y aplicaciones existentes
│   ├── 1.1.2 Definición de requisitos funcionales
│   ├── 1.1.3 Definición de requisitos no funcionales
│   ├── 1.1.4 Selección de tecnologías y stack
│   ├── 1.1.5 Planificación temporal y recursos
│   └── 1.1.6 Documento de especificación de requisitos
├── 1.2 Diseño UX/UI
│   ├── 1.2.1 Investigación de usuarios (personas mayores, cuidadores)
│   ├── 1.2.2 Definición de arquetipos de usuario (personas)
│   ├── 1.2.3 Mapa de navegación y flujo de usuario
│   ├── 1.2.4 Wireframes de baja fidelidad
│   ├── 1.2.5 Diseño visual de alta fidelidad (Figma)
│   │   ├── 1.2.5.1 Sistema de diseño (colores, tipografía, componentes)
│   │   ├── 1.2.5.2 Pantallas perfil Persona Mayor
│   │   ├── 1.2.5.3 Pantallas perfil Cuidador
│   │   └── 1.2.5.4 Pantallas perfil Profesional
│   ├── 1.2.6 Prototipo interactivo
│   └── 1.2.7 Validación y revisión del diseño
├── 1.3 Configuración del Proyecto
│   ├── 1.3.1 Creación proyecto Flutter multiplataforma
│   ├── 1.3.2 Configuración arquitectura Clean Architecture + MVVM
│   ├── 1.3.3 Configuración Firebase Console
│   │   ├── 1.3.3.1 Firebase Authentication
│   │   ├── 1.3.3.2 Cloud Firestore (base de datos)
│   │   ├── 1.3.3.3 Cloud Storage (imágenes, documentos)
│   │   ├── 1.3.3.4 Firebase Cloud Messaging (notificaciones)
│   │   └── 1.3.3.5 Firebase App Check (seguridad)
│   ├── 1.3.4 Configuración CI/CD (GitHub Actions)
│   ├── 1.3.5 Configuración de emuladores locales
│   └── 1.3.6 Configuración de control de versiones (Git)
├── 1.4 Desarrollo del Módulo de Autenticación
│   ├── 1.4.1 Registro de usuarios con validación
│   ├── 1.4.2 Inicio de sesión con email/contraseña
│   ├── 1.4.3 Inicio de sesión con Google (OAuth)
│   ├── 1.4.4 Recuperación de contraseña
│   ├── 1.4.5 Gestión de roles y perfiles
│   ├── 1.4.6 Pantalla de selección de rol al iniciar sesión
│   └── 1.4.7 Cierre de sesión y gestión de sesión persistente
├── 1.5 Desarrollo del Perfil Persona Mayor
│   ├── 1.5.1 Pantalla principal (dashboard simplificado)
│   │   ├── 1.5.1.1 Diseño con botones grandes y alto contraste
│   │   ├── 1.5.1.2 Resumen diario (medicación, citas, constantes)
│   │   └── 1.5.1.3 Accesos directos a funciones frecuentes
│   ├── 1.5.2 Módulo de medicación
│   │   ├── 1.5.2.1 Lista de medicamentos actuales
│   │   ├── 1.5.2.2 Visualización de horario de tomas
│   │   ├── 1.5.2.3 Registro de tomas realizadas
│   │   └── 1.5.2.4 Notificaciones de recordatorio
│   ├── 1.5.3 Botón SOS de emergencia
│   │   ├── 1.5.3.1 Botón de acceso rápido siempre visible
│   │   ├── 1.5.3.2 Geolocalización y envío de ubicación
│   │   ├── 1.5.3.3 Notificación automática al cuidador
│   │   └── 1.5.3.4 Opción de llamada directa al 112
│   ├── 1.5.4 Registro de constantes vitales
│   │   ├── 1.5.4.1 Formulario de entrada manual
│   │   ├── 1.5.4.2 Visualización de historial (gráficos simples)
│   │   └── 1.5.4.3 Historial de constantes con tendencias
│   ├── 1.5.5 Módulo de videollamadas
│   │   ├── 1.5.5.1 Integración SDK de videollamadas
│   │   ├── 1.5.5.2 Lista de contactos disponibles
│   │   └── 1.5.5.3 Interfaz de llamada simplificada
│   ├── 1.5.6 Configuración de accesibilidad
│   │   ├── 1.5.6.1 Texto ampliable
│   │   ├── 1.5.6.2 Modo alto contraste
│   │   └── 1.5.6.3 Asistente de voz integrado
│   └── 1.5.7 Contacto con cuidador
│       ├── 1.5.7.1 Llamada directa al cuidador
│       ├── 1.5.7.2 Envío de mensaje predefinido
│       └── 1.5.7.3 Estado del cuidador (disponible/ocupado)
├── 1.6 Desarrollo del Perfil Cuidador
│   ├── 1.6.1 Panel de control del cuidador
│   │   ├── 1.6.1.1 Vista general de todos los familiares asignados
│   │   ├── 1.6.1.2 Indicadores de estado en tiempo real
│   │   └── 1.6.1.3 Resumen de alertas pendientes
│   ├── 1.6.2 Gestión de familiares
│   │   ├── 1.6.2.1 Añadir familiar (vinculación por código)
│   │   ├── 1.6.2.2 Editar datos del familiar
│   │   └── 1.6.2.3 Gestión de datos médicos del familiar
│   ├── 1.6.3 Panel de monitorización
│   │   ├── 1.6.3.1 Constantes vitales en tiempo real
│   │   ├── 1.6.3.2 Historial con gráficos de tendencias
│   │   ├── 1.6.3.3 Umbrales de alerta personalizables
│   │   └── 1.6.3.4 Indicador de adherencia a la medicación
│   ├── 1.6.4 Sistema de alertas
│   │   ├── 1.6.4.1 Alertas de constantes fuera de rango
│   │   ├── 1.6.4.2 Alertas de medicación no tomada
│   │   ├── 1.6.4.3 Alertas de emergencia SOS
│   │   └── 1.6.4.4 Configuración de alertas personalizables
│   ├── 1.6.5 Historial de medicación
│   │   ├── 1.6.5.1 Programación de medicación
│   │   ├── 1.6.5.2 Historial de tomas (cumplimiento)
│   │   └── 1.6.5.3 Edición de horarios y dosis
│   ├── 1.6.6 Contacto con médico
│   │   ├── 1.6.6.1 Chat con el profesional asignado
│   │   ├── 1.6.6.2 Compartir constantes vitales
│   │   └── 1.6.6.3 Solicitar cita
│   └── 1.6.7 Informes mensuales
│       ├── 1.6.7.1 Generación automática de informe
│       ├── 1.6.7.2 Resumen de constantes vitales
│       ├── 1.6.7.3 Adherencia a la medicación
│       └── 1.6.7.4 Exportación en PDF
├── 1.7 Desarrollo del Perfil Profesional Sanitario
│   ├── 1.7.1 Panel de pacientes
│   │   ├── 1.7.1.1 Lista de pacientes asignados
│   │   ├── 1.7.1.2 Estado de cada paciente (alertas activas)
│   │   └── 1.7.1.3 Búsqueda y filtros de pacientes
│   ├── 1.7.2 Revisión de constantes vitales
│   │   ├── 1.7.2.1 Gráficos detallados por tipo de constante
│   │   ├── 1.7.2.2 Comparativa temporal
│   │   └── 1.7.2.3 Exportación de datos
│   ├── 1.7.3 Valoración geriátrica
│   │   ├── 1.7.3.1 Formularios de evaluación estandarizados
│   │   ├── 1.7.3.2 Escala de Barthel (autonomía)
│   │   ├── 1.7.3.3 Mini-Mental State Examination (MMSE)
│   │   └── 1.7.3.4 Historial de valoraciones
│   ├── 1.7.4 Módulo de prescripción
│   │   ├── 1.7.4.1 Creación y edición de pautas de medicación
│   │   ├── 1.7.4.2 Historial de prescripciones
│   │   └── 1.7.4.3 Notificación de cambios al cuidador
│   └── 1.7.5 Videoconsultas
│       ├── 1.7.5.1 Programación de videoconsultas
│       ├── 1.7.5.2 Interfaz de videoconsulta
│       └── 1.7.5.3 Notas post-consulta
├── 1.8 Integración IoT (Bluetooth Low Energy)
│   ├── 1.8.1 Escaneo de dispositivos BLE cercanos
│   ├── 1.8.2 Emparejamiento con dispositivos médicos
│   │   ├── 1.8.2.1 Tensiómetros digitales
│   │   ├── 1.8.2.2 Pulsioxímetros
│   │   ├── 1.8.2.3 Glucómetros
│   │   └── 1.8.2.4 Termómetros inteligentes
│   ├── 1.8.3 Lectura automática de mediciones
│   ├── 1.8.4 Almacenamiento en Firestore
│   └── 1.8.5 Gestión de conexión y reconexión automática
├── 1.9 Pruebas y Control de Calidad
│   ├── 1.9.1 Pruebas unitarias (servicios, repositorios)
│   ├── 1.9.2 Pruebas de integración (módulos)
│   ├── 1.9.3 Pruebas de interfaz de usuario (widget tests)
│   ├── 1.9.4 Pruebas de rendimiento y consumo
│   ├── 1.9.5 Pruebas de seguridad (Firestore rules)
│   ├── 1.9.6 Pruebas de usabilidad con usuarios reales
│   └── 1.9.7 Corrección de bugs y optimización
├── 1.10 Despliegue
│   ├── 1.10.1 Preparación para Google Play Store
│   │   ├── 1.10.1.1 Firmado de APK/AAB
│   │   ├── 1.10.1.2 Capturas de pantalla y descripción
│   │   └── 1.10.1.3 Política de privacidad
│   ├── 1.10.2 Preparación para Apple App Store
│   │   ├── 1.10.2.1 Firmado de IPA
│   │   ├── 1.10.2.2 Capturas de pantalla y descripción
│   │   └── 1.10.2.3 Cumplimiento guidelines de Apple
│   └── 1.10.3 Despliegue de Cloud Functions a producción
└── 1.11 Documentación del Proyecto
    ├── 1.11.1 Memoria del TFG
    ├── 1.11.2 Documentación técnica del código
    ├── 1.11.3 Manual de usuario
    ├── 1.11.4 Manual técnico
    └── 1.11.5 Presentación para defensa
```

### Hitos del proyecto

**Tabla A.4: Hitos principales del proyecto**

| Hito | Semana | Entregable | Criterio de aceptación |
|------|--------|------------|------------------------|
| **H1: Especificación aprobada** | S2 | Documento de requisitos funcionales y no funcionales | Validado por tutor del TFG |
| **H2: Prototipo UI validado** | S4 | Prototipo interactivo en Figma con los 3 perfiles | Revisado y aprobado por tutor |
| **H3: Autenticación funcional** | S6 | Login, registro y gestión de roles operativa | Pruebas de autenticación superadas |
| **H4: App mayor funcional** | S9 | Perfil persona mayor completo con medicación, SOS y vitales | Funcional en emulador y dispositivo físico |
| **H5: Versión beta completa** | S14 | Los 3 perfiles funcionando + integración BLE | Todas las pruebas QA superadas |
| **H6: Entrega final** | S16 | App publicada + memoria del TFG + documentación | TFG entregado y defendido |

> **Nota:** Incluir diagrama de hitos visual como **Figura A.6**.

---

## 13.4 Manual de usuario

> **Nota:** Este apartado contiene un esquema estructurado del manual de usuario completo. Se recomienda generar el manual completo como documento PDF separado adjunto a la memoria del TFG.

### 1. Introducción

#### 1.1 ¿Qué es CuidadoMayor?

CuidadoMayor es una aplicación multiplataforma diseñada para mejorar la calidad de vida de las personas mayores que viven solas o semi-solas, facilitando la conexión con sus cuidadores familiares y profesionales sanitarios. La aplicación permite:

- Recordatorios inteligentes de medicación
- Monitorización de constantes vitales (tensión arterial, glucosa, saturación de oxígeno, temperatura)
- Botón de emergencia SOS con geolocalización automática
- Videollamadas con cuidadores y médicos
- Conexión con dispositivos médicos Bluetooth (tensiómetros, pulsioxímetros, glucómetros)

#### 1.2 ¿Para quién está diseñada?

| Perfil | Descripción |
|--------|-------------|
| **Persona Mayor** | Usuarios principales de 65+ años. Interfaz simplificada con botones grandes, alto contraste y texto ampliable. |
| **Cuidador Familiar** | Familiares o cuidadores que monitorizan el estado de uno o más familiares mayores. Panel de control con alertas e informes. |
| **Profesional Sanitario** | Médicos, enfermeros y geriatras que realizan seguimiento clínico de pacientes. Acceso a historial de constantes, valoraciones geriátricas y videoconsultas. |

#### 1.3 Requisitos del dispositivo

| Requisito | Android | iOS |
|-----------|---------|-----|
| **Versión mínima** | Android 8.0 (API 26) | iOS 14.0 |
| **RAM mínima** | 2 GB | 2 GB |
| **Almacenamiento** | 150 MB libres | 150 MB libres |
| **Bluetooth** | BLE 4.0+ (opcional) | BLE 4.0+ (opcional) |
| **Conexión** | WiFi o datos móviles | WiFi o datos móviles |
| **Permisos necesarios** | Ubicación, Bluetooth, Notificaciones, Cámara, Micrófono | Ubicación, Bluetooth, Notificaciones, Cámara, Micrófono |

### 2. Primeros pasos

#### 2.1 Descarga e instalación

1. Abra **Google Play Store** (Android) o **App Store** (iOS)
2. Busque **"CuidadoMayor"**
3. Pulse **"Instalar"** o **"Obtener"**
4. Espere a que finalice la descarga
5. Abra la aplicación desde el menú de su dispositivo

> [Insertar captura de pantalla de la app en la tienda correspondiente - **Figura A.7**]

#### 2.2 Creación de cuenta

1. Al abrir la app por primera vez, pulse **"Crear cuenta"**
2. Introduzca su **correo electrónico** y pulse "Siguiente"
3. Verifique su correo con el **código de 6 dígitos** recibido
4. Introduzca su **nombre completo**, **apellidos** y **teléfono**
5. Seleccione su **perfil de usuario**:
   - 👴 **Persona Mayor** - Si es el usuario que recibirá cuidados
   - 👨‍👩‍👧 **Cuidador** - Si va a cuidar de uno o más familiares
   - ⚕️ **Profesional Sanitario** - Si es médico o personal sanitario
6. Cree una **contraseña segura** (mínimo 8 caracteres, con mayúsculas, minúsculas y números)
7. Acepte los **Términos y Condiciones** y la **Política de Privacidad**
8. Pulse **"Registrarse"**

> [Insertar capturas del flujo de registro - **Figura A.8, A.9, A.10**]

#### 2.3 Configuración inicial

Tras crear la cuenta, la aplicación le guiará por un asistente de configuración:

**Para personas mayores:**
- Configurar contactos de emergencia (cuidador principal, familiar alternativo)
- Configurar horarios de medicación (o vincular con cuidador)
- Activar notificaciones de recordatorio
- Configurar preferencias de accesibilidad (tamaño de texto, contraste)

**Para cuidadores:**
- Añadir familiar mayor (mediante código de vinculación)
- Configurar alertas y umbrales de aviso
- Programar medicación del familiar
- Configurar datos del médico de cabecera

**Para profesionales sanitarios:**
- Introducir número de colegiado
- Configurar especialidad y centro de trabajo
- Solicitar acceso a pacientes (requiere aprobación del cuidador)

### 3. Guía para personas mayores

#### 3.1 Pantalla principal

La pantalla principal muestra los elementos más importantes del día:

> [Insertar captura de pantalla de la pantalla principal del perfil mayor - **Figura A.11**]

| Elemento | Función |
|----------|---------|
| **Saludo personalizado** | Muestra la hora del día y el nombre del usuario |
| **Próxima medicación** | Muestra qué medicamento toca tomar y a qué hora |
| **Botón SOS** | Botón rojo siempre visible en la parte inferior |
| **Constantes vitales** | Acceso rápido al registro de tensión, glucosa, etc. |
| **Mis cuidadores** | Lista de contactos de confianza |
| **Videollamada** | Acceso directo para llamar al cuidador |

#### 3.2 Recordatorios de medicación

**¿Cómo funcionan?**
- La aplicación le avisará con un **sonido y una notificación** cada vez que sea hora de tomar un medicamento
- La notificación muestra el **nombre del medicamento** y la **dosis**
- Toque **"Ya lo he tomado"** para confirmar
- Toque **"Recordar en 15 min"** para posponer la alerta

> [Insertar captura de pantalla de notificación de medicación - **Figura A.12**]

**Si olvida una toma:**
- Tras 30 minutos sin confirmar, se enviará una **notificación al cuidador**
- El cuidador podrá llamarle o enviarle un mensaje de recordatorio

#### 3.3 Botón SOS de emergencia

**¿Cómo activarlo?**
1. Pulse el **botón rojo SOS** en la parte inferior de la pantalla principal
2. Manténgalo pulsado durante **3 segundos** (para evitar activaciones accidentales)
3. Se enviará automáticamente:
   - Una **notificación de emergencia** a su cuidador
   - Su **ubicación actual** con la dirección exacta
   - Una opción de **llamada directa al 112**

> [Insertar captura del botón SOS y pantalla de confirmación - **Figura A.13**]

**¿Qué ocurre después?**
- Su cuidador recibirá una **notificación de alta prioridad** en su teléfono
- El cuidador verá su **ubicación en el mapa** en tiempo real
- Si no responde en 2 minutos, se sugerirá llamar al **112**

#### 3.4 Registro de constantes vitales

**Registro manual:**
1. Pulse **"Mis Constantes"** en la pantalla principal
2. Seleccione el tipo de medida:
   - ❤️ **Tensión arterial** (sistólica/diastólica)
   - 🩸 **Glucosa** (mg/dL)
   - 🫁 **Saturación de oxígeno** (%)
   - 🌡️ **Temperatura** (°C)
   - 💓 **Frecuencia cardíaca** (lpm)
3. Introduzca los valores con los botones grandes +/- o escribiendo
4. Pulse **"Guardar"**

**Con dispositivo Bluetooth:**
1. Pulse **"Conectar dispositivo"**
2. Seleccione su dispositivo de la lista
3. Realice la medición según las instrucciones del dispositivo
4. Los valores se guardarán **automáticamente**

> [Insertar captura del formulario de constantes vitales - **Figura A.14**]

#### 3.5 Videollamadas

1. Pulse **"Videollamada"** en la pantalla principal
2. Seleccione a quién desea llamar:
   - Su **cuidador principal**
   - Su **médico** (si tiene cita programada)
   - Otro **familiar de confianza**
3. Pulse el botón verde de **llamar**
4. Para finalizar, pulse el botón rojo de **colgar**

> **Consejo:** Asegúrese de tener buena conexión WiFi para una videollamada de calidad.

#### 3.6 Contactar con el cuidador

**Opciones disponibles:**
- 📞 **Llamar**: Toque el icono del teléfono junto al nombre del cuidador
- 💬 **Mensaje**: Toque el icono del mensaje para enviar un texto
- 📹 **Videollamada**: Toque el icono de la cámara
- 🆘 **Emergencia**: Use el botón SOS

**Mensajes rápidos predefinidos:**
Para facilitar la comunicación, puede enviar mensajes predefinidos sin necesidad de escribir:
- "Estoy bien, no te preocupes"
- "Necesito ayuda"
- "He tomado la medicación"
- "No me encuentro bien"
- "Necesito ir al médico"

### 4. Guía para cuidadores

#### 4.1 Añadir un familiar mayor

1. Pulse **"Añadir familiar"** en el panel principal
2. Seleccione **"Generar código de vinculación"**
3. Comparta el código con su familiar (por SMS, teléfono o en persona)
4. El familiar debe introducir el código en su app en **Configuración > Vincular cuidador**
5. Una vez vinculado, aparecerá en su lista de familiares

> [Insertar captura del proceso de vinculación - **Figura A.15**]

#### 4.2 Panel de monitorización

El panel principal del cuidador muestra:

> [Insertar captura del panel de monitorización del cuidador - **Figura A.16**]

| Sección | Contenido |
|---------|-----------|
| **Estado general** | Indicador visual del estado de cada familiar (verde/amarillo/rojo) |
| **Últimas constantes** | Valores más recientes de tensión, glucosa, saturación y temperatura |
| **Alertas activas** | Lista de alertas que requieren atención |
| **Adherencia medicación** | Porcentaje de tomas confirmadas hoy |
| **Última actividad** | Hora de la última interacción del familiar con la app |

#### 4.3 Alertas

**Tipos de alerta:**

| Tipo | Color | Descripción | Acción recomendada |
|------|-------|-------------|-------------------|
| 🔴 **Emergencia SOS** | Rojo | El familiar ha pulsado el botón SOS | Contactar inmediatamente y verificar ubicación |
| 🟠 **Constante fuera de rango** | Naranja | Tensión, glucosa u otra constante fuera de los límites | Contactar al familiar y valorar llamar al médico |
| 🟡 **Medicación no tomada** | Amarillo | Han pasado más de 30 min de la hora de la toma | Llamar o enviar mensaje de recordatorio |
| 🔵 **Inactividad prolongada** | Azul | El familiar no ha abierto la app en varias horas | Contactar para verificar que está bien |

**Configuración de umbrales:**
1. Pulse **"Configuración" > "Alertas"**
2. Seleccione el familiar
3. Ajuste los valores mínimos y máximos para cada constante:
   - Tensión sistólica: 90 - 140 mmHg (por defecto)
   - Tensión diastólica: 60 - 90 mmHg (por defecto)
   - Glucosa: 70 - 180 mg/dL (por defecto)
   - Saturación O₂: 92% - 100% (por defecto)
   - Temperatura: 36.0°C - 37.5°C (por defecto)

#### 4.4 Historial de medicación

1. Seleccione al familiar en la lista
2. Pulse **"Medicación"**
3. Verá:
   - **Lista de medicamentos** activos con horarios
   - **Calendario de tomas** con marcas verdes (tomadas) y rojas (omitidas)
   - **Porcentaje de adherencia** semanal y mensual
4. Para **modificar** la medicación: pulse el medicamento y edite dosis/horario
5. Para **añadir** medicación: pulse "+" e introduzca los datos

> [Insertar captura del historial de medicación con gráfico de adherencia - **Figura A.17**]

#### 4.5 Contactar con el médico

1. Pulse **"Médico"** en el menú del familiar
2. Opciones disponibles:
   - 📞 **Llamar al médico** (si hay teléfono registrado)
   - 💬 **Chat** (mensajes asíncronos con el profesional asignado)
   - 📊 **Compartir constantes** (envía un resumen de las últimas constantes)
   - 📅 **Solicitar cita** (formulario de solicitud de cita)

#### 4.6 Informes mensuales

1. Pulse **"Informes"** en el menú principal
2. Seleccione el **familiar** y el **mes** deseado
3. El informe incluye:
   - Resumen de **constantes vitales** con gráficos de tendencia
   - **Adherencia a la medicación** (porcentaje y días omitidos)
   - **Alertas recibidas** clasificadas por tipo y gravedad
   - **Actividad general** (veces que abrió la app, videollamadas realizadas)
4. Pulse **"Exportar PDF"** para descargar o compartir el informe

> [Insertar ejemplo de informe mensual exportado - **Figura A.18**]

### 5. Guía para profesionales sanitarios

#### 5.1 Panel de pacientes

1. Al iniciar sesión como profesional, verá su **panel de pacientes**
2. Cada paciente muestra:
   - Nombre y edad
   - Estado actual (indicador de color)
   - Última constante vital registrada
   - Alertas activas

> [Insertar captura del panel de pacientes del profesional - **Figura A.19**]

#### 5.2 Revisión de constantes vitales

1. Seleccione un **paciente** de la lista
2. Pulse **"Constantes vitales"**
3. Seleccione el **tipo de constante** y el **periodo** a visualizar
4. El gráfico mostrará la **evolución temporal** con líneas de referencia (valores normales)
5. Puede **exportar los datos** en formato CSV o PDF para incluir en la historia clínica

#### 5.3 Valoración geriátrica

1. Seleccione un **paciente**
2. Pulse **"Valoraciones"**
3. Seleccione el tipo de evaluación:
   - **Escala de Barthel**: Evalúa la autonomía en actividades básicas de la vida diaria (ABVD). Puntuación de 0-100.
   - **Mini-Mental (MMSE)**: Evaluación del estado cognitivo. Puntuación de 0-30.
   - **Test de Yesavage (GDS)**: Detección de depresión geriátrica. Puntuación de 0-15.
   - **Escala de Lawton**: Evaluación de actividades instrumentales (AIVD).
4. Complete el formulario y pulse **"Guardar"**
5. Las valoraciones anteriores se muestran en un **historial comparativo**

#### 5.4 Prescripción de medicación

1. Seleccione un **paciente**
2. Pulse **"Prescripción"**
3. Pulse **"Añadir medicamento"** e introduzca:
   - Nombre del medicamento
   - Dosis (mg, mL, etc.)
   - Frecuencia (cada X horas / veces al día)
   - Horarios específicos
   - Duración del tratamiento
   - Instrucciones especiales (en ayunas, con comidas, etc.)
4. Pulse **"Guardar y notificar al cuidador"**
5. El cuidador recibirá una **notificación** con los cambios en la medicación

#### 5.5 Videoconsultas

1. Pulse **"Videoconsultas"** en el menú principal
2. **Programar nueva consulta:**
   - Seleccione el paciente
   - Elija fecha y hora
   - Añada motivo de la consulta
   - Pulse **"Enviar invitación"** (el cuidador y el paciente recibirán notificación)
3. **Iniciar consulta programada:**
   - Pulse **"Iniciar"** en la hora programada
   - Espere a que el paciente se conecte
   - Durante la consulta puede tomar **notas clínicas**
4. **Finalizar consulta:**
   - Pulse **"Finalizar"**
   - Complete las **notas post-consulta**
   - Las notas se guardan en el **historial clínico** del paciente

### 6. Preguntas frecuentes (FAQ)

**Tabla A.5: Preguntas frecuentes de la aplicación**

| # | Pregunta | Respuesta |
|---|----------|-----------|
| 1 | **¿Es gratuita la aplicación?** | La aplicación es gratuita para personas mayores y cuidadores. Los profesionales sanitarios pueden necesitar una suscripción institucional proporcionada por su centro de salud. |
| 2 | **¿Qué hago si olvido mi contraseña?** | En la pantalla de inicio de sesión, pulse "¿Olvidaste tu contraseña?" e introduzca su correo electrónico. Recibirá un enlace para restablecerla. Si no tiene acceso al correo, contacte con su cuidador para que le ayude. |
| 3 | **¿Puedo usar la app sin conexión a Internet?** | Las funciones básicas como ver la medicación programada, el botón SOS (almacena la emergencia y la envía al recuperar conexión) y las constantes vitales guardadas están disponibles sin conexión. Las videollamadas y la sincronización de datos requieren Internet. |
| 4 | **¿Mi dispositivo Bluetooth es compatible?** | La aplicación es compatible con la mayoría de tensiómetros, pulsioxímetros, glucómetros y termómetros digitales que utilicen Bluetooth 4.0 (BLE) con perfiles médicos estándar. Consulte la lista de dispositivos compatibles en nuestra web. |
| 5 | **¿Quién puede ver mis datos de salud?** | Solo usted, sus cuidadores vinculados y los profesionales sanitarios que usted o su cuidador autoricen. Los datos están cifrados y protegidos conforme al RGPD. Nadie más tiene acceso. |
| 6 | **¿Qué ocurre si pulso el botón SOS por error?** | No se preocupe. Tras pulsar el SOS, tiene 10 segundos para **cancelar** la emergencia deslizando el botón de cancelar. Si ya se envió la alerta, puede llamar a su cuidador para indicarle que fue un error. |
| 7 | **¿Puedo tener más de un cuidador?** | Sí. Puede vincular a varios cuidadores familiares. Cada uno podrá monitorizar su estado y recibir alertas. Además, puede designar un **cuidador principal** que recibirá las alertas en primer lugar. |
| 8 | **¿La aplicación funciona en tablet?** | Sí. La aplicación está optimizada para smartphones y tablets tanto en Android como en iOS. En tablets, la interfaz se adapta para aprovechar la pantalla más grande. |
| 9 | **¿Cómo puedo cambiar el tamaño del texto?** | En la pantalla principal, pulse **Configuración (⚙️) > Accesibilidad > Tamaño de texto**. Puede elegir entre Normal, Grande y Muy Grande. También puede activar el **modo alto contraste**. |
| 10 | **¿Qué hago si la app no envía las notificaciones de medicación?** | Verifique: 1) Que las notificaciones están activadas en los ajustes de su teléfono. 2) Que la app tiene permiso para enviar notificaciones. 3) En Android, que el modo "No molestar" no está activado. Si el problema persiste, reinstale la aplicación. |

> **Nota:** Se recomienda incluir capturas de pantalla de cada sección del manual como **Figuras A.11 a A.19**. El manual completo debe estar disponible como documento PDF descargable desde la propia aplicación.

---

## 13.5 Manual técnico

> **Nota:** Este apartado contiene un esquema estructurado del manual técnico completo. Se recomienda generar el manual técnico como documento separado para el equipo de desarrollo.

### 1. Documentación de arquitectura

#### 1.1 Arquitectura general

La aplicación sigue una arquitectura **Clean Architecture** combinada con el patrón **MVVM (Model-View-ViewModel)**, separando claramente las responsabilidades en capas:

```
┌─────────────────────────────────────────────────────────────┐
│                    PRESENTACIÓN (UI)                        │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐  ┌──────────┐   │
│  │  Widgets │  │  Screens │  │  Dialogs │  │  Themes  │   │
│  └──────────┘  └──────────┘  └──────────┘  └──────────┘   │
├─────────────────────────────────────────────────────────────┤
│                    VIEWMODELS                                │
│  ┌──────────────────────────────────────────────────────┐   │
│  │  Estado de la UI, lógica de presentación, navegación │   │
│  └──────────────────────────────────────────────────────┘   │
├─────────────────────────────────────────────────────────────┤
│                      DOMINIO                                 │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐  ┌──────────┐   │
│  │  Casos   │  │ Entidades│  │Interfaces│  │  Enums   │   │
│  │  de uso  │  │ (Models) │  │Repositorio│  │  Valor   │   │
│  └──────────┘  └──────────┘  └──────────┘  └──────────┘   │
├─────────────────────────────────────────────────────────────┤
│                        DATOS                                 │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐     │
│  │ Repositorios │  │ Data Sources │  │    DTOs      │     │
│  │ (implement.) │  │  (Firestore, │  │  (Modelos    │     │
│  │              │  │   Local, BLE)│  │   de red)    │     │
│  └──────────────┘  └──────────────┘  └──────────────┘     │
├─────────────────────────────────────────────────────────────┤
│                      CORE/UTILIDADES                         │
│  ┌──────────┐  ┌──────────┐  ┌──────────┐  ┌──────────┐   │
│  │Constantes│  │  Errores │  │Utilidades│  │Extensiones│  │
│  │ (strings,│  │(excepcio- │  │ (helpers,│  │  (exten- │  │
│  │  colores) │  │  nes)    │  │  mixins) │  │  siones) │  │
│  └──────────┘  └──────────┘  └──────────┘  └──────────┘   │
└─────────────────────────────────────────────────────────────┘
```

> **Nota:** Incluir diagrama de arquitectura detallado como **Figura A.20**.

#### 1.2 Principios de diseño aplicados

| Principio | Aplicación en el proyecto |
|-----------|---------------------------|
| **Separation of Concerns** | Cada capa tiene una responsabilidad única y bien definida |
| **Dependency Inversion** | El dominio no depende de la implementación de datos; usa interfaces abstractas |
| **Single Responsibility** | Cada clase tiene una única razón para cambiar |
| **Repository Pattern** | Los repositorios abstraen el origen de datos (Firestore, caché local, BLE) |
| **State Management** | Uso de Provider/Riverpod para gestionar el estado de forma reactiva |
| **Immutabilidad** | Las entidades del dominio y los estados de la UI son inmutables |

### 2. Estructura de carpetas del proyecto

```
cuidado_mayor_app/
├── android/                          # Código nativo Android
│   ├── app/
│   └── build.gradle
├── ios/                              # Código nativo iOS
│   ├── Runner/
│   └── Podfile
├── lib/
│   ├── main.dart                     # Punto de entrada de la aplicación
│   │
│   ├── core/                         # Utilidades compartidas
│   │   ├── constants/
│   │   │   ├── app_colors.dart       # Paleta de colores de la app
│   │   │   ├── app_strings.dart      # Textos y localización (es-ES)
│   │   │   ├── app_routes.dart       # Rutas de navegación
│   │   │   └── app_dimensions.dart   # Medidas y espaciados
│   │   ├── errors/
│   │   │   ├── exceptions.dart       # Excepciones personalizadas
│   │   │   └── failures.dart         # Clase Failure para Either
│   │   ├── utils/
│   │   │   ├── validators.dart       # Validadores de formularios
│   │   │   ├── date_formatter.dart   # Formato de fechas
│   │   │   ├── permission_handler.dart # Gestión de permisos
│   │   │   └── connectivity_checker.dart # Verificación de red
│   │   ├── theme/
│   │   │   ├── app_theme.dart        # ThemeData principal
│   │   │   ├── text_styles.dart      # Estilos de texto
│   │   │   ├── button_styles.dart    # Estilos de botones
│   │   │   └── accessibility_theme.dart # Tema de accesibilidad
│   │   └── services/
│   │       ├── firebase_initializer.dart
│   │       ├── notification_service.dart
│   │       └── analytics_service.dart
│   │
│   ├── data/                         # Capa de datos
│   │   ├── datasources/
│   │   │   ├── remote/
│   │   │   │   ├── auth_remote_datasource.dart
│   │   │   │   ├── paciente_remote_datasource.dart
│   │   │   │   ├── medicacion_remote_datasource.dart
│   │   │   │   ├── vitales_remote_datasource.dart
│   │   │   │   └── emergencia_remote_datasource.dart
│   │   │   └── local/
│   │   │       ├── prefs_local_datasource.dart
│   │   │       └── hive_local_datasource.dart
│   │   ├── models/                   # DTOs (Data Transfer Objects)
│   │   │   ├── usuario_model.dart
│   │   │   ├── paciente_model.dart
│   │   │   ├── medicacion_model.dart
│   │   │   ├── constante_vital_model.dart
│   │   │   └── emergencia_model.dart
│   │   └── repositories/             # Implementaciones de repositorios
│   │       ├── auth_repository_impl.dart
│   │       ├── paciente_repository_impl.dart
│   │       ├── medicacion_repository_impl.dart
│   │       ├── vitales_repository_impl.dart
│   │       └── emergencia_repository_impl.dart
│   │
│   ├── domain/                       # Capa de dominio
│   │   ├── entities/
│   │   │   ├── usuario.dart
│   │   │   ├── paciente.dart
│   │   │   ├── medicacion.dart
│   │   │   ├── constante_vital.dart
│   │   │   ├── emergencia.dart
│   │   │   └── recordatorio.dart
│   │   ├── repositories/             # Interfaces de repositorios
│   │   │   ├── auth_repository.dart
│   │   │   ├── paciente_repository.dart
│   │   │   ├── medicacion_repository.dart
│   │   │   ├── vitales_repository.dart
│   │   │   └── emergencia_repository.dart
│   │   └── usecases/
│   │       ├── auth/
│   │       │   ├── login_usecase.dart
│   │       │   ├── register_usecase.dart
│   │       │   └── logout_usecase.dart
│   │       ├── paciente/
│   │       │   ├── get_paciente_usecase.dart
│   │       │   ├── update_paciente_usecase.dart
│   │       │   └── link_cuidador_usecase.dart
│   │       ├── medicacion/
│   │       │   ├── get_medicaciones_usecase.dart
│   │       │   ├── schedule_medicacion_usecase.dart
│   │       │   └── confirm_toma_usecase.dart
│   │       ├── vitales/
│   │       │   ├── get_vitales_usecase.dart
│   │       │   ├── register_vital_usecase.dart
│   │       │   └── get_vitales_trend_usecase.dart
│   │       └── emergencia/
│   │           ├── activate_sos_usecase.dart
│   │           └── get_emergencias_usecase.dart
│   │
│   ├── presentation/                 # Capa de presentación
│   │   ├── providers/                # State management (Provider/Riverpod)
│   │   │   ├── auth_provider.dart
│   │   │   ├── paciente_provider.dart
│   │   │   ├── medicacion_provider.dart
│   │   │   ├── vitales_provider.dart
│   │   │   └── emergencia_provider.dart
│   │   ├── screens/
│   │   │   ├── auth/
│   │   │   │   ├── login_screen.dart
│   │   │   │   ├── register_screen.dart
│   │   │   │   ├── role_selection_screen.dart
│   │   │   │   └── forgot_password_screen.dart
│   │   │   ├── mayor/
│   │   │   │   ├── home_mayor_screen.dart
│   │   │   │   ├── medicacion_mayor_screen.dart
│   │   │   │   ├── constantes_mayor_screen.dart
│   │   │   │   ├── sos_emergency_screen.dart
│   │   │   │   └── videollamada_screen.dart
│   │   │   ├── cuidador/
│   │   │   │   ├── home_cuidador_screen.dart
│   │   │   │   ├── monitorizacion_screen.dart
│   │   │   │   ├── alertas_screen.dart
│   │   │   │   ├── medicacion_cuidador_screen.dart
│   │   │   │   └── informes_screen.dart
│   │   │   └── profesional/
│   │   │       ├── home_profesional_screen.dart
│   │   │       ├── pacientes_list_screen.dart
│   │       │   ├── detalle_paciente_screen.dart
│   │   │       ├── valoracion_geriatrica_screen.dart
│   │   │       └── videoconsulta_screen.dart
│   │   ├── widgets/
│   │   │   ├── common/
│   │   │   │   ├── custom_button.dart
│   │   │   │   ├── custom_text_field.dart
│   │   │   │   ├── custom_app_bar.dart
│   │   │   │   ├── loading_indicator.dart
│   │   │   │   └── error_widget.dart
│   │   │   ├── mayor/
│   │   │   │   ├── sos_button.dart
│   │   │   │   ├── medicacion_card.dart
│   │   │   │   └── vital_sign_tile.dart
│   │   │   └── cuidador/
│   │   │       ├── status_indicator.dart
│   │   │       ├── alert_card.dart
│   │   │       └── adherence_chart.dart
│   │   └── navigation/
│   │       ├── app_router.dart
│   │       └── route_guard.dart      # Protección de rutas por rol
│   │
│   └── di/                           # Inyección de dependencias
│       └── injection_container.dart  # Configuración de GetIt
│
├── test/                             # Tests
│   ├── unit/
│   │   ├── domain/
│   │   └── data/
│   ├── widget/
│   └── integration/
│
├── assets/                           # Recursos estáticos
│   ├── images/
│   ├── icons/
│   ├── fonts/
│   └── sounds/                       # Sonidos de notificación
│
├── firebase.json                     # Configuración Firebase
├── pubspec.yaml                      # Dependencias del proyecto
└── README.md
```

> **Nota:** Incluir diagrama de estructura de carpetas visual como **Figura A.21**.

### 3. Resumen de referencia API

**Tabla A.6: Endpoints principales de Cloud Functions**

| Endpoint | Método | Descripción | Rol autorizado |
|----------|--------|-------------|----------------|
| `/api/v1/auth/verify-token` | POST | Verifica y renueva el token de autenticación | Todos |
| `/api/v1/pacientes/{id}` | GET | Obtener datos de un paciente | Cuidador, Profesional |
| `/api/v1/pacientes/{id}/vitals` | GET | Historial de constantes vitales | Cuidador, Profesional, Mayor |
| `/api/v1/pacientes/{id}/vitals` | POST | Registrar nueva constante vital | Mayor |
| `/api/v1/pacientes/{id}/medication` | GET | Lista de medicación activa | Todos |
| `/api/v1/pacientes/{id}/medication` | POST | Programar nueva medicación | Cuidador, Profesional |
| `/api/v1/pacientes/{id}/medication/{mid}/confirm` | POST | Confirmar toma de medicamento | Mayor |
| `/api/v1/emergencies` | POST | Activar emergencia SOS | Mayor |
| `/api/v1/emergencies/{id}` | GET | Detalle de una emergencia | Cuidador, Profesional |
| `/api/v1/emergencies/{id}/resolve` | PUT | Resolver una emergencia | Cuidador, Profesional |
| `/api/v1/reports/monthly/{id}` | GET | Generar informe mensual | Cuidador |
| `/api/v1/assessments` | POST | Crear valoración geriátrica | Profesional |
| `/api/v1/assessments/{id}` | GET | Obtener valoración | Profesional |
| `/api/v1/videoconsultations` | POST | Programar videoconsulta | Profesional |
| `/api/v1/videoconsultations/{id}/token` | GET | Obtener token de videollamada | Todos |
| `/api/v1/notifications/fcm-token` | POST | Registrar token FCM | Todos |
| `/api/v1/ble/sync-data` | POST | Sincronizar datos de dispositivo BLE | Mayor |

### 4. Resumen del esquema de base de datos

**Colecciones principales de Firestore:**

**Tabla A.7: Esquema de colecciones de Firestore**

| Colección | Descripción | Campos principales | Subcolecciones |
|-----------|-------------|-------------------|----------------|
| `usuarios` | Perfiles de todos los usuarios | `uid`, `email`, `nombre`, `apellidos`, `telefono`, `rol`, `fechaCreacion`, `ultimaConexion`, `activo` | - |
| `pacientes` | Datos clínicos de personas mayores | `uidUsuario`, `nombre`, `fechaNacimiento`, `grupoSanguineo`, `alergias`, `patologias`, `medicoCabecera`, `cuidadoresAsignados[]` | `historial_medico` |
| `medicacion` | Pautas de medicación activa | `pacienteId`, `nombreMedicamento`, `dosis`, `frecuencia`, `horarios[]`, `viaAdministracion`, `fechaInicio`, `fechaFin`, `prescritoPor` | `registro_tomas` |
| `constantes_vitales` | Registro de mediciones | `pacienteId`, `tipo`, `valores{}`, `unidad`, `fuente` (manual/ble), `dispositivo`, `fecha`, `observaciones` | - |
| `emergencias` | Registro de eventos SOS | `pacienteId`, `tipo`, `fecha`, `ubicacion{lat, lng}`, `direccion`, `estado`, `contactosNotificados[]`, `fechaResolucion` | - |
| `recordatorios` | Alertas y recordatorios | `pacienteId`, `tipo`, `titulo`, `mensaje`, `fechaProgramada`, `estado`, `creadoPor` | - |
| `valoraciones` | Evaluaciones geriátricas | `pacienteId`, `tipo` (Barthel, MMSE, etc.), `puntuacion`, `resultado`, `observaciones`, `profesionalId`, `fecha` | - |
| `videollamadas` | Sesiones de videollamada | `participantes[]`, `tipo` (familiar/profesional), `fechaProgramada`, `estado`, `tokenSesion`, `notas` | - |
| `dispositivos_ble` | Dispositivos IoT vinculados | `pacienteId`, `nombre`, `tipo`, `macAddress`, `ultimaConexion`, `datos` | - |
| `alertas` | Notificaciones generadas | `pacienteId`, `tipo`, `nivel` (info, warning, critical), `mensaje`, `fecha`, `leida`, `leidaPor` | - |

> **Nota:** Incluir Diagrama Entidad-Relación (ER) completo como **Figura A.22**.

### 5. Resumen de guía de despliegue

#### 5.1 Requisitos previos

| Herramienta | Versión mínima | Propósito |
|-------------|----------------|-----------|
| Flutter SDK | 3.19+ | Framework de desarrollo |
| Dart SDK | 3.3+ | Lenguaje de programación |
| Firebase CLI | 13.0+ | Gestión de Firebase |
| Node.js | 20+ | Cloud Functions |
| Java JDK | 17+ | Compilación Android |
| Xcode | 15+ | Compilación iOS (solo macOS) |
| Android Studio | 2023+ | Emuladores y SDK Android |
| CocoaPods | 1.14+ | Dependencias iOS (solo macOS) |

#### 5.2 Despliegue de Firebase

```bash
# 1. Iniciar sesión en Firebase
firebase login

# 2. Inicializar proyecto (si no está hecho)
firebase init

# 3. Desplegar reglas de seguridad de Firestore
firebase deploy --only firestore:rules

# 4. Desplegar índices compuestos
firebase deploy --only firestore:indexes

# 5. Desplegar Cloud Functions
firebase deploy --only functions

# 6. Desplegar Cloud Storage rules
firebase deploy --only storage

# 7. Desplegar configuración de hosting (si aplica)
firebase deploy --only hosting

# Despliegue completo de todo
firebase deploy
```

#### 5.3 Compilación y publicación Android

```bash
# 1. Verificar configuración
flutter doctor

# 2. Obtener dependencias
flutter pub get

# 3. Ejecutar análisis de código
flutter analyze

# 4. Ejecutar tests
flutter test

# 5. Generar App Bundle (formato recomendado para Play Store)
flutter build appbundle --release

# El archivo se genera en: build/app/outputs/bundle/release/app-release.aab

# 6. Subir a Google Play Console manualmente o con Fastlane
# play-console -> Aplicación -> Producción -> Crear nueva versión
# Subir el archivo .aab, completar notas de versión y publicar
```

#### 5.4 Compilación y publicación iOS

```bash
# 1. Instalar dependencias de CocoaPods
cd ios && pod install && cd ..

# 2. Generar IPA
flutter build ios --release

# 3. Abrir en Xcode para distribución
open ios/Runner.xcworkspace

# 4. En Xcode: Product > Archive
# 5. Distribuir a App Store Connect
# 6. Enviar para revisión desde App Store Connect
```

#### 5.5 Variables de entorno

**Archivo `.env` (no incluir en el repositorio):**

```env
# Firebase
FIREBASE_API_KEY=AIza...
FIREBASE_APP_ID=1:123456789:web:abc123
FIREBASE_MESSAGING_SENDER_ID=123456789
FIREBASE_PROJECT_ID=cuidado-mayor-tfg

# APIs externas
OPENAI_API_KEY=sk-...          # Si se usa IA para asistente
TWILIO_ACCOUNT_SID=AC...        # Si se usa Twilio para videollamadas
GOOGLE_MAPS_API_KEY=AIza...     # Para mapas y geocoding

# Configuración
APP_ENVIRONMENT=production
LOG_LEVEL=info
ENABLE_ANALYTICS=true
```

### 6. Guía de resolución de problemas (Troubleshooting)

**Tabla A.8: Problemas comunes y soluciones**

| Problema | Causa probable | Solución |
|----------|----------------|----------|
| **La app se cierra al iniciar** | Firebase no inicializado correctamente | Verificar que `google-services.json` (Android) y `GoogleService-Info.plist` (iOS) están presentes y son correctos. Ejecutar `flutter clean && flutter pub get` |
| **Error de autenticación "token expirado"** | El token de Firebase ha caducado | El SDK renueva tokens automáticamente. Si persiste, forzar cierre de sesión y volver a iniciar. Verificar hora del dispositivo |
| **Las notificaciones no llegan** | Permisos no concedidos o canal no configurado | Verificar permisos en ajustes del dispositivo. En Android 13+, solicitar permiso POST_NOTIFICATIONS explícitamente. Comprobar que el canal de notificación está creado |
| **Error "PERMISSION_DENIED" en Firestore** | Reglas de seguridad bloquean el acceso | Verificar reglas de Firestore en Firebase Console. Comprobar que el usuario tiene el rol correcto y accede a recursos autorizados |
| **Dispositivo BLE no se encuentra** | Bluetooth apagado o dispositivo no compatible | Activar Bluetooth. Verificar que el dispositivo usa BLE 4.0+. En Android, activar ubicación (requerido para BLE). Reiniciar escaneo |
| **La ubicación en SOS no se actualiza** | Permisos de ubicación denegados | Solicitar permisos de ubicación en ajustes. Verificar que GPS está activado. En iOS, comprobar que el permiso es "Always" o "When In Use" |
| **La app va lenta al cargar datos** | Muchos datos sin paginar | Implementar paginación en consultas Firestore (limit + startAfter). Usar índices compuestos para consultas complejas |
| **Error de compilación en iOS** | Dependencias de CocoaPods desactualizadas | Ejecutar `cd ios && pod repo update && pod install`. Verificar versión mínima de iOS en Podfile |
| **Error "MULTIDEX" en Android** | Demasiados métodos en la app (límite 64K) | Añadir `multiDexEnabled true` en `android/app/build.gradle`. Usar R8/ProGuard para reducir tamaño |
| **Las videollamadas fallan** | Firewall o red restrictiva | Verificar conectividad UDP/TCP en puertos requeridos. Probar con otra red WiFi. Comprobar permisos de cámara y micrófono |
| **Datos no se sincronizan offline** | Caché local no configurada | Verificar que Firestore persistence está activada: `FirebaseFirestore.instance.settings = Settings(persistenceEnabled: true)` |
| **Error de firmado al generar APK/AAB** | Keystore mal configurado | Verificar configuración de `key.properties` en Android. Para debug, usar el keystore por defecto de Android Studio |
| **Los colores se ven diferentes en iOS/Android** | Tema no aplicado consistentemente | Usar `ThemeData` unificado. Verificar que los widgets usan `Theme.of(context)` y no colores hardcodeados |

> **Nota:** Para problemas no listados aquí, consultar los logs con `flutter run --verbose` o revisar la sección de issues del repositorio del proyecto.

---

## Índice de figuras y tablas de los anexos

### Figuras

| Referencia | Descripción | Sección |
|------------|-------------|---------|
| Figura A.1 | Configuración de App Check en Firebase Console | 13.1 |
| Figura A.2 | Diagrama de flujo de autenticación | 13.1 |
| Figura A.3 | Diagrama de secuencia de comunicación BLE | 13.1 |
| Figura A.4 | Diagrama Entidad-Relación (ER) de Firestore | 13.2 |
| Figura A.5 | Diagrama de Gantt visual del proyecto | 13.3 |
| Figura A.6 | Diagrama de hitos del proyecto | 13.3 |
| Figura A.7 | Captura de la app en Google Play Store | 13.4 |
| Figura A.8 | Pantalla de creación de cuenta | 13.4 |
| Figura A.9 | Pantalla de selección de rol | 13.4 |
| Figura A.10 | Pantalla de configuración inicial | 13.4 |
| Figura A.11 | Pantalla principal del perfil persona mayor | 13.4 |
| Figura A.12 | Notificación de recordatorio de medicación | 13.4 |
| Figura A.13 | Botón SOS y pantalla de confirmación | 13.4 |
| Figura A.14 | Formulario de registro de constantes vitales | 13.4 |
| Figura A.15 | Proceso de vinculación cuidador-familiar | 13.4 |
| Figura A.16 | Panel de monitorización del cuidador | 13.4 |
| Figura A.17 | Historial de medicación con gráfico de adherencia | 13.4 |
| Figura A.18 | Ejemplo de informe mensual exportado | 13.4 |
| Figura A.19 | Panel de pacientes del profesional sanitario | 13.4 |
| Figura A.20 | Diagrama de arquitectura Clean Architecture + MVVM | 13.5 |
| Figura A.21 | Diagrama visual de estructura de carpetas | 13.5 |
| Figura A.22 | Diagrama Entidad-Relación completo | 13.5 |

### Tablas

| Referencia | Descripción | Sección |
|------------|-------------|---------|
| Tabla A.1 | Matriz de trazabilidad de requisitos | 13.2 |
| Tabla A.2 | Diagrama de Gantt del proyecto | 13.3 |
| Tabla A.3 | Distribución de carga semanal por fase | 13.3 |
| Tabla A.4 | Hitos principales del proyecto | 13.3 |
| Tabla A.5 | Preguntas frecuentes (FAQ) | 13.4 |
| Tabla A.6 | Endpoints principales de Cloud Functions | 13.5 |
| Tabla A.7 | Esquema de colecciones de Firestore | 13.5 |
| Tabla A.8 | Problemas comunes y soluciones (Troubleshooting) | 13.5 |

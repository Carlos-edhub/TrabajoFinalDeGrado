# SECCIONES 5-7: TFG Aplicacion Multiplataforma de Cuidado de Personas Mayores

---

# SECCION 5: DISENO DEL PROYECTO

## 5.1 Diseno funcional

### 5.1.1 Casos de uso

**Actores del sistema:**
- **Usuario mayor**: Persona mayor que utiliza la aplicacion para gestionar su salud y bienestar.
- **Cuidador profesional**: Profesional sanitario o cuidador que monitoriza al usuario mayor.
- **Familiar/Tutor**: Miembro de la familia que supervisa remotamente al usuario mayor.
- **Administrador del sistema**: Gestiona la plataforma, usuarios y configuraciones globales.
- **Sistema de emergencias**: Servicio externo que recibe alertas de emergencia.

---

#### CU-01: Registro de usuario mayor
| Campo | Descripcion |
|-------|-------------|
| **Actor** | Usuario mayor / Familiar |
| **Descripcion** | El usuario crea una cuenta en la aplicacion proporcionando datos personales basicos |
| **Precondiciones** | El usuario dispone de un dispositivo compatible con la aplicacion |
| **Flujo principal** | 1. El usuario abre la aplicacion y selecciona Registrarse. 2. Introduce nombre, apellidos, fecha de nacimiento, DNI/NIE. 3. Introduce email y crea una contrasena segura. 4. Acepta los terminos y condiciones y la politica de privacidad. 5. Verifica su email mediante codigo enviado. 6. El sistema crea la cuenta y redirige al panel principal. |
| **Flujo alternativo** | 5a. Si no verifica en 24h, la cuenta se marca como pendiente y se reenvia el codigo |
| **Postcondiciones** | La cuenta queda activa y el usuario puede acceder a todas las funcionalidades |

#### CU-02: Inicio de sesion con autenticacion biometrica
| Campo | Descripcion |
|-------|-------------|
| **Actor** | Usuario mayor / Cuidador / Familiar |
| **Descripcion** | El usuario accede a la aplicacion utilizando huella dactilar o reconocimiento facial |
| **Precondiciones** | El usuario tiene una cuenta registrada y biometria configurada en su dispositivo |
| **Flujo principal** | 1. El usuario abre la aplicacion. 2. El sistema detecta autenticacion biometrica disponible. 3. Solicita huella dactilar o reconocimiento facial. 4. El usuario autentica correctamente. 5. El sistema accede a la cuenta y muestra el panel principal. |
| **Flujo alternativo** | 3a. Si la biometria falla 3 veces, se solicita email y contrasena como metodo alternativo |
| **Postcondiciones** | Sesion activa con token JWT valido por 24 horas |

#### CU-03: Gestion de medicacion - Anadir medicamento
| Campo | Descripcion |
|-------|-------------|
| **Actor** | Usuario mayor / Cuidador |
| **Descripcion** | Se anade un nuevo medicamento al plan de medicacion del usuario |
| **Precondiciones** | Usuario autenticado en la aplicacion |
| **Flujo principal** | 1. El usuario accede a la seccion Medicacion. 2. Pulsa Anadir medicamento. 3. Introduce nombre del medicamento, dosis, frecuencia y horario. 4. Opcionalmente escanea el codigo de barras del envase. 5. Configura las alertas de recordatorio. 6. Guarda el medicamento. 7. El sistema programa las notificaciones automaticas. |
| **Flujo alternativo** | 4a. Si el escaneo falla, el usuario introduce los datos manualmente |
| **Postcondiciones** | El medicamento queda registrado y las alertas programadas |

#### CU-04: Gestion de medicacion - Tomar medicamento
| Campo | Descripcion |
|-------|-------------|
| **Actor** | Usuario mayor |
| **Descripcion** | El usuario confirma que ha tomado su medicacion |
| **Precondiciones** | Existe al menos un medicamento programado; es la hora de la toma |
| **Flujo principal** | 1. El sistema envia una notificacion de recordatorio. 2. El usuario abre la notificacion. 3. Se muestra la pantalla con el medicamento, dosis e instrucciones. 4. El usuario pulsa He tomado mi medicacion. 5. Opcionalmente anade una nota. 6. El sistema registra la toma en el historial. |
| **Flujo alternativo** | 4a. El usuario pulsa Posponer 15 minutos y el sistema reprograma la alerta |
| **Postcondiciones** | La toma queda registrada con fecha, hora y estado |

#### CU-05: Gestion de citas medicas
| Campo | Descripcion |
|-------|-------------|
| **Actor** | Usuario mayor / Familiar / Cuidador |
| **Descripcion** | El usuario programa y gestiona sus citas medicas |
| **Precondiciones** | Usuario autenticado |
| **Flujo principal** | 1. El usuario accede a Citas medicas. 2. Pulsa Nueva cita. 3. Introduce especialidad, medico, centro, fecha y hora. 4. Anade notas y documentos relevantes. 5. Configura recordatorios (24h, 2h, 30min antes). 6. El sistema sincroniza con Google Calendar. 7. Guarda la cita. |
| **Flujo alternativo** | 6a. Si no hay conexion, se almacena localmente y sincroniza al recuperar conexion |
| **Postcondiciones** | La cita queda registrada y sincronizada con el calendario |

#### CU-06: Boton de emergencia SOS
| Campo | Descripcion |
|-------|-------------|
| **Actor** | Usuario mayor |
| **Descripcion** | El usuario activa una alerta de emergencia que notifica a contactos y servicios |
| **Precondiciones** | Usuario autenticado; contactos de emergencia configurados |
| **Flujo principal** | 1. El usuario pulsa el boton SOS (visible en pantalla principal). 2. El sistema muestra confirmacion de 3 segundos. 3. Si confirma o pasan 3 segundos: a) Envia SMS con ubicacion GPS a contactos de emergencia. b) Realiza llamada automatica al contacto principal. c) Envia alerta al servidor con ubicacion en tiempo real. d) Si procede, contacta con servicios de emergencia (112). |
| **Flujo alternativo** | 2a. El usuario cancela durante los 3 segundos; no se envia alerta |
| **Postcondiciones** | Se registra el incidente en el historial de emergencias |

#### CU-07: Monitorizacion de constantes vitales
| Campo | Descripcion |
|-------|-------------|
| **Actor** | Usuario mayor / Sistema |
| **Descripcion** | La aplicacion recopila datos de salud desde dispositivos BLE conectados |
| **Precondiciones** | Dispositivo BLE emparejado (tensiometro, glucometro, pulsioximetro) |
| **Flujo principal** | 1. El usuario acerca el dispositivo BLE al telefono. 2. La aplicacion detecta y conecta automaticamente. 3. El usuario realiza la medicion en el dispositivo. 4. Los datos se transmiten via BLE a la aplicacion. 5. La aplicacion muestra los valores y los almacena. 6. Si los valores superan umbrales seguros, genera alerta. |
| **Flujo alternativo** | 3a. Si el dispositivo no conecta, el usuario puede introducir valores manualmente |
| **Postcondiciones** | Los valores quedan registrados en el historial medico |

#### CU-08: Geolocalizacion del usuario mayor
| Campo | Descripcion |
|-------|-------------|
| **Actor** | Familiar / Cuidador |
| **Descripcion** | El familiar o cuidador consulta la ubicacion actual del usuario mayor |
| **Precondiciones** | El usuario mayor ha otorgado permiso de ubicacion; familiar vinculado |
| **Flujo principal** | 1. El familiar accede a la seccion Ubicacion. 2. Selecciona al usuario mayor a monitorizar. 3. El mapa muestra la ubicacion actual en tiempo real. 4. Se muestra el historial de ubicaciones recientes. 5. Se puede establecer geocercas (zonas seguras). |
| **Flujo alternativo** | 3a. Si la ubicacion no esta disponible, se muestra la ultima conocida con timestamp |
| **Postcondiciones** | Se registra la consulta de ubicacion en el log de auditoria |

#### CU-09: Configuracion de geocercas (zonas seguras)
| Campo | Descripcion |
|-------|-------------|
| **Actor** | Familiar / Cuidador |
| **Descripcion** | Se definen zonas geograficas seguras; se alerta si el usuario sale de ellas |
| **Precondiciones** | Familiar vinculado al usuario mayor; permisos de ubicacion activos |
| **Flujo principal** | 1. El familiar accede a Geocercas. 2. Pulsa Crear zona segura. 3. Selecciona un punto en el mapa o usa la ubicacion actual. 4. Define el radio de la zona (100m - 2km). 5. Asigna un nombre. 6. Configura las alertas de salida/entrada. 7. Guarda la geocerca. |
| **Flujo alternativo** | 6a. Se pueden configurar varias zonas con diferentes niveles de alerta |
| **Postcondiciones** | El sistema monitoriza activamente las geocercas en segundo plano |

#### CU-10: Chat entre familiar y cuidador
| Campo | Descripcion |
|-------|-------------|
| **Actor** | Familiar / Cuidador |
| **Descripcion** | Los miembros del equipo de cuidado se comunican mediante mensajeria interna |
| **Precondiciones** | Ambos usuarios autenticados y vinculados al mismo usuario mayor |
| **Flujo principal** | 1. El usuario accede a la seccion Chat. 2. Selecciona el contacto con quien comunicarse. 3. Escribe un mensaje y lo envia. 4. El destinatario recibe notificacion push. 5. Ambos pueden intercambiar mensajes en tiempo real. 6. Se pueden adjuntar fotos y documentos. |
| **Flujo alternativo** | 4a. Si el destinatario esta offline, el mensaje se entrega al reconectar |
| **Postcondiciones** | La conversacion queda cifrada y almacenada de forma segura |

#### CU-11: Gestion de perfil del usuario mayor
| Campo | Descripcion |
|-------|-------------|
| **Actor** | Usuario mayor / Familiar |
| **Descripcion** | Se editan los datos personales, medicos y de contacto del usuario |
| **Precondiciones** | Usuario autenticado |
| **Flujo principal** | 1. El usuario accede a Mi perfil. 2. Edita datos personales. 3. Actualiza informacion medica. 4. Configura contactos de emergencia. 5. Ajusta preferencias de accesibilidad. 6. Guarda los cambios. |
| **Postcondiciones** | Los datos actualizados se sincronizan con el servidor |

#### CU-12: Visualizacion del historial medico
| Campo | Descripcion |
|-------|-------------|
| **Actor** | Usuario mayor / Cuidador / Familiar |
| **Descripcion** | Se consulta el historial completo de medicacion, citas y constantes vitales |
| **Precondiciones** | Usuario autenticado con permisos adecuados |
| **Flujo principal** | 1. El usuario accede a Historial medico. 2. Selecciona el tipo de registro. 3. Filtra por rango de fechas. 4. Visualiza los registros en formato lista o grafico. 5. Puede exportar el historial en PDF. |
| **Postcondiciones** | Se registra la consulta en el log de auditoria |

#### CU-13: Gestion de documentos medicos
| Campo | Descripcion |
|-------|-------------|
| **Actor** | Usuario mayor / Familiar |
| **Descripcion** | Se almacenan y organizan documentos medicos digitales |
| **Precondiciones** | Usuario autenticado |
| **Flujo principal** | 1. El usuario accede a Documentos. 2. Pulsa Subir documento. 3. Selecciona la fuente (camara, galeria, archivos). 4. Asigna categoria. 5. Anade fecha y notas. 6. El sistema sube y almacena el documento cifrado. |
| **Postcondiciones** | El documento queda almacenado en la nube y disponible offline |

#### CU-14: Generacion de informes para el medico
| Campo | Descripcion |
|-------|-------------|
| **Actor** | Usuario mayor / Familiar |
| **Descripcion** | Se genera un informe resumido del estado de salud para compartir con el medico |
| **Precondiciones** | Existen datos suficientes en el historial |
| **Flujo principal** | 1. El usuario accede a Informes. 2. Selecciona el tipo de informe. 3. Configura el rango de fechas y secciones. 4. El sistema genera el informe en PDF. 5. El usuario puede revisar y compartir por email o WhatsApp. |
| **Postcondiciones** | El informe se genera y queda disponible para compartir |

#### CU-15: Panel de control del cuidador profesional
| Campo | Descripcion |
|-------|-------------|
| **Actor** | Cuidador profesional |
| **Descripcion** | El cuidador monitoriza a multiples usuarios mayores asignados |
| **Precondiciones** | Cuidador autenticado con usuarios asignados |
| **Flujo principal** | 1. El cuidador accede al panel principal. 2. Se muestra lista de usuarios con estado. 3. Selecciona un usuario para ver detalle. 4. Consulta medicacion, citas, constantes y alertas. 5. Puede anadir notas de seguimiento. 6. Envia recordatorios personalizados. |
| **Postcondiciones** | Las acciones del cuidador quedan registradas en el historial |

#### CU-16: Administracion del sistema
| Campo | Descripcion |
|-------|-------------|
| **Actor** | Administrador |
| **Descripcion** | El administrador gestiona usuarios, roles y configuraciones de la plataforma |
| **Precondiciones** | Administrador autenticado con rol admin |
| **Flujo principal** | 1. El admin accede al panel de administracion. 2. Gestiona usuarios. 3. Asigna roles y permisos. 4. Configura parametros del sistema. 5. Consulta logs de auditoria y metricas de uso. |
| **Postcondiciones** | Los cambios se aplican y se registran en el log de auditoria |

#### CU-17: Configuracion de recordatorios personalizados
| Campo | Descripcion |
|-------|-------------|
| **Actor** | Usuario mayor / Familiar |
| **Descripcion** | Se configuran recordatorios para actividades no medicas |
| **Precondiciones** | Usuario autenticado |
| **Flujo principal** | 1. El usuario accede a Recordatorios. 2. Pulsa Nuevo recordatorio. 3. Selecciona tipo. 4. Configura hora, frecuencia y mensaje. 5. Activa el recordatorio. |
| **Postcondiciones** | El recordatorio se programa y notifica segun la configuracion |

#### CU-18: Videollamada de emergencia
| Campo | Descripcion |
|-------|-------------|
| **Actor** | Usuario mayor / Familiar / Cuidador |
| **Descripcion** | Se inicia una videollamada entre el usuario mayor y su cuidador o familiar |
| **Precondiciones** | Conexion a internet; ambos usuarios en la app |
| **Flujo principal** | 1. El usuario accede a Videollamada. 2. Selecciona el contacto. 3. Inicia la videollamada. 4. El destinatario recibe la llamada con prioridad alta. 5. Se establece la conexion de video. 6. Durante la llamada se puede compartir ubicacion. |
| **Postcondiciones** | La llamada queda registrada con duracion y timestamp |

### 5.1.2 Historias de usuario

| ID | Historia de usuario | Criterios de aceptacion |
|----|---------------------|------------------------|
| HU-01 | Como usuario mayor, quiero registrarme con mis datos basicos para poder usar la aplicacion | El formulario pide nombre, email, fecha de nacimiento y contrasena. La contrasena debe tener al menos 8 caracteres con mayuscula, numero y simbolo. Se envia email de verificacion. Sin verificar no se puede acceder. |
| HU-02 | Como usuario mayor, quiero iniciar sesion con mi huella dactilar para no tener que recordar contrasenas | La app detecta si el dispositivo tiene biometria. Permite registrar huella tras el primer login con contrasena. Si falla 3 veces, pide contrasena. Funciona en Android e iOS. |
| HU-03 | Como usuario mayor, quiero recibir recordatorios de cuando tomar mi medicacion para no olvidar ninguna dosis | La notificacion se muestra a la hora configurada. Incluye nombre del medicamento y dosis. Permite confirmar, posponer 15 min o saltar. Si no responde en 30 min, notifica al familiar. |
| HU-04 | Como cuidador, quiero ver si el usuario mayor ha tomado su medicacion para saber si esta siguiendo el tratamiento | El panel muestra estado: tomado, pendiente, saltado. Se actualiza en tiempo real. Muestra historial de las ultimas 24h. |
| HU-05 | Como usuario mayor, quiero un boton de emergencia visible y facil de pulsar para pedir ayuda rapidamente | El boton SOS es grande y rojo en la pantalla principal. Tiene confirmacion de 3 segundos para evitar falsas alarmas. Envia SMS + llamada + ubicacion a contactos. |
| HU-06 | Como familiar, quiero recibir una alerta si mi padre sale de su zona segura para poder actuar rapidamente | Se configura un radio minimo de 100m. La alerta llega como push notification + SMS. Incluye ubicacion actual en el mapa. Se puede desactivar temporalmente. |
| HU-07 | Como usuario mayor, quiero que la aplicacion tenga textos grandes y alto contraste para poder leer bien | Opcion de tamano de texto: Normal, Grande, Muy grande. Modo alto contraste disponible. Botones con tamano minimo de 48x48dp. Compatible con TalkBack/VoiceOver. |
| HU-08 | Como familiar, quiero ver la ubicacion de mi madre en un mapa para saber donde esta en cada momento | El mapa muestra la ubicacion en tiempo real. Actualiza cada 5 minutos en segundo plano. Muestra la ultima ubicacion si esta offline. Historial de ubicaciones de las ultimas 24h. |
| HU-09 | Como usuario mayor, quiero anadir mis citas medicas para tener un recordatorio de cuando voy al medico | Formulario con campos: medico, especialidad, centro, fecha, hora, notas. Sincronizacion con Google Calendar. Recordatorios configurables: 24h, 2h, 30min. Posibilidad de adjuntar documentos. |
| HU-10 | Como cuidador profesional, quiero gestionar varios pacientes desde un solo panel para ser mas eficiente | Lista de pacientes con estado visible. Filtros por nombre, estado, prioridad. Acceso rapido al historial de cada paciente. Posibilidad de anadir notas de seguimiento. |
| HU-11 | Como usuario mayor, quiero registrar mis constantes vitales para llevar un control de mi salud | Conexion automatica por BLE con dispositivos compatibles. Entrada manual como alternativa. Visualizacion en graficos de tendencias. Alertas si los valores superan umbrales. |
| HU-12 | Como familiar, quiero chatear con el cuidador de mi padre para coordinar su atencion | Chat en tiempo real. Notificaciones push de nuevos mensajes. Posibilidad de enviar fotos y documentos. Historial de conversaciones. |
| HU-13 | Como usuario mayor, quiero almacenar mis recetas e informes medicos en la app para tenerlos siempre accesibles | Subir fotos desde camara o galeria. Categorizar por tipo. Busqueda por fecha o categoria. Acceso offline a documentos descargados. |
| HU-14 | Como usuario mayor, quiero generar un informe de mi salud para llevarselo a mi medico | Seleccion de rango de fechas. Incluye medicacion, citas, constantes vitales. Genera PDF con graficos. Compartir por email o WhatsApp. |
| HU-15 | Como usuario mayor, quiero recordatorios para beber agua y hacer ejercicio para mantener habitos saludables | Recordatorios configurables por tipo. Frecuencia personalizable. Mensajes motivacionales. Seguimiento de cumplimiento. |
| HU-16 | Como familiar, quiero poder iniciar una videollamada con mi madre para verla sin desplazarme | Videollamada desde la app con un toque. Calidad adaptativa segun conexion. Opcion de compartir pantalla. Funciona en WiFi y datos moviles. |
| HU-17 | Como usuario mayor, quiero que la aplicacion funcione sin internet para las funciones basicas | Consulta de medicacion offline. Historial medico cacheado. Las acciones se sincronizan al recuperar conexion. Boton SOS funciona sin conexion (SMS/llamada). |
| HU-18 | Como administrador, quiero gestionar los usuarios y sus roles para mantener la plataforma segura | CRUD de usuarios. Asignacion de roles. Activacion/desactivacion de cuentas. Logs de auditoria. |
| HU-19 | Como usuario mayor, quiero que mis datos medicos esten protegidos y cifrados | Cifrado AES-256 en reposo. TLS 1.3 en transito. Autenticacion biometrica obligatoria. Cumplimiento RGPD. |
| HU-20 | Como familiar, quiero recibir un resumen semanal del estado de mi padre | Resumen enviado por email cada lunes. Incluye medicacion tomada, citas, constantes vitales, incidentes. Graficos de tendencias. Enlace a la app para mas detalle. |
| HU-21 | Como usuario mayor, quiero escanear el codigo de barras de mi medicamento para anadirlo rapidamente | Escaner integrado en la app. Base de datos de medicamentos en Espana (AEMPS). Autocompleta nombre, dosis y posologia. Posibilidad de editar datos antes de guardar. |
| HU-22 | Como cuidador, quiero establecer rutinas diarias para los usuarios a mi cargo | Crear rutinas con multiples acciones. Horarios personalizables. Asignar a uno o varios usuarios. Plantillas predefinidas. |

### 5.1.3 Diagramas funcionales

**Diagramas de casos de uso (UML):** Se recomienda crear un diagrama de casos de uso con PlantUML o Draw.io que muestre los actores principales (Usuario Mayor, Cuidador, Familiar, Admin) como stick figures y los casos de uso como ovalos, con las relaciones de asociacion, inclusion y extension.

`plantuml
@startuml
left to right direction
skinparam packageStyle rectangle

actor " Usuario Mayor\ as UM
actor \Cuidador\ as C
actor \Familiar\ as F
actor \Admin\ as A

rectangle \Sistema CuidadoMayor\ {
 usecase \Registro\ as UC1
 usecase \Login Biometrico\ as UC2
 usecase \Gestion Medicacion\ as UC3
 usecase \Boton SOS\ as UC4
 usecase \Monitorizacion Vitals\ as UC5
 usecase \Geolocalizacion\ as UC6
 usecase \Chat\ as UC7
 usecase \Citas Medicas\ as UC8
 usecase \Documentos Medicos\ as UC9
 usecase \Informes Salud\ as UC10
 usecase \Panel Cuidador\ as UC11
 usecase \Admin Sistema\ as UC12
}

UM --> UC1
UM --> UC2
UM --> UC3
UM --> UC4
UM --> UC5
UM --> UC8
UM --> UC9
UM --> UC10

C --> UC2
C --> UC3
C --> UC5
C --> UC7
C --> UC11

F --> UC2
F --> UC6
F --> UC7
F --> UC8
F --> UC10

A --> UC12
@enduml
`

**Diagramas de actividad:** Para cada caso de uso critico (SOS, medicacion, registro) se debe crear un diagrama de actividad que muestre el flujo de decisiones, bifurcaciones y acciones paralelas. Herramienta recomendada: Draw.io o Lucidchart.

**Diagramas de estado:** Para entidades como Medicacion (programada -> notificada -> tomada/pendiente/saltada) y Alerta (activa -> notificada -> atendida/resuelta).

**Sugerencia de infografia:** Crear un diagrama de flujo del ciclo de vida completo de una alerta SOS, desde la pulsacion del boton hasta la resolucion, incluyendo todas las notificaciones paralelas y los estados intermedios.

## 5.2 Diseno tecnico

### 5.2.1 Arquitectura del sistema

La aplicacion sigue una arquitectura **MVVM (Model-View-ViewModel)** combinada con principios de **Clean Architecture**, estructurada en tres capas principales:

`
+-------------------------------------------------+
|                  PRESENTACION                    |
|  +-------------+  +-------------+  +----------+  |
|  |   Widgets   |  |  Pantallas  |  |  Temas   |  |
|  |  (Flutter)  |  |  (Screens)  |  |  Estilo  |  |
|  +-------------+  +-------------+  +----------+  |
|         |                |                      |
|  +------v----------------v------+               |
|  |        ViewModels            |               |
|  |  (State Management)          |               |
|  |  Riverpod / Provider         |               |
|  +--------------+---------------+               |
+-----------------|-------------------------------+
                  |
+-----------------v-------------------------------+
|                   DOMINIO                        |
|  +-------------+  +-------------+  +----------+  |
|  |  Entidades  |  |  Casos de   |  |Repositorio| |
|  |   (Models)  |  |    Uso      |  |Interfaces | |
|  +-------------+  +-------------+  +-----+----+  |
+------------------------------------------|------+
                                           |
+------------------------------------------v------+
|                    DATOS                         |
|  +-------------+  +-------------+  +----------+  |
|  |  Firebase   |  |   SQLite    |  |  APIs    |  |
|  |  Firestore  |  |  (sqflite)  |  |Externas  |  |
|  +-------------+  +-------------+  +----------+  |
|  +-------------+  +-------------+               |
|  |  Secure     |  |  Shared     |               |
|  |  Storage    |  |Preferences |               |
|  +-------------+  +-------------+               |
+-------------------------------------------------+
`

**Capa de Presentacion:**
- Widgets Flutter reutilizables para componentes accesibles (botones grandes, textos ampliables)
- Pantallas (Screens) que consumen ViewModels
- Gestion de estado con Riverpod (recomendado) o Provider
- Temas configurables para accesibilidad

**Capa de Dominio:**
- Entidades puras (sin dependencias de framework)
- Casos de uso (UseCases) que encapsulan la logica de negocio
- Interfaces de repositorio que definen contratos de datos

**Capa de Datos:**
- Implementaciones concretas de repositorios
- Firebase Firestore para datos en la nube
- SQLite (sqflite/drift) para almacenamiento local offline
- flutter_secure_storage para datos sensibles
- APIs externas (Google Calendar, BLE, FCM)

**Patron de inyeccion de dependencias:**
`dart
// Ejemplo con Riverpod
@riverpod
FirebaseAuthRepository authRepository(AuthRepositoryRef ref) {
  return FirebaseAuthRepository(FirebaseAuth.instance);
}

@riverpod
MedicationRepository medicationRepository(MedicationRepositoryRef ref) {
  return MedicationRepository(
    firestore: FirebaseFirestore.instance,
    localDb: ref.watch(localDatabaseProvider),
  );
}

@riverpod
Stream<List<Medication>> userMedications(UserMedicationsRef ref, String userId) {
  return ref.watch(medicationRepositoryProvider).streamMedications(userId);
}
`

### 5.2.2 Diagrama de clases

**Entidades principales y sus relaciones:**

`
+----------------------+       +----------------------+
|       User           |       |      Profile         |
|----------------------| 1   1 |----------------------|
| - id: String         |<----->| - fullName: String   |
| - email: String      |       | - dateOfBirth: Date  |
| - phone: String      |       | - bloodType: String  |
| - role: UserRole     |       | - allergies: List    |
| - createdAt: Date    |       | - conditions: List   |
| - isActive: bool     |       | - photoUrl: String   |
|----------------------|       | - address: Address   |
| + authenticate()     |       +----------------------+
| + logout()           |
| + updateProfile()    |       +----------------------+
+----------+-----------+       |    Medication        |
           |                   |----------------------|
           | 1               * | - id: String         |
           v                   | - name: String       |
+----------------------+       | - dosage: String     |
|  CareRelationship    |       | - frequency: Enum    |
|----------------------|       | - schedule: List     |
| - id: String         |       | - startDate: Date    |
| - elderId: String    |       | - endDate: Date?     |
| - caregiverId: String|       | - notes: String      |
| - familyMemberId: S? |       | - isActive: bool     |
| - permissions: Set   |       | - barcode: String?   |
| - createdAt: Date    |       |----------------------|
+----------------------+       | + take()             |
                               | + skip()             |
                               | + snooze()           |
                               +----------+-----------+
                                          |
                                          | 1       *
                                          v
+----------------------+       +----------------------+
|    Appointment       |       |  MedicationLog       |
|----------------------|       |----------------------|
| - id: String         |       | - id: String         |
| - userId: String     |       | - medicationId: Str  |
| - doctor: String     |       | - userId: String     |
| - specialty: String  |       | - scheduledAt: Date  |
| - center: String     |       | - takenAt: Date?     |
| - date: DateTime     |       | - status: LogStatus  |
| - notes: String      |       | - notes: String?     |
| - reminderMinutes:[] |       +----------------------+
| - calendarEventId: S?|
|----------------------|       +----------------------+
| + reschedule()       |       |    VitalSign         |
| + cancel()           |       |----------------------|
+----------------------+       | - id: String         |
                               | - userId: String     |
+----------------------+       | - type: VitalType    |
|       Alert          |       | - value: double      |
|----------------------|       | - unit: String       |
| - id: String         |       | - recordedAt: Date   |
| - userId: String     |       | - deviceId: String?  |
| - type: AlertType    |       | - isManual: bool     |
| - severity: Severity |       +----------------------+
| - message: String    |
| - location: Geo?     |       +----------------------+
| - status: AlertStatus|       |     Document         |
| - createdAt: Date    |       |----------------------|
| - resolvedAt: Date?  |       | - id: String         |
| - resolvedBy: String?|       | - userId: String     |
|----------------------|       | - category: DocType  |
| + trigger()          |       | - fileUrl: String    |
| + resolve()          |       | - fileName: String   |
+----------------------+       | - uploadedAt: Date   |
                               | - ocrText: String?   |
+----------------------+       +----------------------+
|   EmergencyContact   |
|----------------------|       +----------------------+
| - id: String         |       |      Message         |
| - userId: String     |       |----------------------|
| - name: String       |       | - id: String         |
| - phone: String      |       | - senderId: String   |
| - relationship: Str  |       | - receiverId: String |
| - priority: int      |       | - content: String    |
| - isPrimary: bool    |       | - timestamp: Date    |
+----------------------+       | - isRead: bool       |
                               | - attachmentUrl: S?  |
+----------------------+       | - type: MsgType      |
|    Geofence          |       +----------------------+
|----------------------|
| - id: String         |       +----------------------+
| - userId: String     |       |     Routine          |
| - name: String       |       |----------------------|
| - latitude: double   |       | - id: String         |
| - longitude: double  |       | - userId: String     |
| - radiusMeters: int  |       | - name: String       |
| - alertOnExit: bool  |       | - items: List        |
| - alertOnEnter: bool |       | - schedule: Time     |
| - isActive: bool     |       | - daysOfWeek: List   |
+----------------------+       | - isActive: bool     |
                               +----------------------+
`

### 5.2.3 Diagramas de secuencia

**Secuencia 1: Registro de usuario con verificacion**
`
Usuario          App            AuthService        Firestore
  |               |                 |                 |
  | 1. Rellenar   |                 |                 |
  |   formulario  |                 |                 |
  |--------------->|                 |                 |
  |               | 2. createUser() |                 |
  |               |---------------->|                 |
  |               |                 | 3. Crear doc    |
  |               |                 |---------------->|
  |               |                 | 4. Enviar email |
  |               |                 |   verificacion  |
  |               |                 |<----------------|
  |               | 5. Resultado    |                 |
  |               |<----------------|                 |
  | 6. Confirmar  |                 |                 |
  |   verificacion|                 |                 |
  |--------------->|                 |                 |
  |               | 7. verifyEmail()|                 |
  |               |---------------->|---------------->|
  |               | 8. Cuenta activa|                 |
  |               |<----------------|                 |
  | 9. Redirigir  |                 |                 |
  |   al dashboard|                 |                 |
  |<---------------|                 |                 |
`

**Secuencia 2: Toma de medicacion con notificacion**
`
Sistema         FCM            App           User        MedicationRepo
  |              |              |              |              |
  | 1. Trigger   |              |              |              |
  |   alert      |              |              |              |
  |------------->|              |              |              |
  |              | 2. Push notif|              |              |
  |              |------------->|              |              |
  |              |              | 3. Mostrar   |              |
  |              |              |   notificacion|             |
  |              |              |-------------->|              |
  |              |              | 4. Confirmar |              |
  |              |              |   toma       |              |
  |              |              |<--------------|              |
  |              |              | 5. logTake() |              |
  |              |              |----------------------------->|
  |              |              |              | 6. Actualizar |
  |              |              |              |   historial   |
  |              |              |              |<--------------|
  |              |              | 7. Feedback  |              |
  |              |              |   visual     |              |
  |              |              |<--------------|              |
`

**Secuencia 3: Activacion del boton SOS**
`
Usuario        App          SOSHandler    FCM       Contacts    GPS
  |             |             |            |           |         |
  | 1. Pulsar   |             |            |           |         |
  |   boton SOS |             |            |           |         |
  |------------>|             |            |           |         |
  |             | 2. Timer 3s |            |           |         |
  |             | (cancelable)|            |           |         |
  | 3. Confirmar|             |            |           |         |
  |   / Timeout |             |            |           |         |
  |------------>|             |            |           |         |
  |             | 3. activateSOS()         |           |         |
  |             |------------>|            |           |         |
  |             |             | 4. getLocation()       |         |
  |             |             |----------------------->|         |
  |             |             | 5. coordenadas          |         |
  |             |             |<-----------------------|         |
  |             |             | 6. sendSMS()            |         |
  |             |             |------------------------>|         |
  |             |             | 7. push alert()         |         |
  |             |             |----------->|           |         |
  |             |             | 8. call()  |           |         |
  |             |             |------------------------>|         |
  |             | 9. Confirmacion        |           |         |
  |             |   enviada   |            |           |         |
  |             |<------------|            |           |         |
  |10. Mensaje  |             |            |           |         |
  |   Ayuda en  |             |            |           |         |
  |   camino    |             |            |           |         |
  |<------------|             |            |           |         |
`

**Secuencia 4: Sincronizacion de datos offline-online**
`
App         SyncManager   SQLite    Firestore   ConflictResolver
 |              |            |          |             |
 | 1. Detectar  |            |          |             |
 |   conexion  |            |          |             |
 |------------->|            |          |             |
 |              | 2. getPending()       |             |
 |              |------------>|         |             |
 |              | 3. operaciones        |             |
 |              |   pendientes |         |             |
 |              |<------------|         |             |
 |              | 4. sync each |         |             |
 |              |----------------------->|             |
 |              | 5. resultado |         |             |
 |              |<-----------------------|             |
 |              | 6. check conflicts      |             |
 |              |------------------------------------>|
 |              | 7. resolve strategy     |             |
 |              |<------------------------------------|
 |              | 8. mark synced          |             |
 |              |------------>|           |             |
 | 9. Sync      |            |           |             |
 |   completado |            |           |             |
 |<-------------|            |           |             |
`

**Secuencia 5: Consulta de ubicacion en tiempo real**
`
Familiar     App       LocationService   Firestore   ElderDevice
  |           |              |              |            |
  | 1. Abrir  |              |              |            |
  |   mapa    |              |              |            |
  |---------->|              |              |            |
  |           | 2. subscribe |              |            |
  |           |   location   |              |            |
  |           |---------------------------->            |
  |           |              | 3. start GPS |            |
  |           |              |   tracking   |            |
  |           |              |-------------------------->|
  |           |              |              |            | 4. coords
  |           |              |              |            |<----------
  |           |              | 5. update location        |
  |           |              |------------->|            |
  |           | 6. stream    |              |            |
  |           |   location   |              |            |
  |           |<-------------|              |            |
  | 7. Mostrar|              |              |            |
  |   en mapa |              |              |            |
  |<----------|              |              |            |
`

**Secuencia 6: Autenticacion biometrica con fallback**
`
Usuario      App       BiometricService   Keychain/Keystore   AuthBackend
  |           |              |                    |                |
  | 1. Abrir  |              |                    |                |
  |   app     |              |                    |                |
  |---------->|              |                    |                |
  |           | 2. canAuth() |                    |                |
  |           |------------->|                    |                |
  |           | 3. available |                    |                |
  |           |<-------------|                    |                |
  |           | 4. authenticate()                 |                |
  |           |------------->|                    |                |
  | 5. Huella |              |                    |                |
  |   / Face  |              |                    |                |
  |---------->|              |                    |                |
  |           | 6. success   |                    |                |
  |           |<-------------|                    |                |
  |           | 7. getToken()|                    |                |
  |           |--------------|--------------------|                |
  |           | 8. token     |                    |                |
  |           |<-------------|--------------------|                |
  |           | 9. validateToken()                |                |
  |           |----------------------------------->|                |
  |           |10. valid      |                    |                |
  |           |<-----------------------------------|                |
  |11. Acceso |              |                    |                |
  |   concedido|             |                    |                |
  |<----------|              |                    |                |
`

**Secuencia 7: Envio de mensaje en el chat**
`
Sender       App       ChatService     Firestore      FCM        Receiver
  |           |            |               |            |            |
  | 1. Escribir|           |               |            |            |
  |   mensaje |           |               |            |            |
  |---------->|           |               |            |            |
  |           | 2. encrypt()|              |            |            |
  |           |------------>|              |            |            |
  |           | 3. send()   |              |            |            |
  |           |------------->|------------>|            |            |
  |           |             | 4. save msg  |            |            |
  |           |             |------------->|            |            |
  |           |             |              | 5. notify  |            |
  |           |             |              |----------->|            |
  |           |             |              |            | 6. Push    |
  |           |             |              |            |----------->|
  |           |             |              |            |            | 7. Leer
  |           |             |              |            |            |<--------
  |           | 8. update   |              |            |            |
  |           |   en vivo   |              |            |            |
  |           |<------------|--------------|            |            |
  | 9. Ver    |             |              |            |            |
  |   enviado |             |              |            |            |
  |<----------|             |              |            |            |
`

**Secuencia 8: Lectura de constante vital por BLE**
`
Usuario      App        BLEManager    Tensiometro   VitalRepo
  |           |            |              |            |
  | 1. Iniciar|            |              |            |
  |   medicion|            |              |            |
  |---------->|            |              |            |
  |           | 2. scan()  |              |            |
  |           |----------->|              |            |
  |           | 3. device  |              |            |
  |           |   found    |              |            |
  |           |<-----------|              |            |
  |           | 4. connect()|             |            |
  |           |------------>|            |            |
  |           | 5. connected|             |            |
  |           |<------------|             |            |
  | 6. Medir  |            |              |            |
  |----------|------------|------------->|            |
  |           |            | 7. sendData  |            |
  |           |            |<-------------|            |
  |           | 8. parse   |              |            |
  |           |   reading  |              |            |
  |           |<------------|              |            |
  |           | 9. saveVital()            |            |
  |           |--------------------------->|            |
  |           |10. mostrar |              |            |
  |           |   resultado |             |            |
  |           |------------>|              |            |
  |11. Ver    |            |              |            |
  |   valores |            |              |            |
  |<----------|            |              |            |
`

### 5.2.4 Modelo entidad-relacion

`
+--------------+       +---------------------+       +------------------+
|    USERS     |       |  CARE_RELATIONSHIPS |       |     USERS        |
|--------------| 1   * |---------------------| *   1 |  (caregiver)     |
| user_id (PK) |<----->| relationship_id(PK) |<----->| user_id (PK)     |
| email        |       | elder_id (FK)       |       +------------------+
| phone        |       | caregiver_id (FK)   |
| password_hash|       | family_member(FK)   |
| role         |       | permissions         |
| created_at   |       | created_at          |
| is_active    |       +---------------------+
+------+-------+
       | 1
       |
       | *
+------v-------+ 1    * +-----------------+ *    1 +-----------------+
|   PROFILES   |<----->|   MEDICATIONS   |<----->|  MEDICATION_LOGS|
|--------------|       |-----------------|       |-----------------|
| profile_id   |       | medication_id   |       | log_id (PK)     |
| user_id(FK)  |       | user_id (FK)    |       | medication_id   |
| full_name    |       | name            |       | user_id (FK)    |
| date_of_birth|       | dosage          |       | scheduled_at    |
| blood_type   |       | frequency       |       | taken_at        |
| allergies    |       | schedule        |       | status          |
| conditions   |       | start_date      |       | notes           |
| photo_url    |       | end_date        |       | created_at      |
| address      |       | is_active       |       +-----------------+
| created_at   |       | barcode         |
+------+-------+       +-----------------+
       | 1
       |
       | *
+------v-------+ 1    * +-----------------+ 1    * +-----------------+
| APPOINTMENTS |       |   VITAL_SIGNS   |       |    ALERTS       |
|--------------|       |-----------------|       |-----------------|
| appt_id (PK) |       | vital_id (PK)   |       | alert_id (PK)   |
| user_id (FK) |       | user_id (FK)    |       | user_id (FK)    |
| doctor       |       | type            |       | type            |
| specialty    |       | value           |       | severity        |
| center       |       | unit            |       | message         |
| date_time    |       | recorded_at     |       | location        |
| notes        |       | device_id       |       | status          |
| reminder_cfg |       | is_manual       |       | created_at      |
| calendar_id  |       +-----------------+       | resolved_at     |
| created_at   |                                 | resolved_by     |
+--------------+                                 +-----------------+

+------------------+     +-----------------+     +-----------------+
|EMERGENCY_CONTACTS|     |    DOCUMENTS    |     |    MESSAGES     |
|------------------|     |-----------------|     |-----------------|
| contact_id (PK)  |     | doc_id (PK)     |     | msg_id (PK)     |
| user_id (FK)     |     | user_id (FK)    |     | sender_id (FK)  |
| name             |     | category        |     | receiver_id(FK) |
| phone            |     | file_url        |     | content         |
| relationship     |     | file_name       |     | timestamp       |
| priority         |     | uploaded_at     |     | is_read         |
| is_primary       |     | ocr_text        |     | attachment_url  |
+------------------+     +-----------------+     +-----------------+

+------------------+     +-----------------+     +-----------------+
|   GEOFENCES      |     |    ROUTINES     |     |   AUDIT_LOGS    |
|------------------|     |-----------------|     |-----------------|
| geofence_id (PK) |     | routine_id(PK)  |     | log_id (PK)     |
| user_id (FK)     |     | user_id (FK)    |     | user_id (FK)    |
| name             |     | name            |     | action          |
| latitude         |     | schedule        |     | entity_type     |
| longitude        |     | items_json      |     | entity_id       |
| radius_meters    |     | days_of_week    |     | timestamp       |
| alert_on_exit    |     | is_active       |     | details_json    |
| alert_on_enter   |     | created_at      |     | ip_address      |
| is_active        |     +-----------------+     | device_info     |
+------------------+                             +-----------------+
`

### 5.2.5 Diseno de la base de datos

#### Firebase Firestore (Colecciones principales)

`
users/
  {userId}/
    email: string
    phone: string
    role: elder | caregiver | family | admin
    createdAt: timestamp
    isActive: boolean
    lastLoginAt: timestamp
    fcmTokens: array<string>

profiles/
  {userId}/
    fullName: string
    dateOfBirth: timestamp
    bloodType: string
    allergies: array<string>
    conditions: array<string>
    photoUrl: string
    address: { street, city, province, postalCode, country, coordinates }
    accessibilitySettings: { fontSize, highContrast, voiceGuidance }

medications/
  {medicationId}/
    userId: string (reference)
    name: string
    dosage: string
    frequency: once_daily | twice_daily | three_daily | every_8h | as_needed
    schedule: [{ time: 08:00, days: [0,1,2,3,4,5,6] }]
    startDate: timestamp
    endDate: timestamp | null
    notes: string
    isActive: boolean
    barcode: string | null

medication_logs/
  {logId}/
    medicationId: string (reference)
    userId: string (reference)
    scheduledAt: timestamp
    takenAt: timestamp | null
    status: taken | skipped | snoozed | pending
    notes: string | null

appointments/
  {appointmentId}/
    userId: string (reference)
    doctor: string
    specialty: string
    center: string
    dateTime: timestamp
    notes: string
    reminderMinutes: array<number>
    status: scheduled | completed | cancelled

vital_signs/
  {vitalId}/
    userId: string (reference)
    type: blood_pressure | glucose | heart_rate | oxygen | temperature | weight
    value: number
    unit: string
    systolic: number | null
    diastolic: number | null
    recordedAt: timestamp
    deviceId: string | null
    isManual: boolean

alerts/
  {alertId}/
    userId: string (reference)
    type: sos | medication_missed | geofence_exit | vital_anomaly | fall_detected
    severity: low | medium | high | critical
    message: string
    location: GeoPoint | null
    status: active | notified | acknowledged | resolved | false_alarm
    createdAt: timestamp
    resolvedAt: timestamp | null

emergency_contacts/
  {contactId}/
    userId: string (reference)
    name: string
    phone: string
    relationship: string
    priority: number
    isPrimary: boolean

geofences/
  {geofenceId}/
    userId: string (reference)
    name: string
    center: GeoPoint
    radiusMeters: number
    alertOnExit: boolean
    alertOnEnter: boolean
    isActive: boolean

documents/
  {documentId}/
    userId: string (reference)
    category: prescription | report | lab_results | insurance | id | other
    fileUrl: string  (Firebase Storage)
    fileName: string
    uploadedAt: timestamp
    ocrText: string | null

messages/
  {conversationId}/
    participants: array<string>
    lastMessage: string
    lastMessageAt: timestamp
  messages/{messageId}/
    senderId: string
    content: string (cifrado)
    timestamp: timestamp
    isRead: boolean
    attachmentUrl: string | null
    type: text | image | document | system

care_relationships/
  {relationshipId}/
    elderId: string (reference)
    caregiverId: string (reference) | null
    familyMemberId: string (reference) | null
    permissions: array<string>
    status: pending | active | suspended

audit_logs/
  {logId}/
    userId: string
    action: string
    entityType: string
    entityId: string
    timestamp: timestamp
    details: map
`

#### SQLite (Almacenamiento local - Offline)

`sql
CREATE TABLE medications_local (
    id TEXT PRIMARY KEY,
    user_id TEXT NOT NULL,
    name TEXT NOT NULL,
    dosage TEXT NOT NULL,
    frequency TEXT NOT NULL,
    schedule_json TEXT NOT NULL,
    start_date TEXT NOT NULL,
    end_date TEXT,
    notes TEXT,
    is_active INTEGER DEFAULT 1,
    barcode TEXT,
    created_at TEXT NOT NULL,
    updated_at TEXT NOT NULL,
    sync_status TEXT DEFAULT pending,
    FOREIGN KEY (user_id) REFERENCES users_local(id)
);

CREATE TABLE medication_logs_local (
    id TEXT PRIMARY KEY,
    medication_id TEXT NOT NULL,
    user_id TEXT NOT NULL,
    scheduled_at TEXT NOT NULL,
    taken_at TEXT,
    status TEXT NOT NULL,
    notes TEXT,
    created_at TEXT NOT NULL,
    sync_status TEXT DEFAULT pending,
    FOREIGN KEY (medication_id) REFERENCES medications_local(id)
);

CREATE TABLE vital_signs_local (
    id TEXT PRIMARY KEY,
    user_id TEXT NOT NULL,
    type TEXT NOT NULL,
    value REAL NOT NULL,
    systolic REAL,
    diastolic REAL,
    unit TEXT NOT NULL,
    recorded_at TEXT NOT NULL,
    device_id TEXT,
    is_manual INTEGER DEFAULT 0,
    sync_status TEXT DEFAULT pending,
    FOREIGN KEY (user_id) REFERENCES users_local(id)
);

CREATE TABLE appointments_local (
    id TEXT PRIMARY KEY,
    user_id TEXT NOT NULL,
    doctor TEXT NOT NULL,
    specialty TEXT,
    center TEXT,
    date_time TEXT NOT NULL,
    notes TEXT,
    reminder_config TEXT,
    status TEXT DEFAULT scheduled,
    created_at TEXT NOT NULL,
    sync_status TEXT DEFAULT pending,
    FOREIGN KEY (user_id) REFERENCES users_local(id)
);

CREATE TABLE alerts_local (
    id TEXT PRIMARY KEY,
    user_id TEXT NOT NULL,
    type TEXT NOT NULL,
    severity TEXT NOT NULL,
    message TEXT NOT NULL,
    latitude REAL,
    longitude REAL,
    status TEXT DEFAULT active,
    created_at TEXT NOT NULL,
    resolved_at TEXT,
    sync_status TEXT DEFAULT pending,
    FOREIGN KEY (user_id) REFERENCES users_local(id)
);

CREATE TABLE users_local (
    id TEXT PRIMARY KEY,
    email TEXT NOT NULL UNIQUE,
    phone TEXT,
    role TEXT NOT NULL,
    full_name TEXT,
    date_of_birth TEXT,
    blood_type TEXT,
    allergies_json TEXT,
    conditions_json TEXT,
    photo_url TEXT,
    address_json TEXT,
    is_active INTEGER DEFAULT 1,
    last_sync_at TEXT,
    created_at TEXT NOT NULL
);

CREATE TABLE emergency_contacts_local (
    id TEXT PRIMARY KEY,
    user_id TEXT NOT NULL,
    name TEXT NOT NULL,
    phone TEXT NOT NULL,
    relationship TEXT,
    priority INTEGER DEFAULT 0,
    is_primary INTEGER DEFAULT 0,
    FOREIGN KEY (user_id) REFERENCES users_local(id)
);

-- Indices para optimizar consultas frecuentes
CREATE INDEX idx_medications_user ON medications_local(user_id, is_active);
CREATE INDEX idx_med_logs_medication ON medication_logs_local(medication_id, scheduled_at);
CREATE INDEX idx_vitals_user_type ON vital_signs_local(user_id, type, recorded_at);
CREATE INDEX idx_appointments_user ON appointments_local(user_id, date_time);
CREATE INDEX idx_alerts_user_status ON alerts_local(user_id, status);
`

### 5.2.6 Flujos de interaccion

**Flujo 1: Primer uso de la aplicacion (Onboarding)**
`
Pantalla de bienvenida -> Seleccion de rol (mayor/familiar/cuidador)
  -> Registro con email -> Verificacion email
  -> Configuracion de perfil (datos personales, foto)
  -> Configuracion de accesibilidad (tamano texto, contraste)
  -> Configuracion de biometria (opcional)
  -> Anadir contactos de emergencia (minimo 1)
  -> Panel principal
`

**Flujo 2: Flujo diario del usuario mayor**
`
Abrir app (biometria) -> Panel principal
  -> Ver recordatorios del dia
  -> Confirmar tomas de medicacion
  -> Registrar constantes vitales (si corresponde)
  -> Ver proximas citas
  -> (Opcional) Chat con familiar/cuidador
  -> (Opcional) Subir documento medico
`

**Flujo 3: Flujo del cuidador profesional**
`
Login -> Panel de pacientes
  -> Revisar alertas pendientes
  -> Ver estado de medicacion de cada paciente
  -> Consultar constantes vitales
  -> Anadir notas de seguimiento
  -> Enviar recordatorios personalizados
  -> Generar informes
`

**Flujo 4: Flujo de emergencia**
`
Boton SOS -> Confirmacion (3s)
  -> Obtener ubicacion GPS
  -> Enviar SMS con ubicacion a contactos
  -> Llamada automatica al contacto principal
  -> Notificacion push a todos los vinculados
  -> Registro en historial
  -> Seguimiento hasta resolucion
`

### 5.2.7 Diseno de interfaz - Wireframes de pantallas clave

**Pantalla 1: Pantalla de bienvenida (Splash/Onboarding)**
`
+---------------------------------+
|                                 |
|        [Logo App]               |
|     CuidadoMayor Plus           |
|                                 |
|   Tu bienestar, nuestra         |
|    prioridad                    |
|                                 |
|   +-------------------------+   |
|   |   [Ilustracion]         |   |
|   |   Cuidado inteligente   |   |
|   |   para personas mayores |   |
|   +-------------------------+   |
|                                 |
|   +-------------------------+   |
|   |      Comenzar           |   |  <- Boton grande, alto contraste
|   +-------------------------+   |
|                                 |
|   Ya tienes cuenta? Iniciar sesion|
|                                 |
+---------------------------------+
`

**Pantalla 2: Panel principal del usuario mayor (Dashboard)**
`
+---------------------------------+
| +  Buenos dias, Maria      (3)  |  <- Saludo + notificaciones
+---------------------------------+
| +-----------------------------+ |
| |  RECORDATORIOS DE HOY       | |
| |  +-----------------------+  | |
| |  |  Paracetamol 500mg      |  | |
| |  |   08:00 - 1 pastilla    |  | |
| |  |   [Tomado] [Posponer]   |  | |
| |  +-----------------------+  | |
| |  +-----------------------+  | |
| |  |  Omeprazol 20mg         |  | |
| |  |   14:00 - 1 capsula     |  | |
| |  |   [   Tomar   ]         |  | |
| |  +-----------------------+  | |
| +-----------------------------+ |
| +-----------------------------+ |
| |  PROXIMA CITA               | |
| |  Dr. Garcia - Cardiologia   | |
| |  15 May, 10:30 - Hosp. X    | |
| |  [Ver detalles]             | |
| +-----------------------------+ |
| +-------------+ +-------------+ |
| |  Ritmo      | |  Tension    | |
| |  72 bpm     | |  130/80     | |
| |  Normal     | |  Elevada    | |
| +-------------+ +-------------+ |
|                                 |
|    [SOS - EMERGENCIA]           |  <- Boton rojo grande
|                                 |
+---------------------------------+
|Inicio  Med   Citas  Chat  Perfil|  <- Barra navegacion inferior
+---------------------------------+
`

**Pantalla 3: Gestion de medicacion**
`
+---------------------------------+
| <  Mi Medicacion          [+]  |
+---------------------------------+
| +-----------------------------+ |
| |  Paracetamol 500mg         | |
| |    1 pastilla - 3 veces/dia | |
| |    Horarios: 08:00 14:00 20:00|
| |    [Editar] [Historial]     | |
| |    Activo                   | |
| +-----------------------------+ |
| +-----------------------------+ |
| |  Omeprazol 20mg            | |
| |    1 capsula - 1 vez/dia    | |
| |    Horario: 08:00 (ayunas)  | |
| |    [Editar] [Historial]     | |
| +-----------------------------+ |
|                                 |
| +-----------------------------+ |
| |  Historial de hoy           | |
| | 08:00 Paracetamol [OK]      | |
| | 08:00 Omeprazol [OK]        | |
| | 14:00 Paracetamol [Pendiente]| |
| | 20:00 Paracetamol [20:00]   | |
| +-----------------------------+ |
+---------------------------------+
`

**Pantalla 4: Pantalla de emergencia (SOS)**
`
+---------------------------------+
|            <  EMERGENCIA        |
+---------------------------------+
|                                 |
|        +-------------+          |
|        |             |          |
|        |    SOS      |          |
|        |             |          |
|        |  PULSAR     |          |
|        |             |          |
|        +-------------+          |
|                                 |
|  Al pulsar se enviara:          |
|  - SMS con tu ubicacion a:      |
|    - Ana (hija) 666 123 456    |
|    - Carlos (hijo) 666 789 012 |
|  - Llamada a Ana (contacto princ.)|
|  - Alerta a tu cuidador         |
|                                 |
|  +-------------------------+    |
|  |   LLAMAR 112            |    |
|  +-------------------------+    |
|                                 |
|  +-------------------------+    |
|  |   COMPARTIR UBICACION   |    |
|  +-------------------------+    |
|                                 |
+---------------------------------+
`

**Pantalla 5: Perfil del usuario mayor**
`
+---------------------------------+
| <  Mi Perfil              edit  |
+---------------------------------+
|        +-----------+            |
|        |  [Foto]   |            |
|        +-----------+            |
|     Maria Lopez Garcia          |
|     72 anos                     |
+---------------------------------+
| DATOS PERSONALES                |
|   Tel: 666 111 222             |
|   Email: maria@email.com       |
|   Dir: C/ Mayor 15, 2B         |
|      28001 Madrid              |
+---------------------------------+
| DATOS MEDICOS                   |
|   Grupo: A+                    |
|   Alergias: Penicilina         |
|   Patologias: Hipertension,    |
|      Diabetes tipo 2           |
+---------------------------------+
| CONTACTOS DE EMERGENCIA        |
|   [*] Ana Lopez (hija)         |
|      666 123 456               |
|   Carlos Lopez (hijo)          |
|      666 789 012               |
|   [Anadir contacto]            |
+---------------------------------+
| ACCESIBILIDAD                   |
|   Tamano texto: [Grande v]     |
|   Alto contraste: [ON]         |
|   Guia por voz: [OFF]          |
+---------------------------------+
|   [Cerrar sesion]              |
+---------------------------------+
`

**Pantalla 6: Pantalla de citas medicas**
`
+---------------------------------+
| <  Citas Medicas          [+]  |
+---------------------------------+
| +-----------------------------+ |
| |  15 Mayo 2026               | |
| | +-----------------------+   | |
| | | Dr. Garcia Lopez      |   | |
| | |    Cardiologia        |   | |
| | |    Hosp. La Paz       |   | |
| | |    10:30 - Consulta 4 |   | |
| | |    [Ver] [Recordatorio]|  | |
| | +-----------------------+   | |
| +-----------------------------+ |
| +-----------------------------+ |
| |  22 Mayo 2026               | |
| | +-----------------------+   | |
| | | Dra. Martin Ruiz        |   | |
| | |    Oftalmologia         |   | |
| | |    Centro Optico Plus   |   | |
| | |    12:00 - Revision     |   | |
| | +-----------------------+   | |
| +-----------------------------+ |
|                                 |
| +-----------------------------+ |
| |  PROXIMOS 30 DIAS           | |
| |  [Calendario visual]        | |
| +-----------------------------+ |
+---------------------------------+
`

**Pantalla 7: Constantes vitales**
`
+---------------------------------+
| <  Constantes Vitales     [BLE]|
+---------------------------------+
| +-----------------------------+ |
| |  TENSION ARTERIAL           | |
| |   Ultima: 130/80 mmHg       | |
| |   Hace 2 horas              | |
| |   Ligeramente elevada       | |
| |   +---------------------+   | |
| |   | [Grafico tendencia 7d]| | |
| |   +---------------------+   | |
| |   [Registrar nueva]         | |
| +-----------------------------+ |
| +-------------+ +-------------+ |
| | Ritmo       | | Glucosa     | |
| |   72 bpm    | |   110 mg/dL | |
| |   Normal    | |   Normal    | |
| +-------------+ +-------------+ |
| +-------------+ +-------------+ |
| | Saturacion  | | Temp.       | |
| |   98%       | |   36.5C     | |
| |   Normal    | |   Normal    | |
| +-------------+ +-------------+ |
|                                 |
| +-----------------------------+ |
| | DISPOSITIVOS CONECTADOS     | |
| |  [OK] Tensiometro OMRON BLE | |
| |  [OK] Glucometro Accu-Chek  | |
| |  [Anadir dispositivo]       | |
| +-----------------------------+ |
+---------------------------------+
`

**Pantalla 8: Chat**
`
+---------------------------------+
| <  Mensajes               edit  |
+---------------------------------+
| +-----------------------------+ |
| | Ana (hija)                  | |
| |    Hola mama, has tomado    | |
| |    la pastilla del...       | |
| |                  10:30 [ok] | |
| +-----------------------------+ |
| +-----------------------------+ |
| | Carmen (cuidadora)          | |
| |    Le he medido la tension  | |
| |    y esta bien.             | |
| |                  09:15 [ok] | |
| +-----------------------------+ |
| +-----------------------------+ |
| | Carlos (hijo)               | |
| |    Necesitas que te lleve   | |
| |    al medico el lunes?      | |
| |                  Ayer [ok]  | |
| +-----------------------------+ |
+---------------------------------+
`

**Pantalla 9: Conversacion de chat**
`
+---------------------------------+
| <  Ana (hija)             tel   |
+---------------------------------+
|                  +------------+ |
|                  | Hola mama, | |
|                  | como estas | |
|                  | hoy?       | |
|                  |    10:25   | |
|  +------------+|              |
|  | Hola hija, ||              |
|  | estoy bien ||              |
|  | ya tome las||              |
|  | pastillas  ||              |
|  |      10:28 ||              |
|  +------------+|              |
|                  +------------+ |
|                  | Que bien!  | |
|                  | Vienes a   | |
|                  | comer?     | |
|                  |    10:30   | |
|                                 |
| +-----------------------------+ |
| | Escribe un mensaje...  clip | |
| +-----------------------------+ |
+---------------------------------+
`

**Pantalla 10: Panel del cuidador profesional**
`
+---------------------------------+
| +  Panel Cuidador        (5)   |
+---------------------------------+
| +-----------------------------+ |
| |  RESUMEN DEL DIA            | |
| | 12 pacientes activos        | |
| |  3 alertas pendientes      | |
| |  2 visitas hoy             | |
| +-----------------------------+ |
|                                 |
| +-----------------------------+ |
| |  ATENCION REQUERIDA         | |
| | +-----------------------+   | |
| | | [ROJO] Maria Lopez      |   | |
| | |   Med. saltada 14:00    |   | |
| | |   [Ver] [Contactar]     |   | |
| | +-----------------------+   | |
| | +-----------------------+   | |
| | | [AMARILLO] Pedro Sanchez|   | |
| | |   Tension alta: 160/95  |   | |
| | |   [Ver] [Contactar]     |   | |
| | +-----------------------+   | |
| +-----------------------------+ |
|                                 |
| +-----------------------------+ |
| |  MIS PACIENTES              | |
| | [Buscar paciente...]        | |
| | +-----------------------+   | |
| | | Maria Lopez [OK]        |   | |
| | |   Med: 3/4 tomadas      |   | |
| | +-----------------------+   | |
| | +-----------------------+   | |
| | | Pedro Sanchez [OK]      |   | |
| | |   Med: 4/4 tomadas      |   | |
| | +-----------------------+   | |
| +-----------------------------+ |
+---------------------------------+
`

**Pantalla 11: Documentos medicos**
`
+---------------------------------+
| <  Documentos             [+]  |
+---------------------------------+
| [Todas] [Recetas] [Informes]   |
| [Analisis] [Seguro] [Otros]    |
+---------------------------------+
| +-----------------------------+ |
| |  Receta Paracetamol         | |
| |    Dr. Garcia - 10/05/2026  | |
| |    [Ver] [Compartir] [borrar]| |
| +-----------------------------+ |
| +-----------------------------+ |
| |  Analisis sangre            | |
| |    Lab. Clinico - 01/05/2026| |
| |    [Ver] [Compartir] [borrar]| |
| +-----------------------------+ |
|                                 |
| +-----------------------------+ |
| |  Escanear documento         | |
| |    Usa la camara para       | |
| |    digitalizar documentos   | |
| +-----------------------------+ |
+---------------------------------+
`

**Pantalla 12: Informes de salud**
`
+---------------------------------+
| <  Informes de Salud           |
+---------------------------------+
| +-----------------------------+ |
| |  GENERAR NUEVO INFORME      | |
| |  Tipo: [Mensual v]          | |
| |  Periodo: [Abril 2026]      | |
| |  Incluir:                   | |
| |  [x] Medicacion              | |
| |  [x] Constantes vitales      | |
| |  [x] Citas medicas           | |
| |  [x] Alertas                 | |
| |  +---------------------+    | |
| |  |   Generar PDF       |    | |
| |  +---------------------+    | |
| +-----------------------------+ |
|                                 |
| +-----------------------------+ |
| |  INFORMES ANTERIORES        | |
| |  Informe Marzo 2026         | |
| |   [Ver] [Compartir]         | |
| |  Informe Febrero 2026       | |
| |   [Ver] [Compartir]         | |
| +-----------------------------+ |
+---------------------------------+
`

**Pantalla 13: Geolocalizacion (vista familiar)**
`
+---------------------------------+
| <  Ubicacion de Maria     ref   |
+---------------------------------+
|  +---------------------------+  |
|  |                           |  |
|  |    [MAPA]                 |  |
|  |       [pin] Maria         |  |
|  |       En casa             |  |
|  |       Hace 5 min          |  |
|  |    +-----+               |  |
|  |    |Casa |               |  |
|  |    +-----+               |  |
|  +---------------------------+  |
|                                 |
| +-----------------------------+ |
| |  Estado actual              | |
| |    Ultima ubicacion: C/...  | |
| |    Zona: [OK] Dentro de casa| |
| |    Bateria: 78%             | |
| +-----------------------------+ |
| +-----------------------------+ |
| |  ZONAS SEGURAS              | |
| |  [OK] Casa (radio 200m)     | |
| |  [OK] Centro de dia (500m)  | |
| |  [Gestionar zonas]          | |
| +-----------------------------+ |
+---------------------------------+
`

---

# SECCION 6: DESARROLLO / IMPLEMENTACION

## 6.1 Entorno de desarrollo

### Herramientas necesarias

| Herramienta | Version | Descripcion |
|-------------|---------|-------------|
| VS Code | >= 1.85 | IDE principal con extensiones de Flutter |
| Flutter SDK | 3.24+ (Canal estable) | Framework multiplataforma |
| Dart SDK | 3.5+ (incluido con Flutter) | Lenguaje de programacion |
| Android Studio | 2024.1+ | Para emulador Android y herramientas |
| Xcode | 15+ (macOS) | Para compilacion iOS |
| Git | 2.43+ | Control de versiones |
| Node.js | 20 LTS | Para Firebase CLI y herramientas web |
| Firebase CLI | 13+ | Gestion de Firebase desde terminal |

### Extensiones de VS Code recomendadas
- Dart (oficial), Flutter (oficial), Flutter Riverpod Snippets
- Error Lens, GitLens, Todo Tree

### Proceso de configuracion
1. Instalar Flutter SDK desde el repositorio oficial
2. Ejecutar flutter doctor para verificar la instalacion
3. Configurar emuladores Android con sdkmanager y avdmanager
4. Crear el proyecto con flutter create --org com.cuidadomayor
5. Configurar Firebase con flutterfire configure
6. Instalar dependencias con flutter pub get

### Estructura del proyecto
```
cuidado_mayor_app/
+-- android/                    # Configuracion Android nativa
+-- ios/                        # Configuracion iOS nativa
+-- web/                        # Configuracion web
+-- lib/
|   +-- main.dart               # Punto de entrada
|   +-- app.dart                # Configuracion de la app
|   +-- core/                   # Nucleo compartido
|   |   +-- constants/          # Constantes, URLs, keys
|   |   +-- errors/             # Manejo de errores
|   |   +-- theme/              # Temas y estilos
|   |   +-- utils/              # Utilidades
|   |   +-- services/           # Servicios globales
|   +-- config/                 # Configuracion
|   +-- features/               # Funcionalidades por modulo
|   |   +-- auth/               # Autenticacion (data/domain/presentation)
|   |   +-- medication/         # Gestion de medicacion
|   |   +-- appointments/       # Citas medicas
|   |   +-- vitals/             # Constantes vitales
|   |   +-- alerts/             # Alertas y SOS
|   |   +-- chat/               # Mensajeria
|   |   +-- documents/          # Documentos medicos
|   |   +-- location/           # Geolocalizacion
|   |   +-- profile/            # Perfil y config.
|   +-- shared/                 # Componentes compartidos
+-- test/                       # Tests
+-- assets/                     # Recursos estaticos
+-- pubspec.yaml                # Dependencias
```

## 6.2 Configuraciones importantes

### Variables de entorno
Se utiliza String.fromEnvironment para pasar variables en tiempo de compilacion, separando entornos de desarrollo, staging y produccion. Las claves sensibles se almacenan en secrets del repositorio y se inyectan en CI/CD.

### CI/CD con GitHub Actions
El pipeline incluye: ejecucion de tests con coverage, verificacion de formato con dart format, analisis estatico con flutter analyze, compilacion de APK para Android en rama main, y compilacion de iOS en runners macOS. Los secrets se manejan mediante variables de entorno del workflow.

### Dependencias principales (pubspec.yaml)
- Estado: flutter_riverpod 2.5+, riverpod_annotation
- Firebase: firebase_core, firebase_auth, cloud_firestore, firebase_storage, firebase_messaging, firebase_analytics
- Almacenamiento local: sqflite, drift, flutter_secure_storage, shared_preferences
- UI: go_router, cached_network_image, flutter_svg, intl
- Mapas: google_maps_flutter, geolocator
- BLE: flutter_blue_plus
- Calendario: googleapis, googleapis_auth, table_calendar
- Notificaciones: flutter_local_notifications
- Chat: stream_chat_flutter
- PDF: pdf, printing
- Utilidades: uuid, connectivity_plus, image_picker, mobile_scanner, permission_handler, dartz, equatable

## 6.3 Desarrollo de modulos

### Modulo de autenticacion
```dart
abstract class AuthRepository {
  Future<Either<Failure, User>> loginWithEmailAndPassword({
    required String email, required String password});
  Future<Either<Failure, User>> register({
    required String email, required String password,
    required String fullName, required String phone,
    required DateTime dateOfBirth});
  Future<Either<Failure, void>> logout();
  Future<Either<Failure, bool>> authenticateWithBiometrics({String? reason});
  Stream<User?> get authStateChanges;
  User? get currentUser;
}

class AuthRepositoryImpl implements AuthRepository {
  final FirebaseAuth _firebaseAuth;
  final FirebaseFirestore _firestore;
  final FlutterSecureStorage _secureStorage;
  final BiometricService _biometricService;

  @override
  Future<Either<Failure, User>> loginWithEmailAndPassword({
    required String email, required String password}) async {
    try {
      final credential = await _firebaseAuth.signInWithEmailAndPassword(
        email: email, password: password);
      final userDoc = await _firestore
          .collection('users').doc(credential.user!.uid).get();
      if (!userDoc.exists) {
        return const Left(Failure('Usuario no encontrado'));
      }
      return Right(UserModel.fromDocument(userDoc).toEntity());
    } on FirebaseAuthException catch (e) {
      return Left(AuthFailure.fromFirebaseCode(e.code));
    }
  }
}
```

### Modulo de medicacion - Recordatorios
```dart
class ScheduleMedicationReminder {
  final NotificationService _notificationService;
  ScheduleMedicationReminder(this._notificationService);

  Future<Either<Failure, void>> execute(Medication medication) async {
    try {
      for (final schedule in medication.schedule) {
        for (final day in schedule.days) {
          await _notificationService.scheduleDailyNotification(
            id: '${medication.id}_${schedule.time}_$day'.hashCode,
            title: 'Hora de tu medicacion',
            body: '${medication.name} - ${medication.dosage}',
            hour: int.parse(schedule.time.split(':')[0]),
            minute: int.parse(schedule.time.split(':')[1]));
        }
      }
      return const Right(null);
    } catch (e) {
      return Left(Failure('Error al programar recordatorios: $e'));
    }
  }
}
```

### Modulo de alertas y SOS
```dart
class TriggerSOSAlert {
  final AlertRepository _alertRepo;
  final LocationService _locationService;
  final EmergencyService _emergencyService;
  final NotificationService _notificationService;

  Future<Either<Failure, Alert>> execute(String userId) async {
    try {
      final location = await _locationService.getCurrentPosition();
      final alert = Alert(
        id: const Uuid().v4(), userId: userId,
        type: AlertType.sos, severity: AlertSeverity.critical,
        message: 'Alerta SOS activada',
        location: LocationData(
          latitude: location.latitude, longitude: location.longitude),
        status: AlertStatus.active, createdAt: DateTime.now());
      await _alertRepo.createAlert(alert);
      await Future.wait([
        _emergencyService.sendSMSToContacts(alert),
        _emergencyService.callPrimaryContact(alert),
        _notificationService.sendPushToCaregivers(alert)]);
      return Right(alert);
    } catch (e) {
      return Left(Failure('Error al activar SOS: $e'));
    }
  }
}
```

### Patron arquitectonico: Repository Pattern con Either
```dart
abstract class Failure extends Equatable {
  final String message;
  const Failure(this.message);
  @override List<Object> get props => [message];
}

class AuthFailure extends Failure {
  const AuthFailure(super.message);
  factory AuthFailure.fromFirebaseCode(String code) {
    switch (code) {
      case 'user-not-found': return const AuthFailure('No existe cuenta con este email');
      case 'wrong-password': return const AuthFailure('Contrasena incorrecta');
      case 'email-already-in-use': return const AuthFailure('Email ya registrado');
      case 'weak-password': return const AuthFailure('Contrasena demasiado debil');
      default: return const AuthFailure('Error de autenticacion desconocido');
    }
  }
}

class NetworkFailure extends Failure { const NetworkFailure(super.message); }
class ServerFailure extends Failure { const ServerFailure(super.message); }
class CacheFailure extends Failure { const CacheFailure(super.message); }
```

## 6.4 Integraciones externas

### Firebase Cloud Messaging (FCM)
```dart
class NotificationService {
  final FirebaseMessaging _fcm = FirebaseMessaging.instance;
  final FlutterLocalNotificationsPlugin _local = FlutterLocalNotificationsPlugin();

  Future<void> initialize() async {
    await _fcm.requestPermission(alert: true, badge: true, sound: true);
    const androidSettings = AndroidInitializationSettings('@mipmap/ic_launcher');
    const iosSettings = DarwinInitializationSettings(
      requestAlertPermission: true, requestBadgePermission: true, requestSoundPermission: true);
    await _local.initialize(const InitializationSettings(
      android: androidSettings, iOS: iosSettings));
    final token = await _fcm.getToken();
    await _saveFcmToken(token);
    _fcm.onTokenRefresh.listen(_saveFcmToken);
    FirebaseMessaging.onMessage.listen(_handleForegroundMessage);
    FirebaseMessaging.onBackgroundMessage(_firebaseMessagingBackgroundHandler);
  }

  Future<void> scheduleDailyNotification({
    required int id, required String title, required String body,
    required int hour, required int minute, String? payload}) async {
    await _local.zonedSchedule(id, title, body,
      _nextInstanceOfTime(hour, minute),
      const NotificationDetails(
        android: AndroidNotificationDetails('medication_channel',
          'Recordatorios', importance: Importance.max, priority: Priority.high),
        iOS: DarwinNotificationDetails(
          presentAlert: true, presentBadge: true, presentSound: true)),
      androidScheduleMode: AndroidScheduleMode.exactAllowWhileIdle,
      matchDateTimeComponents: DateTimeComponents.time, payload: payload);
  }
}
```

### Google Calendar API
```dart
class CalendarService {
  late GoogleCalendarApi _calendarApi;

  Future<void> initialize(String accessToken) async {
    final client = GoogleAuthClient(accessToken);
    _calendarApi = GoogleCalendarApi(client);
  }

  Future<String?> createCalendarEvent({
    required String summary, required String description,
    required DateTime startDateTime, required DateTime endDateTime,
    String? location, List<int>? reminderMinutes}) async {
    final event = Event(
      summary: summary, description: description, location: location,
      start: EventDateTime(dateTime: startDateTime.toUtc(), timeZone: 'Europe/Madrid'),
      end: EventDateTime(dateTime: endDateTime.toUtc(), timeZone: 'Europe/Madrid'),
      reminders: EventReminderOverrides(
        overrides: (reminderMinutes ?? [1440, 120, 30])
            .map((m) => EventReminder(minutes: m, method: 'popup')).toList(),
        useDefault: false));
    final created = await _calendarApi.events.insert('primary', event);
    return created.id;
  }

  Future<List<Event>> getUpcomingEvents({int maxResults = 10}) async {
    final response = await _calendarApi.events.list('primary',
      timeMin: DateTime.now().toUtc().toIso8601String(),
      maxResults: maxResults, singleEvents: true, orderBy: 'startTime');
    return response.items ?? [];
  }
}
```

### Bluetooth Low Energy (BLE) para dispositivos de salud
```dart
class BleHealthService {
  final FlutterBluePlus _fbp;
  BluetoothDevice? _connectedDevice;
  static const bloodPressureService = '1810';
  static const heartRateService = '180D';
  static const glucoseService = '1808';

  Future<List<BluetoothDevice>> scanHealthDevices() async {
    await _fbp.startScan(timeout: const Duration(seconds: 10),
      withServices: [Guid(bloodPressureService), Guid(heartRateService), Guid(glucoseService)]);
    final devices = <BluetoothDevice>{};
    await for (final result in _fbp.scanResults) {
      if (result.isNotEmpty) devices.add(result.first.device);
    }
    return devices.toList();
  }

  Future<bool> connectToDevice(BluetoothDevice device) async {
    try {
      await device.connect(autoConnect: false);
      _connectedDevice = device;
      return true;
    } catch (e) { return false; }
  }

  Future<BloodPressureReading?> readBloodPressure() async {
    if (_connectedDevice == null) return null;
    final services = await _connectedDevice!.discoverServices();
    final bpService = services.firstWhere(
      (s) => s.uuid.str.toLowerCase() == bloodPressureService);
    final bpChar = bpService.characteristics.firstWhere(
      (c) => c.uuid.str.toLowerCase() == '2a35');
    await bpChar.setNotifyValue(true);
    final reading = await bpChar.lastValueStream.first;
    return _parseBloodPressure(reading);
  }

  BloodPressureReading _parseBloodPressure(List<int> data) {
    final systolic = _decodeUint16(data.sublist(1, 3)) / 10.0;
    final diastolic = _decodeUint16(data.sublist(3, 5)) / 10.0;
    return BloodPressureReading(
      systolic: systolic, diastolic: diastolic, timestamp: DateTime.now());
  }
  int _decodeUint16(List<int> bytes) => bytes[0] | (bytes[1] << 8);
}
```

## 6.5 Gestion del codigo y control de versiones (Git)

### Git Flow para el proyecto
```
main (produccion)
  |-- v1.0.0, v1.1.0, v1.2.0

develop (desarrollo)
  |-- feature/auth-biometric-login
  |-- feature/medication-reminders
  |-- feature/sos-alert-system
  |-- feature/vitals-ble-integration
  |-- bugfix/fix-notification-crash
  |-- hotfix/critical-security-patch
```

### Estrategia de ramas
| Rama | Proposito | Merge a |
|------|-----------|---------|
| main | Codigo en produccion | - |
| develop | Integracion de features | - |
| feature/* | Nuevas funcionalidades | develop |
| bugfix/* | Correccion de bugs | develop |
| hotfix/* | Correcciones urgentes | main + develop |
| release/* | Preparacion de version | main + develop |

### Convenciones de commits (Conventional Commits)
| Tipo | Descripcion | Ejemplo |
|------|-------------|---------|
| feat | Nueva funcionalidad | feat(medication): add barcode scanner |
| fix | Correccion de bug | fix(auth): resolve biometric timeout |
| docs | Documentacion | docs(readme): update setup instructions |
| refactor | Refactorizacion | refactor(auth): extract validation logic |
| test | Tests | test(medication): add unit tests |
| chore | Mantenimiento | chore(deps): update flutter_riverpod |
| perf | Mejora rendimiento | perf(chat): optimize message rendering |
| security | Cambios seguridad | security(auth): implement cert pinning |

## 6.6-6.10 Pruebas

### Pruebas unitarias
Framework: flutter_test + mocktail

```dart
class MockNotificationService extends Mock implements NotificationService {}

void main() {
  late ScheduleMedicationReminder usecase;
  late MockNotificationService mockNotif;

  setUp(() {
    mockNotif = MockNotificationService();
    usecase = ScheduleMedicationReminder(mockNotif);
    registerFallbackValue('');
  });

  test('debe programar notificaciones para cada horario', () async {
    final medication = Medication(
      id: 'med-001', name: 'Paracetamol', dosage: '500mg',
      frequency: MedicationFrequency.threeDaily,
      schedule: [
        ScheduleEntry(time: '08:00', days: [0,1,2,3,4,5,6]),
        ScheduleEntry(time: '14:00', days: [0,1,2,3,4,5,6]),
        ScheduleEntry(time: '20:00', days: [0,1,2,3,4,5,6])],
      startDate: DateTime.now(), isActive: true);

    when(() => mockNotif.scheduleDailyNotification(
      id: any(named: 'id'), title: any(named: 'title'),
      body: any(named: 'body'), hour: any(named: 'hour'),
      minute: any(named: 'minute'), payload: any(named: 'payload')))
        .thenAnswer((_) async => {});

    final result = await usecase.execute(medication);
    expect(result.isRight(), true);
    verify(() => mockNotif.scheduleDailyNotification(
      id: any(named: 'id'), body: 'Paracetamol - 500mg',
      hour: any(named: 'hour'), minute: any(named: 'minute'))).called(3);
  });

  test('debe devolver AuthFailure con email no registrado', () async {
    when(() => mockFirebaseAuth.signInWithEmailAndPassword(
      email: any(named: 'email'), password: any(named: 'password')))
        .thenThrow(FirebaseAuthException(code: 'user-not-found'));

    final result = await repo.loginWithEmailAndPassword(
      email: 'nonexistent@email.com', password: 'Test1234!');

    expect(result.isLeft(), true);
    result.fold(
      (failure) {
        expect(failure, isA<AuthFailure>());
        expect(failure.message, 'No existe cuenta con este email');
      }, (_) => fail('Expected failure'));
  });
}
```

### Pruebas de integracion
```dart
void main() {
  IntegrationTestWidgetsFlutterBinding.ensureInitialized();

  group('Flujo completo de medicacion', () {
    testWidgets('Usuario anade medicamento y recibe recordatorio', (tester) async {
      app.main();
      await tester.pumpAndSettle();
      await tester.tap(find.text('Medicacion'));
      await tester.pumpAndSettle();
      await tester.tap(find.byIcon(Icons.add));
      await tester.pumpAndSettle();
      await tester.enterText(
        find.byKey(const Key('medication_name')), 'Paracetamol');
      await tester.enterText(
        find.byKey(const Key('medication_dosage')), '500mg');
      await tester.tap(find.text('Guardar'));
      await tester.pumpAndSettle();
      expect(find.text('Paracetamol'), findsOneWidget);
      expect(find.text('500mg'), findsOneWidget);
    });
  });
}
```

### Pruebas de rendimiento
| Metrica | Herramienta | Objetivo |
|---------|-------------|----------|
| Tiempo de arranque | flutter run --trace-startup | Menos de 3 segundos |
| FPS | DevTools Performance | 55 FPS constantes |
| Uso de memoria | DevTools Memory | Menos de 150 MB en Android |
| Tiempo respuesta API | Postman | Menos de 500ms p95 |
| Tamano APK | flutter build apk --analyze-size | Menos de 50 MB |
| Consumo bateria | Android Battery Historian | Menos de 5% por hora |

### Pruebas de seguridad - Checklist
| Categoria | Prueba |
|-----------|--------|
| Autenticacion | Verificar que no se puede acceder sin auth |
| Autenticacion | Verificar que el token expira correctamente |
| Autenticacion | Verificar que biometria bloquea tras 3 fallos |
| Almacenamiento | Verificar que datos sensibles estan cifrados |
| Almacenamiento | Verificar que no hay datos en logs |
| Comunicacion | Verificar que todas las conexiones son HTTPS |
| Comunicacion | Verificar certificate pinning |
| Autorizacion | Verificar que un usuario no accede a datos de otro |
| Autorizacion | Verificar que roles se respetan |
| Input | Verificar sanitizacion de inputs del usuario |
| Session | Verificar que tokens se limpian al logout |

---

# SECCION 7: SEGURIDAD

## 7.1 Identificacion de riesgos

### OWASP Mobile Top 10 (2024-2025) aplicado a la aplicacion

Dado que se trata de una aplicacion de salud que maneja datos especialmente protegidos segun el RGPD (datos de salud = categoria especial, Articulo 9), la seguridad es un pilar fundamental del diseno.

| Riesgo | Descripcion | Impacto en CuidadoMayor | Mitigacion aplicada |
|--------|-------------|------------------------|---------------------|
| M1: Uso inadecuado de credenciales | API keys, contraseÃ±as o claves criptograficas hardcodeadas en el codigo | Un atacante podria acceder a Firebase o APIs externas extrayendo claves del APK | - Ninguna clave hardcodeada, variables de entorno. Firebase App Check. ProGuard/R8 para ofuscacion |
| M2: Almacenamiento inseguro de datos | Datos sensibles almacenados sin cifrar en SharedPreferences, SQLite o cache | Historial medico, medicacion o ubicacion podrian ser leidos por otra app | - flutter_secure_storage (Android Keystore / iOS Keychain). Cifrado AES-256 para DB local. Limpieza de cache |
| M3: Autenticacion/autorizacion insegura | Politicas de contraseÃ±as debiles, falta de MFA, gestion de sesiones incorrecta | Acceso no autorizado a datos de salud del usuario mayor | - Firebase Auth con validacion de contrasena fuerte. Biometria obligatoria. Tokens JWT con expiracion. 2FA opcional |
| M4: Validacion insuficiente de entrada/salida | Inyecciones SQL, XSS por no validar datos del usuario | Manipulacion de datos medicos o del sistema | - Validacion en todas las capas. Consultas parametrizadas en SQLite. Sanitizacion de inputs |
| M5: Comunicacion insegura | Datos transmitidos sin cifrar o con validacion SSL incorrecta | Intercepcion de datos de salud en transito | - TLS 1.3 obligatorio. Certificate pinning. No HTTP plano |
| M6: Controles de privacidad inadecuados | Recogida excesiva de datos, falta de consentimiento, incumplimiento RGPD | Sanciones de la AEPD, vulneracion de derechos del usuario | - Privacy by design. Consentimiento explicito. Minimizacion de datos. Derecho al olvido |
| M7: Protecciones binarias insuficientes | Falta de ofuscacion, deteccion de tampering o anti-debugging | Ingenieria inversa para extraer logica o datos | - Ofuscacion con R8/ProGuard. Deteccion de rooted/jailbreak. Integrity API de Play |
| M8: Configuracion insegura de seguridad | Modo debug en produccion, permisos excesivos | Exposicion de datos internos o funciones no deseadas | - Flags de debug desactivados en prod. Permisos minimos necesarios. Firebase Security Rules |
| M9: Almacenamiento inseguro (backups/logs) | Backups sin cifrar, logs con datos sensibles | Fuga de datos a traves de backups o archivos temporales | - Backup de DB cifrado. Logs sin datos personales. Archivos temporales eliminados |
| M10: Criptografia insuficiente | Algoritmos debiles (MD5, DES), mala gestion de claves | Datos descifrados por atacantes | - AES-256-GCM para datos en reposo. RSA-2048/ECDSA para claves. Rotacion de claves |

### Escenarios de brecha de datos de salud

**Escenario 1: Acceso no autorizado al historial medico**
Un atacante obtiene acceso a la cuenta de un usuario mayor mediante phishing. Podria consultar todo el historial medico, medicamentos y ubicacion. Mitigacion: 2FA obligatorio para acceder a datos medicos sensibles, biometria requerida, timeout de sesion de 15 minutos.

**Escenario 2: Fuga de datos por API no protegida**
Una API de la aplicacion no valida correctamente los tokens de autorizacion, permitiendo que un usuario acceda a datos de otro usuario. Mitigacion: Firebase Security Rules estrictas, validacion de ownership en cada consulta, tests de automatizacion de seguridad.

**Escenario 3: Intercepcion de datos en red publica WiFi**
Un usuario mayor usa la aplicacion en una red WiFi publica sin cifrar. Un atacante en la misma red podria interceptar los datos de salud transmitidos. Mitigacion: TLS 1.3 obligatorio, certificate pinning, deteccion de redes inseguras con aviso al usuario.

**Escenario 4: Extraccion de datos del dispositivo perdido**
El dispositivo del usuario mayor es perdido o robado. El atacante podria extraer datos de la base de datos local SQLite. Mitigacion: Cifrado completo de la base de datos local con SQLCipher, borrado remoto de datos, biometria obligatoria para abrir la app.

## 7.2 Autenticacion y autorizacion

### OAuth 2.0 con PKCE
Para la autenticacion con proveedores externos (Google Sign-In), se implementa OAuth 2.0 con PKCE (Proof Key for Code Exchange), que es el estandar recomendado para aplicaciones moviles nativas:

```dart
// Flujo de autenticacion OAuth 2.0 con PKCE
Future<UserCredential> signInWithGoogle() async {
  // 1. Generar code verifier y challenge (PKCE)
  final codeVerifier = generateCodeVerifier();
  final codeChallenge = generateCodeChallenge(codeVerifier);

  // 2. Iniciar el flujo de autorizacion
  final result = await appAuth.authorizeAndExchangeCode(
    AuthorizationTokenRequest(
      clientId, redirectUri,
      discoveryUrl: discoveryUrl,
      scopes: ['openid', 'profile', 'email'],
      promptValues: ['login'],
      codeVerifier: codeVerifier,
    ));

  // 3. Obtener credenciales de Firebase
  final googleCredential = GoogleAuthProvider.credential(
    idToken: result.idToken, accessToken: result.accessToken);

  // 4. Autenticar con Firebase
  return await FirebaseAuth.instance.signInWithCredential(googleCredential);
}
```

### Autenticacion biometrica
Se utiliza el plugin local_auth o biometric_shield para autenticacion biomtrica con fallback a PIN:

```dart
class BiometricService {
  final LocalAuthentication _localAuth = LocalAuthentication();

  Future<bool> canAuthenticate() async {
    final canCheck = await _localAuth.canCheckBiometrics;
    final biometrics = await _localAuth.getAvailableBiometrics();
    return canCheck && biometrics.isNotEmpty;
  }

  Future<bool> authenticate({String? reason}) async {
    try {
      return await _localAuth.authenticate(
        localizedReason: reason ?? 'Verifica tu identidad',
        options: const AuthenticationOptions(
          biometricOnly: false, // Permite fallback a PIN
          stickyAuth: true,     // Mantiene auth si la app va a background
          sensitiveTransaction: true));
    } on PlatformException catch (e) {
      return false;
    }
  }
}
```

### Autenticacion de dos factores (2FA)
Para acciones criticas (cambio de contrasena, eliminacion de cuenta, acceso a informes medicos completos), se solicita un segundo factor de autenticacion:

```dart
class TwoFactorAuthService {
  // TOTP (Time-based One-Time Password)
  Future<bool> verifyTOTP(String userId, String code) async {
    final secret = await _getTOTPSecret(userId);
    final totp = TOTP(secret);
    return totp.verify(code);
  }

  // Verificacion por email
  Future<bool> sendEmailVerification(String email) async {
    final code = generateVerificationCode(); // 6 digitos
    await _emailService.sendVerificationCode(email, code);
    await _cacheVerificationCode(email, code, Duration(minutes: 10));
    return true;
  }
}
```

### Control de acceso basado en roles (RBAC)

| Rol | Permisos |
|-----|----------|
| Usuario mayor | Ver su perfil, gestionar su medicacion, ver sus citas, registrar constantes, activar SOS, chat con sus contactos |
| Familiar | Ver ubicacion del usuario mayor, ver estado de medicacion, recibir alertas SOS, chat con cuidador, generar informes |
| Cuidador profesional | Gestionar medicacion de usuarios asignados, ver constantes vitales, aÃ±adir notas de seguimiento, recibir alertas, chat con familiares |
| Administrador | Gestionar todos los usuarios, asignar roles, configurar parametros del sistema, ver logs de auditoria |

```dart
// Firebase Security Rules - ejemplo de RBAC
match /medications/{medicationId} {
  allow read: if request.auth != null &&
    (resource.data.userId == request.auth.uid ||
     isCaregiverOf(request.auth.uid, resource.data.userId) ||
     isFamilyMemberOf(request.auth.uid, resource.data.userId));

  allow write: if request.auth != null &&
    (resource.data.userId == request.auth.uid ||
     isCaregiverOf(request.auth.uid, resource.data.userId));
}

match /alerts/{alertId} {
  allow read, write: if request.auth != null &&
    (resource.data.userId == request.auth.uid ||
     isAdmin(request.auth.uid));

  allow create: if request.auth != null;
}
```

## 7.3 Gestion de sesiones

### Gestion de tokens JWT

```dart
class SessionManager {
  final FlutterSecureStorage _storage = FlutterSecureStorage();
  static const _accessTokenKey = 'access_token';
  static const _refreshTokenKey = 'refresh_token';
  static const _tokenExpiryKey = 'token_expiry';

  // Almacenar tokens tras login exitoso
  Future<void> saveTokens({
    required String accessToken,
    required String refreshToken,
    required DateTime expiry}) async {
    await _storage.write(key: _accessTokenKey, value: accessToken);
    await _storage.write(key: _refreshTokenKey, value: refreshToken);
    await _storage.write(key: _tokenExpiryKey, value: expiry.toIso8601String());
  }

  // Obtener token valido, refrescando si es necesario
  Future<String?> getValidToken() async {
    final expiryStr = await _storage.read(key: _tokenExpiryKey);
    if (expiryStr == null) return null;

    final expiry = DateTime.parse(expiryStr);
    final now = DateTime.now();

    // Refrescar si expira en menos de 5 minutos
    if (expiry.difference(now).inMinutes < 5) {
      final refreshed = await _refreshToken();
      if (!refreshed) return null;
    }

    return await _storage.read(key: _accessTokenKey);
  }

  // Refrescar token de acceso
  Future<bool> _refreshToken() async {
    final refreshToken = await _storage.read(key: _refreshTokenKey);
    if (refreshToken == null) return false;

    try {
      final response = await http.post(
        Uri.parse('${Environment.baseUrl}/auth/refresh'),
        headers: {'Authorization': 'Bearer $refreshToken'});

      if (response.statusCode == 200) {
        final data = jsonDecode(response.body);
        await saveTokens(
          accessToken: data['access_token'],
          refreshToken: data['refresh_token'] ?? refreshToken,
          expiry: DateTime.now().add(Duration(hours: 1)));
        return true;
      }
      return false;
    } catch (e) {
      return false;
    }
  }

  // Cerrar sesion - limpiar todos los tokens
  Future<void> logout() async {
    await _storage.deleteAll();
    await FirebaseAuth.instance.signOut();
  }
}
```

### Timeout de sesion
- **Sesion activa**: 24 horas con actividad
- **Inactividad**: Tras 15 minutos sin interaccion, se requiere reautenticacion biometrica
- **Token de acceso**: Expira en 1 hora, se refresca automaticamente
- **Refresh token**: Expira en 7 dias, requiere login completo tras expiracion
- **Forzar cierre**: Al detectar cambio de dispositivo o ubicacion sospechosa

```dart
class SessionTimeoutService {
  Timer? _inactivityTimer;
  final Duration _timeout = Duration(minutes: 15);

  void startTracking() {
    // Reiniciar timer en cada interaccion del usuario
    _inactivityTimer?.cancel();
    _inactivityTimer = Timer(_timeout, _onSessionTimeout);
  }

  void _onSessionTimeout() {
    // Requerir reautenticacion biometrica
    _requireReauthentication();
  }

  void reset() {
    _inactivityTimer?.cancel();
    startTracking();
  }
}
```

## 7.4 Cifrado de datos

### Cifrado de datos en reposo
- **SQLite local**: Cifrado con SQLCipher usando AES-256-CBC
- **flutter_secure_storage**: Usa Android Keystore y iOS Keychain para almacenar tokens y claves
- **Firebase Firestore**: Cifrado automatico en reposo gestionado por Google (AES-256)
- **Firebase Storage**: Cifrado automatico en reposo con claves gestionadas por Google

```dart
// Cifrado de base de datos local con SQLCipher via sqflite_sqlcipher
import 'package:sqflite_sqlcipher/sqflite.dart';

Future<Database> openSecureDatabase(String path, String password) async {
  return openDatabase(
    path,
    password: password, // Clave derivada del keystore del dispositivo
    version: 1,
    onCreate: (db, version) async {
      await db.execute('''
        CREATE TABLE medications (
          id TEXT PRIMARY KEY,
          user_id TEXT NOT NULL,
          name TEXT NOT NULL,
          dosage TEXT NOT NULL,
          encrypted_data BLOB
        )
      ''');
    });
}

// Cifrado de datos sensibles antes de almacenar
import 'package:encrypt/encrypt.dart' as encrypt;

class DataEncryptionService {
  final encrypt.Key _key;
  final encrypt.IV _iv;

  DataEncryptionService()
      : _key = encrypt.Key.fromSecureStorage('encryption_key'),
        _iv = encrypt.IV.fromSecureStorage('encryption_iv');

  String encryptData(String plainText) {
    final encrypter = encrypt.Encrypter(
      encrypt.AES(_key, mode: encrypt.AESMode.gcm));
    final encrypted = encrypter.encrypt(plainText, iv: _iv);
    return encrypted.base64;
  }

  String decryptData(String encryptedText) {
    final encrypter = encrypt.Encrypter(
      encrypt.AES(_key, mode: encrypt.AESMode.gcm));
    final encrypted = encrypt.Encrypted.fromBase64(encryptedText);
    return encrypter.decrypt(encrypted, iv: _iv);
  }
}
```

### Cifrado de datos en transito (TLS)
- **HTTPS obligatorio**: Todas las comunicaciones con Firebase y APIs externas usan TLS 1.3
- **Certificate pinning**: Se implementa pinning de certificados para prevenir ataques Man-in-the-Middle

```dart
// Certificate pinning con http client
import 'package:http/io_client.dart';

class SecureHttpClient {
  static final HttpClient _httpClient = HttpClient()
    ..badCertificateCallback = (cert, host, port) {
      // Verificar fingerprint del certificado
      final expectedFingerprint = 'SHA256:expected_cert_hash_here';
      final actualFingerprint = _getCertFingerprint(cert);
      return expectedFingerprint == actualFingerprint;
    };

  static final Client client = IOClient(_httpClient);

  static String _getCertFingerprint(X509Certificate cert) {
    // Calcular SHA-256 del certificado
    return 'SHA256:${_calculateSHA256(cert.der)}';
  }
}
```

### Cifrado de mensajes en el chat (End-to-End)
Para las conversaciones entre familiares y cuidadores, se implementa cifrado de extremo a extremo:

```dart
class EndToEndEncryption {
  // Generar par de claves para el usuario
  Future<AsymmetricKeyPair> generateKeyPair() async {
    final rsaKeyGenerator = RSAKeyGenerator();
    final secureRandom = FortunaRandom();
    secureRandom.seed(KeyParameter(
      Uint8List.fromList(List.generate(32, (_) => Random.secure().nextInt(256)))));
    rsaKeyGenerator.init(ParametersWithRandom(
      RSAKeyGeneratorParameters(BigInt.parse('65537'), 2048, 64), secureRandom));
    return rsaKeyGenerator.generateKeyPair();
  }

  // Cifrar mensaje con clave publica del destinatario
  String encryptMessage(String message, String recipientPublicKey) {
    final encrypter = encrypt.Encrypter(encrypt.RSA(
      publicKey: encrypt.RSAPublicKey.fromString(recipientPublicKey)));
    return encrypter.encrypt(message).base64;
  }

  // Descifrar mensaje con clave privada del destinatario
  String decryptMessage(String encryptedMessage, String privateKey) {
    final encrypter = encrypt.Encrypter(encrypt.RSA(
      privateKey: encrypt.RSAPrivateKey.fromString(privateKey)));
    return encrypter.decrypt(encrypt.Encrypted.fromBase64(encryptedMessage));
  }
}
```

## 7.5 Buenas practicas aplicadas

### Cumplimiento OWASP MASVS (Mobile App Security Verification Standard)

**Nivel 1 (Estandar):**
- V1: Arquitectura - La app usa Clean Architecture con separacion clara de capas
- V2: Almacenamiento de datos - Datos sensibles cifrados con AES-256
- V3: Criptografia - TLS 1.3, AES-256-GCM, RSA-2048
- V4: Autenticacion - Firebase Auth + biometria + 2FA
- V5: Autorizacion - RBAC con Firebase Security Rules
- V6: Manejo de red - Certificate pinning, TLS obligatorio
- V7: Interaccion con plataforma - Permisos minimos, sin datos en logs
- V8: Resiliencia - Deteccion de rooted/jailbreak, ofuscacion

**Nivel 2 (Fortificado) - para datos de salud:**
- V2-R: Almacenamiento - Cifrado completo de la base de datos local
- V4-R: Autenticacion - Biometria obligatoria, 2FA para acciones criticas
- V6-R: Red - Certificate pinning estricto, sin excepciones

### Buenas practicas de codificacion segura

1. **Principio de minimo privilegio**: Cada componente solo tiene los permisos estrictamente necesarios
2. **Validacion de entrada en todas las capas**: Frontend, backend y base de datos
3. **No almacenar datos sensibles en logs**: Usar logger configurado para excluir PII
4. **Sanitizacion de salida**: Escape de HTML en vistas web, validacion de URLs
5. **Gestion segura de errores**: Mensajes genericos al usuario, detalles solo en logs internos
6. **Actualizacion de dependencias**: Revisar periodicamente vulnerabilidades en pub.dev
7. **Secretos en variables de entorno**: Nunca hardcodear claves en el codigo
8. **Revision de codigo**: Pull requests requieren revision de al menos un desarrollador
9. **Tests de seguridad automatizados**: Integrados en el pipeline de CI/CD

### Cumplimiento RGPD en Espana

Dado que la aplicacion maneja datos de salud (categoria especial segun Articulo 9 del RGPD), se aplican las siguientes medidas:

- **Consentimiento explicito**: El usuario debe aceptar explicitamente el tratamiento de sus datos de salud
- **Minimizacion de datos**: Solo se recogen datos estrictamente necesarios para la funcionalidad
- **Derecho al olvido**: El usuario puede solicitar la eliminacion completa de sus datos
- **Portabilidad**: Los datos pueden exportarse en formato estandar (JSON/PDF)
- **DPO (Delegado de Proteccion de Datos)**: Designado para supervisar el cumplimiento
- **Registro de actividades de tratamiento**: Documentado segun Articulo 30 RGPD
- **Notificacion de brechas**: Procedimiento establecido para notificar a la AEPD en 72 horas
- **Evaluacion de impacto (DPIA)**: Realizada antes del lanzamiento, dado el alto riesgo para los derechos del usuario

## 7.6 Pruebas de seguridad

### Enfoque de pruebas de penetracion

**Fase 1: Analisis estatico (SAST)**
- Analisis del codigo fuente con herramientas como Dart Analyzer con reglas de seguridad
- Busqueda de secretos hardcodeados con truffleHog o gitleaks
- Verificacion de dependencias vulnerables con dart pub deps y audit

**Fase 2: Analisis dinamico (DAST)**
- Interceptacion de trafico con Burp Suite o OWASP ZAP
- Pruebas de inyeccion en inputs de la aplicacion
- Verificacion de certificate pinning
- Pruebas de manipulacion de tokens JWT

**Fase 3: Analisis del binario**
- Decompilacion del APK con jadx para verificar ofuscacion
- Busqueda de claves o secrets en el binario
- Verificacion de permisos declarados en AndroidManifest.xml
- Analisis de las reglas de seguridad de Firebase

### Checklist de auditoria de seguridad

| Categoria | Verificacion | Estado |
|-----------|-------------|--------|
| **Autenticacion** | ContraseÃ±as con politica de fortaleza (8+ chars, mayus, numero, simbolo) | |
| **Autenticacion** | Biometria obligatoria para abrir la app | |
| **Autenticacion** | Bloqueo tras 3 intentos fallidos de biometria | |
| **Autenticacion** | 2FA para acciones criticas (cambio contrasena, eliminacion cuenta) | |
| **Almacenamiento** | Base de datos local cifrada con SQLCipher | |
| **Almacenamiento** | Tokens en flutter_secure_storage (Keystore/Keychain) | |
| **Almacenamiento** | No hay datos sensibles en SharedPreferences sin cifrar | |
| **Almacenamiento** | Cache limpiado tras logout | |
| **Comunicacion** | Todas las conexiones usan HTTPS/TLS 1.3 | |
| **Comunicacion** | Certificate pinning implementado | |
| **Comunicacion** | No se envian datos sensibles por URL | |
| **Comunicacion** | Headers de seguridad configurados (CSP, HSTS) | |
| **Autorizacion** | Firebase Security Rules restrictivas | |
| **Autorizacion** | Validacion de ownership en cada operacion | |
| **Autorizacion** | Roles correctamente segregados | |
| **Input** | Validacion de todos los inputs del usuario | |
| **Input** | Sanitizacion de datos antes de almacenar | |
| **Input** | Limitacion de tamano de uploads | |
| **Sesion** | Tokens expiran correctamente | |
| **Sesion** | Refresh tokens con ciclo de vida limitado | |
| **Sesion** | Logout invalida todos los tokens | |
| **Sesion** | Timeout de inactividad configurado (15 min) | |
| **Binario** | Ofuscacion con R8/ProGuard activada | |
| **Binario** | Modo debug desactivado en produccion | |
| **Binario** | Deteccion de rooted/jailbreak implementada | |
| **Binario** | No hay logs con datos sensibles | |
| **RGPD** | Consentimiento explicito para datos de salud | |
| **RGPD** | Politica de privacidad accesible | |
| **RGPD** | Derecho al olvido implementado | |
| **RGPD** | Exportacion de datos funcional | |

### Herramientas recomendadas para auditoria

| Herramienta | Tipo | Uso |
|-------------|------|-----|
| OWASP ZAP | DAST | Analisis dinamico de seguridad |
| Burp Suite | DAST | Interceptacion y manipulacion de trafico |
| MobSF | SAST+DAST | Analisis automatizado de apps moviles |
| truffleHog | Secrets | Busqueda de secretos en repositorio |
| dart pub audit | Dependencias | Vulnerabilidades en dependencias |
| Firebase Security Rules Simulator | Config | Verificacion de reglas de Firestore |
| jadx | Binario | Decompilacion de APK para analisis |
| ADB + logcat | Runtime | Verificacion de logs en tiempo real |

### Sugerencias de infografias y graficos para incluir

1. **Infografia de arquitectura de seguridad**: Diagrama que muestra las capas de seguridad desde el dispositivo hasta la nube, incluyendo cifrado en cada nivel
2. **Grafico de flujo de autenticacion**: Diagrama de secuencia detallado del flujo OAuth 2.0 + PKCE + biometria
3. **Tabla comparativa de riesgos OWASP**: Matriz de probabilidad vs impacto para cada riesgo del Mobile Top 10
4. **Diagrama de cumplimiento RGPD**: Flujo que muestra como la app cumple con cada requisito del RGPD para datos de salud
5. **Grafico de evolucion de vulnerabilidades**: Linea temporal mostrando como se han mitigado las vulnerabilidades encontradas durante el desarrollo
6. **Infografia de cifrado**: Diagrama visual mostrando donde se aplica cada tipo de cifrado (AES-256, TLS 1.3, RSA-2048) en la aplicacion

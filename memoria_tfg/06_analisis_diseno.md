# 4. Análisis y Diseño

## 4.1 Requisitos Funcionales

| ID | Requisito | Prioridad |
|----|-----------|-----------|
| RF01 | El usuario debe poder registrarse e iniciar sesión | Alta |
| RF02 | El usuario debe poder gestionar su perfil | Media |
| RF03 | El usuario debe poder añadir, editar y eliminar medicaciones | Alta |
| RF04 | El sistema debe notificar al usuario según el horario de cada medicación | Alta |
| RF05 | Las notificaciones deben sobrevivir al reinicio del dispositivo | Alta |
| RF06 | El usuario debe poder añadir y eliminar contactos de emergencia | Alta |
| RF07 | El sistema debe enviar SMS y WhatsApp a contactos al activar SOS | Alta |
| RF08 | El sistema debe realizar una llamada telefónica al activar SOS | Alta |
| RF09 | El sistema debe incluir la ubicación GPS en el mensaje SOS | Alta |
| RF10 | El usuario debe poder registrar mediciones de salud | Alta |
| RF11 | El sistema debe mostrar gráficas de evolución de salud | Media |
| RF12 | El usuario debe poder exportar registros de salud a CSV | Media |
| RF13 | El usuario debe poder gestionar eventos de calendario | Alta |
| RF14 | El calendario debe sincronizarse con Firestore | Media |
| RF15 | El usuario debe poder invitar miembros al círculo familiar | Alta |
| RF16 | El círculo familiar debe soportar roles (admin, caregiver, viewer) | Media |
| RF17 | El sistema debe solicitar permisos runtime necesarios | Alta |
| RF18 | La interfaz debe ser accesible para personas mayores | Alta |

## 4.2 Requisitos No Funcionales

| ID | Requisito | Descripción |
|----|-----------|-------------|
| RNF01 | Rendimiento | La app debe responder en menos de 2 segundos |
| RNF02 | Offline | Todas las funciones principales deben funcionar sin conexión |
| RNF03 | Accesibilidad | Texto mínimo 18sp, botones mínimo 64dp, alto contraste |
| RNF04 | Persistencia | Los datos deben persistir localmente en Room |
| RNF05 | Seguridad | Las contraseñas se almacenan localmente |
| RNF06 | Compatibilidad | minSdk 27 (Android 8.1) |
| RNF07 | Fiabilidad | Los recordatorios deben funcionar incluso tras reinicio |

## 4.3 Casos de Uso Principales

### CU01: Gestión de Medicación
1. El usuario accede a la pantalla de medicación
2. Añade una nueva medicación con nombre, dosis, frecuencia y hora
3. El sistema guarda la medicación y programa el recordatorio
4. A la hora indicada, el sistema muestra una notificación
5. El usuario puede editar o eliminar la medicación

### CU02: Activación de SOS
1. El usuario accede a la pantalla SOS
2. Pulsa el botón de emergencia
3. El sistema solicita permisos si no están concedidos
4. Muestra diálogo de confirmación con los contactos
5. El usuario confirma
6. El sistema obtiene la ubicación GPS
7. Envía SMS y WhatsApp con la ubicación a cada contacto
8. Inicia una llamada telefónica al primer contacto

### CU03: Seguimiento de Salud
1. El usuario accede a la pantalla de salud
2. Añade un registro (tipo, valor, nota)
3. El sistema guarda y muestra el registro en la lista
4. El usuario puede ver gráficas de evolución
5. Puede exportar todos los registros a CSV

### CU04: Gestión del Calendario
1. El usuario accede al calendario
2. Añade un evento con título, fecha, hora y tipo
3. El sistema guarda localmente y sincroniza con Firestore
4. Otros miembros del círculo familiar ven el evento

### CU05: Círculo Familiar
1. El usuario accede al círculo familiar
2. Invita a un miembro con nombre, email y rol
3. El sistema guarda localmente y sincroniza con Firestore
4. Los miembros pueden ver eventos y estado del mayor

## 4.4 Arquitectura del Sistema

```
┌─────────────────────────────────────────┐
│              Capa de Presentación        │
│     (Activities, Adapters, Layouts)      │
├─────────────────────────────────────────┤
│           Capa de Lógica/Negocio         │
│   (Helpers, Managers, WorkScheduler)     │
├─────────────────────────────────────────┤
│           Capa de Persistencia           │
│       (Room Entities, DAOs, DB)          │
├─────────────────────────────────────────┤
│         Capa de Sincronización           │
│  (Firebase Auth, Firestore, FCM)         │
└─────────────────────────────────────────┘
```

### 4.5 Diagrama de Base de Datos

**Entidades y Relaciones:**

- **UserEntity** (1) ──── (N) **Medicacion**
- **UserEntity** (1) ──── (N) **ContactoEmergencia**
- **UserEntity** (1) ──── (N) **RegistroSalud**
- **UserEntity** (1) ──── (N) **EventoCalendario**
- **UserEntity** (1) ──── (N) **MiembroFamiliar**
- **UserEntity** (1) ──── (N) **Abuelito**
- **UserEntity** (1) ──── (N) **ServicioHistorial**

## 4.6 Diseño de la Interfaz

La interfaz sigue los principios de **Material Design 3** adaptados para accesibilidad:

### Paleta de Colores
- **Primary**: #0055CC (azul oscuro, alto contraste)
- **Error**: #D92D20 (rojo intenso para emergencias y errores)
- **Surface**: #FFFFFF (fondo blanco limpio)
- **On Surface**: #000000 (texto negro puro)
- **Background**: #F5F5F7 (fondo gris claro)

### Tipografía
- Headlines: 30-36sp, bold
- Títulos: 22sp, bold
- Cuerpo: 18sp
- Etiquetas: 15sp

### Componentes
- Botones accesibles: minHeight 64dp
- Botón emergencia: minHeight 80dp, color rojo
- Tarjetas: border-radius 16dp, padding 24dp
- Animaciones de transición slide-in/out

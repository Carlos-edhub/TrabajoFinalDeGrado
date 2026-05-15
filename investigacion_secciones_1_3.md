# INVESTIGACIÓN TFG - SECCIONES 1-3
## Aplicación Multiplataforma para Servicios de Cuidado de Personas Mayores en España

---

# SECCIÓN 1: RESUMEN

Este Trabajo de Fin de Grado presenta el diseño y desarrollo de una aplicación multiplataforma orientada a la prestación de servicios de cuidado para personas mayores en España. La aplicación surge como respuesta al acelerado envejecimiento demográfico del país, donde la población de 65 años y más ha superado por primera vez los 10 millones de personas, representando el 20,7% del total de la población (INE, 2025). Este fenómeno se ve agravado por el hecho de que más de 2,9 millones de personas superan ya los 80 años, y cerca de 296.000 personas permanecen en lista de espera para recibir prestaciones por dependencia (IMSERSO, 2024).

La aplicación propone un ecosistema digital que integra funcionalidades de gestión de medicamentos, monitorización de constantes vitales, comunicación entre familiares y cuidadores, servicios de teleasistencia y acceso a profesionales de la salud, todo ello diseñado con criterios de accesibilidad específicos para personas mayores. El desarrollo se ha llevado a cabo utilizando un enfoque ágil (Scrum), con una arquitectura multiplataforma que permite el despliegue simultáneo en sistemas Android e iOS, garantizando la máxima cobertura de usuarios potenciales.

El proyecto aborda además la brecha digital existente en este colectivo: mientras que el 80,1% de las personas de 65-74 años ha usado internet en los últimos tres meses, esta cifra cae drásticamente al 41,7% en mayores de 75 años (INE, 2023). Por ello, la interfaz de usuario ha sido diseñada siguiendo las pautas WCAG 2.1 de accesibilidad, con tipografías amplias, contrastes elevados y navegación simplificada. La aplicación también incorpora funcionalidades de asistencia por voz e inteligencia artificial para facilitar la interacción de usuarios con menor alfabetización digital.

El impacto social esperado incluye la reducción del aislamiento social —que afecta al 20% de los mayores de 75 años según el Barómetro SoledadES (2024)—, la mejora en la adherencia terapéutica, la coordinación familiar en los cuidados, y la减轻 de la carga que soportan los cuidadores informales, de los cuales el 87,9% son mujeres (IMSERSO, 2024).

**Palabras clave:** personas mayores, dependencia, teleasistencia, aplicación multiplataforma, envejecimiento activo, salud digital, soledad no deseada, accesibilidad, cuidadores, silver economy, SAAD, Flutter/React Native.

---

# SECCIÓN 2: INTRODUCCIÓN

## 2.1 Presentación del proyecto

Este Trabajo de Fin de Grado (TFG) se enmarca dentro del ciclo formativo de Desarrollo de Aplicaciones Multiplataforma (DAM) y presenta el diseño, arquitectura e implementación de prototipo de una aplicación móvil destinada a facilitar los servicios de cuidado y atención a personas mayores en España.

El proyecto se estructura en torno a tres ejes fundamentales:

1. **Análisis del contexto sociodemográfico:** Estudio exhaustivo de la situación de las personas mayores en España, incluyendo datos de envejecimiento poblacional, dependencia, soledad no deseada y brecha digital, utilizando fuentes oficiales como el Instituto Nacional de Estadística (INE), el Instituto de Mayores y Servicios Sociales (IMSERSO), el Consejo Superior de Investigaciones Científicas (CSIC) y la Organización Mundial de la Salud (OMS).

2. **Diseño y desarrollo de la aplicación:** Definición de requisitos funcionales y no funcionales, diseño de la arquitectura de software (frontend multiplataforma, backend, base de datos), diseño de interfaz de usuario centrado en la accesibilidad (UI/UX para personas mayores), e implementación de un prototipo funcional utilizando tecnologías multiplataforma (Flutter o React Native).

3. **Validación y evaluación:** Pruebas de usabilidad con usuarios del colectivo objetivo, evaluación de accesibilidad, y análisis del impacto potencial de la aplicación en la mejora de la calidad de vida de las personas mayores y sus cuidadores.

**[SUGERENCIA: Insertar aquí un diagrama general de la arquitectura de la aplicación]**

**[SUGERENCIA: Incluir un esquema visual de las tres fases del proyecto]**

---

## 2.2 Motivación y origen de la idea

### Datos demográficos clave

El envejecimiento de la población española es uno de los fenómenos demográficos más destacados de nuestro tiempo. Según los datos más recientes del INE:

- **Población mayor de 65 años:** A 1 de enero de 2025, España cuenta con 10.178.625 personas de 65 años y más, lo que representa el **20,7%** del total de la población de 49.128.297 habitantes (INE, Censo Anual de Población 2025).

- **Población mayor de 80 años (sobreenvejecimiento):** 2.950.434 personas, alcanzando el **6,1%** del total, frente al 1,6% registrado en 1975. Este dato evidencia un claro proceso de sobreenvejecimiento (Envejecimiento en Red-CSIC, 2025).

- **Población mayor de 90 años:** 653.520 personas, representando el **1,3%** de la población, cifra que se ha duplicado respecto al 0,6% de 1975.

- **Esperanza de vida:** En 2024, la esperanza de vida al nacer alcanzó un récord histórico de **84,01 años** (81,38 años para hombres y 86,53 años para mujeres). A los 65 años, una persona puede esperar vivir **21,87 años** adicionales (INE, Movimiento Natural de la Población 2024).

### Proyecciones futuras

Las Proyecciones de Población del INE (serie 2024-2074) indican que:

- Para **2045**, la población de personas mayores podría superar los **15,9 millones**, equivalente al **29,2%** del total.
- El porcentaje de población de 65 años y más alcanzará un **máximo del 30,5%** en torno a 2055.
- La tasa de dependencia (relación entre población menor de 16 o mayor de 64 y la de 16 a 64 años) alcanzará un máximo del **75,3%** en torno a 2052.

**[SUGERENCIA: Insertar gráfico de evolución de la población mayor de 65 años en España (1975-2025) con proyección hasta 2050]**

**[SUGERENCIA: Incluir pirámide poblacional de España comparativa 2025 vs 2050]**

### Contexto mundial

Según la OMS (2024):
- A nivel mundial, se prevé que el número de personas de 60 años o más pase de **1.100 millones en 2023** a **1.400 millones en 2030**.
- La esperanza de vida al nacer alcanzó los **73,3 años** a nivel mundial en 2024.
- Las enfermedades crónicas no transmisibles representan el **87,2%** de las causas de muerte en personas mayores.

### La silver economy como oportunidad

La economía plateada (silver economy) en España tiene un impacto equivalente al **26% del PIB**. Los mayores de 55 años representan el **60% del gasto en consumo** del país. El mercado global de tecnologías para la salud digital para personas mayores crecerá a una tasa del **15% anual**, pudiendo superar los **280.000 millones de dólares para 2026** (La Vanguardia, 2024; El Periódico, 2024).

---

## 2.3 Contexto: Problema detectado y necesidad

### 2.3.1 La dependencia en España

El Sistema para la Autonomía y Atención a la Dependencia (SAAD) atiende actualmente a una cifra récord de personas:

- **Solicitudes totales:** 2.437.488 solicitudes a marzo de 2026, con un crecimiento del 7,3% interanual.
- **Personas con prestación efectiva:** **1.655.446** personas (máximo histórico), un 9,8% más que en marzo de 2025.
- **Lista de espera:** 152.249 personas (reducción del 16,6% respecto a marzo de 2025, pero aún una cifra significativa).
- **Tiempo medio de resolución:** **338 días**, muy por encima de los 180 días que establece la ley (RTVE, 2025; Ministerio de Derechos Sociales, 2026).

**Perfil tipo del solicitante:** Mujer mayor de 80 años (el 62% de los solicitantes son mujeres).

**[SUGERENCIA: Insertar gráfico de evolución de solicitudes y beneficiarios del SAAD (2019-2026)]**

**[SUGERENCIA: Incluir infografía sobre el "limbo de la dependencia"]**

### 2.3.2 Soledad no deseada

La soledad no deseada constituye una epidemia silenciosa:

- El **20%** de la población adulta en España sufre soledad no deseada (Barómetro SoledadES, 2024).
- En el grupo de **mayores de 75 años**, la incidencia vuelve a subir hasta el **20%**, tras descender en la franja de 55-74 años (14,8%).
- El **39%** de las personas mayores de 65 años presenta soledad emocional según un estudio de La Caixa.
- La soledad crónica afecta al **13,5%** de la población española.
- Dos de cada tres personas que sufren soledad llevan en esta situación **más de 2 años**, y el 59% más de 3 años.
- El **41,7%** de las personas que viven solas en España tiene 65 años o más (IMSERSO, 2024).
- El coste económico de la soledad no deseada se estima en **14.141 millones de euros anuales**, equivalente al **1,17% del PIB** (SoledadES, 2023).

### 2.3.3 Cuidadores informales

- **83.589** cuidadores no profesionales dados de alta en convenio especial (IMSERSO, julio 2024).
- El **87,9%** de estos cuidadores son mujeres (casi 9 de cada 10).
- El **37,1%** de los hogares con personas dependientes tiene una **necesidad no cubierta** de servicios de cuidados a domicilio (INE, ECV 2024).
- El **18,6%** de los hogares con personas dependientes que recibieron cuidados a domicilio los pagó con "dificultad" o "mucha dificultad".
- Solo el **26,0%** de los hogares con personas dependientes recibía servicios de cuidados a domicilio remunerados en 2024.

### 2.3.4 Brecha digital

La brecha digital sigue siendo una barrera significativa:

- El **80,1%** de las personas de 65-74 años ha usado internet en los últimos tres meses, pero esta cifra cae al **41,7%** en mayores de 75 años (INE, 2023).
- El **65%** de las personas de 65-74 años usa internet a diario (al menos 5 días/semana).
- Solo el **37,5%** de las personas entre 65-74 años ha tenido interacción con administración electrónica (INE, 2023).
- El **78%** de los mayores de 70 años enfrenta dificultades con el móvil que afectan su integración social (estudio SPC, 2024).
- El **9 de cada 10** usuarios sénior de tecnología tienen problemas para manejar sus dispositivos (SPC, 2024).
- El **76%** de las personas mayores de 80 años considera que existe ausencia de acompañamiento en su adaptación a las nuevas tecnologías (Observatorio Senior 65ymás).

**[SUGERENCIA: Insertar gráfico comparativo de uso de internet por grupos de edad (16-24, 25-44, 45-64, 65-74, 75+)]**

---

## 2.4 Objetivos generales y específicos

### Objetivo general

Diseñar y desarrollar una aplicación móvil multiplataforma que facilite la gestión integral de los servicios de cuidado de personas mayores en España, promoviendo el envejecimiento activo, reduciendo la soledad no deseada y mejorando la coordinación entre familiares, cuidadores y profesionales de la salud.

### Objetivos específicos

| Código | Objetivo | Tipo | Criterio de medición |
|--------|----------|------|---------------------|
| **OE01** | Analizar las necesidades y requisitos del colectivo de personas mayores y sus cuidadores en España mediante encuestas y entrevistas a un mínimo de 50 participantes | Medible | Informe de requisitos validado con al menos 50 respuestas |
| **OE02** | Diseñar una interfaz de usuario accesible que cumpla con las pautas WCAG 2.1 nivel AA, específicamente adaptada para personas mayores con posibles limitaciones visuales, motoras y cognitivas | Específico | Auditoría de accesibilidad con puntuación ≥90/100 |
| **OE03** | Implementar un sistema de gestión de medicamentos con recordatorios inteligentes que logre una tasa de adherencia terapéutica superior al 85% en las pruebas de usabilidad | Específico | Tasa de cumplimiento de recordatorios >85% en pruebas piloto |
| **OE04** | Desarrollar un módulo de comunicación familiar que permita la coordinación entre al menos 5 cuidadores simultáneos por persona mayor, con notificaciones en tiempo real | Funcional | Sistema funcional con soporte para 5+ cuidadores concurrentes |
| **OE05** | Integrar un sistema de teleasistencia con geolocalización y botón de emergencia que garantice un tiempo de respuesta inferior a 30 segundos | Específico | Tiempo de respuesta <30s en el 95% de las pruebas |
| **OE06** | Desplegar la aplicación en al menos dos plataformas (Android e iOS) utilizando un framework multiplataforma, con un rendimiento equivalente al 90% de una aplicación nativa | Técnico | Benchmark de rendimiento ≥90% respecto a app nativa |
| **OE07** | Realizar pruebas de usabilidad con un mínimo de 20 personas mayores de 65 años, obteniendo una puntuación SUS (System Usability Scale) superior a 68 | Medible | SUS Score >68 con n≥20 usuarios de 65+ años |
| **OE08** | Garantizar el cumplimiento del Reglamento General de Protección de Datos (RGPD) y la Ley Orgánica 3/2018 de protección de datos en el tratamiento de datos de salud | Legal | Auditoría de cumplimiento RGPD aprobada sin incidencias críticas |

---

## 2.5 Metodología empleada

### 2.5.1 Enfoque general

El proyecto combina dos marcos metodológicos complementarios:

1. **Design Thinking** para la fase de investigación y definición del problema.
2. **Scrum (Metodología Ágil)** para la fase de desarrollo e implementación.

### 2.5.2 Design Thinking

Se aplica en las fases iniciales del proyecto siguiendo sus cinco etapas:

1. **Empatizar:** Investigación con usuarios reales (personas mayores, familiares, cuidadores profesionales) mediante entrevistas semiestructuradas, observación contextual y mapas de empatía. Se realizarán al menos 15 entrevistas y 2 sesiones de observación.

2. **Definir:** Síntesis de los hallazgos para definir el problema central mediante la creación de personas (user personas), mapas de recorrido del usuario (user journey maps) y la definición del problema en formato: *"¿Cómo podemos [acción] para que [usuario] logre [resultado]?"*

3. **Idear:** Sesiones de brainstorming para generar soluciones innovadoras. Técnicas aplicadas: SCAMPER, Crazy Eights, y mapas de ideas. Se seleccionarán las 10 ideas más viables para prototipar.

4. **Prototipar:** Creación de wireframes de baja fidelidad seguidos de prototipos interactivos de media y alta fidelidad utilizando herramientas como Figma. Se desarrollarán al menos 3 iteraciones de prototipo.

5. **Testar:** Pruebas de usabilidad con los prototipos, recogiendo feedback de usuarios reales para iterar sobre el diseño. Se realizarán al menos 2 rondas de testing con 10 usuarios cada una.

### 2.5.3 Metodología Scrum

El desarrollo se organiza en **sprints de 2 semanas**, siguiendo la estructura:

| Elemento | Descripción |
|----------|-------------|
| **Product Backlog** | Lista priorizada de todas las funcionalidades (historias de usuario) |
| **Sprint Planning** | Planificación al inicio de cada sprint (2-4 horas) |
| **Daily Stand-up** | Reunión diaria de 15 minutos para sincronización |
| **Sprint Review** | Demostración de lo completado al final de cada sprint |
| **Sprint Retrospective** | Reflexión sobre el proceso y áreas de mejora |
| **Product Increment** | Versión funcional de la aplicación al final de cada sprint |

Se estima un total de **8-10 sprints** para completar el proyecto.

**[SUGERENCIA: Insertar diagrama de flujo de la metodología combinada Design Thinking + Scrum]**

**[SUGERENCIA: Incluir un ejemplo de tablero Kanban/Scrum del proyecto]**

### 2.5.4 Herramientas de gestión

- **Gestión de proyecto:** Trello / Jira / GitHub Projects
- **Control de versiones:** Git + GitHub
- **Diseño:** Figma
- **Comunicación:** Slack / Discord
- **Documentación:** Markdown + GitHub Wiki

---

# SECCIÓN 3: ANÁLISIS INICIAL

## 3.1 Descripción del problema o necesidad

### 3.1.1 Análisis del problema

El problema central se puede articular en tres dimensiones interrelacionadas:

**Dimensión demográfica:** España experimenta un envejecimiento acelerado sin precedentes. Entre 1975 y 2025, la proporción de personas de 65 y más años se ha duplicado (del 10,2% al 20,7%). A partir de 2030, con la llegada a la vejez de las generaciones del baby boom, este fenómeno se intensificará. Las provincias del noroeste y del interior (Zamora 32,2%, Ourense 32,1%, Lugo 30,1%, León 29%) ya presentan situaciones críticas de envejecimiento (CSIC - Envejecimiento en Red, 2025).

**Dimensión asistencial:** El sistema público de atención a la dependencia, aunque ha mejorado significativamente (reducción del 51% en la lista de espera desde 2020), sigue sin dar respuesta completa a la demanda. El tiempo medio de resolución de 338 días duplica el plazo legal, y cerca de 152.249 personas siguen esperando una prestación. Además, el 37,1% de los hogares con personas dependientes tiene necesidades de cuidado no cubiertas.

**Dimensión social:** La soledad no deseada afecta a 1 de cada 5 mayores de 75 años, con consecuencias graves para la salud física y mental. Estudios demuestran que la soledad crónica incrementa un 60% el uso de servicios de urgencias y está asociada a 848 muertes prematuras anuales en España.

**Dimensión tecnológica:** La brecha digital persiste como barrera. Aunque el 84,7% de los séniores tiene smartphone, el 78% de los mayores de 70 años enfrenta dificultades para manejar sus dispositivos, lo que limita su acceso a servicios digitales de salud y cuidado.

### 3.1.2 Análisis del vacío de mercado (Market Gap Analysis)

| Aspecto | Situación actual | Oportunidad |
|---------|-----------------|-------------|
| **Aplicaciones existentes** | Soluciones fragmentadas (una para medicación, otra para teleasistencia, otra para comunicación) | Plataforma integral que centralice todos los servicios |
| **Accesibilidad** | Pocas apps diseñadas específicamente para personas mayores con limitaciones | Diseño centrado en accesibilidad desde el inicio (WCAG 2.1 AA) |
| **Coordinación familiar** | Se realiza mediante WhatsApp, llamadas y papel, sin trazabilidad | Espacio colaborativo dedicado con roles y permisos |
| **Integración con SAAD** | Sin conexión directa entre aplicaciones comerciales y el sistema público | Módulo de información y gestión de trámites de dependencia |
| **Inteligencia artificial** | Uso limitado en el sector de cuidado de mayores | Asistente conversacional para personas con baja alfabetización digital |
| **Precios** | Soluciones profesionales con costes elevados (30-80€/mes) | Modelo freemium accesible para la mayoría de familias |

**Competencia directa identificada:**

| Aplicación | Funcionalidades principales | Limitaciones |
|------------|---------------------------|--------------|
| **Senium** (España) | Pastillero virtual, agenda compartida, botón emergencia, registro de constantes, muro familiar | De pago, sin integración SAAD, sin IA conversacional |
| **Plamily System** (España) | Gestión medicamentos, calendario compartido, contactos, contabilidad | Sin teleasistencia, sin monitorización de salud |
| **Gelpy** (España) | Teleasistencia 24h, geolocalización, botón salud, vinculación con farmacia | De pago, requiere suscripción, sin coordinación familiar completa |
| **Maia Care** (Internacional) | Seguimiento salud, sugerencias proactivas, recursos locales | En inglés, no adaptada al contexto español |
| **Careforth** (Internacional) | Plan cuidador, recordatorios, capacitación, círculo de atención | En inglés, no adaptada al sistema sanitario español |
| **Elli Cares** (Internacional) | Recordatorios con video, zonas seguras, IA | De pago (suscripción), no en español para España |

**[SUGERENCIA: Insertar tabla comparativa visual de las aplicaciones competidoras]**

**[SUGERENCIA: Incluir mapa de posicionamiento (ejes: precio vs. funcionalidades)]**

---

## 3.2 Usuarios objetivos / públicos involucrados

### 3.2.1 Usuario primario: Personas mayores (65+ años)

**Perfil demográfico:**
- Edad: 65-90+ años
- Distribución: 53% mujeres, 47% hombres (mayor proporción femenina en edades avanzadas)
- Situación de vida: 41,7% de las personas que viven solas tienen 65+ años
- Nivel educativo: Heterogéneo (desde analfabetismo hasta estudios superiores)
- Competencia digital: 65% usa internet diariamente (65-74 años), 41,7% (75+ años)

**Necesidades principales:**
- Recordatorios de medicación
- Contacto rápido con familiares y servicios de emergencia
- Monitorización de salud
- Compañía y reducción de la soledad
- Acceso sencillo a servicios (sin complejidad tecnológica)
- Autonomía en la vida diaria

**Barreras:**
- Limitaciones visuales, auditivas y motoras
- Baja alfabetización digital (especialmente en 75+)
- Miedo a la tecnología y a cometer errores
- Dificultad para recordar contraseñas
- Desconfianza hacia la privacidad digital

**[SUGERENCIA: Crear User Persona "María, 78 años, viuda, vive sola en Madrid"]**

### 3.2.2 Usuario secundario: Familiares cuidadores

**Perfil demográfico:**
- Edad: 40-65 años (generalmente hijos/as de las personas mayores)
- Sexo: Predominantemente mujeres (62% de los solicitantes de SAAD son mujeres)
- Situación laboral: Activos profesionalmente, con dedicación parcial al cuidado
- Competencia digital: Alta (nativos o inmigrantes digitales)

**Necesidades principales:**
- Coordinación con otros familiares cuidadores
- Monitorización remota del estado del familiar
- Gestión de citas médicas y medicación
- Acceso a información sobre el estado de salud
- Reducción de la carga mental del cuidado
- Apoyo emocional y recursos para cuidadores

**[SUGERENCIA: Crear User Persona "Carmen, 52 años, hija única, trabaja a tiempo completo"]**

### 3.2.3 Usuario terciario: Cuidadores profesionales

**Perfil demográfico:**
- Edad: 25-60 años
- Sexo: Mayoritariamente mujeres (87,9% de cuidadores no profesionales)
- Formación: Variable (desde sin formación específica hasta técnicos en cuidados)
- Situación laboral: Empleo en domicilio, residencias o centros de día

**Necesidades principales:**
- Acceso al plan de cuidados del usuario
- Registro de incidencias y evoluciones
- Comunicación con la familia y otros profesionales
- Gestión de horarios y rutas (si atienden a varios usuarios)
- Acceso a protocolos y guías de actuación

**[SUGERENCIA: Crear User Persona "Ana, 38 años, cuidadora profesional a domicilio"]**

### 3.2.4 Usuario cuaternario: Profesionales de la salud

- Médicos de atención primaria
- Enfermería comunitaria
- Trabajadores sociales
- Fisioterapeutas

**Necesidades:** Acceso a datos de monitorización del paciente, informes de evolución, comunicación con el equipo de cuidados.

**[SUGERENCIA: Incluir tabla resumen de los 4 perfiles de usuario con sus necesidades y frecuencias de uso esperadas]**

---

## 3.3 Requisitos del proyecto

### Estructura de requisitos

Los requisitos se estructuran en dos categorías:

1. **Requisitos Funcionales (RF):** Describen QUÉ debe hacer el sistema (funcionalidades, comportamientos).
2. **Requisitos No Funcionales (RNF):** Describen CÓMO debe hacerlo el sistema (rendimiento, seguridad, usabilidad, etc.).

**Formato de especificación:**

```
Identificador: RFXX / RNFXX
Título: [Nombre descriptivo]
Descripción: [Descripción clara y concisa]
Prioridad: [Alta / Media / Baja]
Usuario involucrado: [Primario / Secundario / Terciario / Cuaternario]
Criterios de aceptación: [Condiciones que deben cumplirse]
```

---

## 3.4 Requisitos funcionales

### Módulo 1: Registro y perfil de usuario

| ID | Título | Descripción | Prioridad |
|----|--------|-------------|-----------|
| **RF01** | Registro de usuario | El sistema debe permitir el registro mediante email, teléfono móvil o cuenta de Google/Apple, con validación de identidad | Alta |
| **RF02** | Creación de perfil de persona mayor | Permitir la creación de un perfil con datos personales, datos médicos básicos, contactos de emergencia y preferencias de accesibilidad | Alta |
| **RF03** | Gestión de roles | El sistema debe diferenciar roles: persona mayor, familiar cuidador, cuidador profesional y profesional de salud, con permisos específicos | Alta |
| **RF04** | Círculo de cuidados | Permitir invitar a familiares y cuidadores al círculo de cuidados de una persona mayor mediante código o enlace | Alta |

### Módulo 2: Gestión de medicación

| ID | Título | Descripción | Prioridad |
|----|--------|-------------|-----------|
| **RF05** | Registro de medicación | Permitir registrar medicamentos con nombre, dosis, frecuencia, vía de administración y horario | Alta |
| **RF06** | Recordatorios de medicación | Enviar notificaciones push, sonoras y visuales en los horarios programados para cada medicamento | Alta |
| **RF07** | Confirmación de toma | Permitir al usuario confirmar que ha tomado la medicación, con opción de posponer o registrar incidencia | Alta |
| **RF08** | Historial de medicación | Mostrar un registro cronológico de todas las tomas realizadas, omitidas y pospuestas | Media |
| **RF09** | Escaneo de recetas | Permitir fotografiar una receta médica y extraer automáticamente los datos del medicamento mediante OCR | Media |
| **RF10** | Alertas de interacciones | Avisar al usuario y a su cuidador sobre posibles interacciones entre medicamentos registrados | Media |

### Módulo 3: Monitorización de salud

| ID | Título | Descripción | Prioridad |
|----|--------|-------------|-----------|
| **RF11** | Registro de constantes vitales | Permitir registrar manualmente tensión arterial, glucosa, frecuencia cardíaca, temperatura y saturación de oxígeno | Alta |
| **RF12** | Gráficos de evolución | Mostrar gráficos interactivos de la evolución de cada constante vital a lo largo del tiempo | Media |
| **RF13** | Alertas de valores anómalos | Notificar al usuario y a los cuidadores cuando los valores registrados superen los umbrales configurados | Alta |
| **RF14** | Integración con wearables | Sincronizar datos de dispositivos wearables compatibles (relojes inteligentes, tensiómetros Bluetooth) | Baja |

### Módulo 4: Comunicación y coordinación

| ID | Título | Descripción | Prioridad |
|----|--------|-------------|-----------|
| **RF15** | Chat familiar | Sistema de mensajería interna entre los miembros del círculo de cuidados | Alta |
| **RF16** | Muro de novedades | Espacio tipo red social donde los familiares pueden publicar actualizaciones sobre el estado de la persona mayor | Media |
| **RF17** | Agenda compartida | Calendario colaborativo para citas médicas, visitas de cuidadores, actividades y recordatorios | Alta |
| **RF18** | Videollamadas | Integrar funcionalidad de videollamada simplificada con un solo toque para contactar con familiares | Media |
| **RF19** | Diario de cuidados | Registro diario de incidencias, estado de ánimo, alimentación y actividades de la persona mayor | Media |

### Módulo 5: Teleasistencia y emergencias

| ID | Título | Descripción | Prioridad |
|----|--------|-------------|-----------|
| **RF20** | Botón de emergencia | Botón siempre visible en la pantalla principal que envía una alerta a todos los contactos de emergencia con ubicación GPS | Alta |
| **RF21** | Geolocalización | Permitir a los cuidadores consultar la ubicación en tiempo real de la persona mayor (con consentimiento) | Alta |
| **RF22** | Detección de caídas | Utilizar los sensores del dispositivo para detectar posibles caídas y enviar alerta automática | Media |
| **RF23** | Tarjeta médica de emergencia | Generar una tarjeta con información médica vital accesible desde la pantalla de bloqueo | Media |
| **RF24** | Guía ante desorientación | Proporcionar instrucciones guiadas para volver a casa si la persona mayor se encuentra desorientada | Baja |

### Módulo 6: Servicios y recursos

| ID | Título | Descripción | Prioridad |
|----|--------|-------------|-----------|
| **RF25** | Directorio de servicios | Listado geolocalizado de centros de día, residencias, servicios de ayuda a domicilio, farmacias y centros de salud cercanos | Media |
| **RF26** | Información sobre SAAD | Sección informativa sobre la Ley de Dependencia, cómo solicitar valoración, grados y prestaciones disponibles | Media |
| **RF27** | Actividades sociales | Listado de actividades y eventos para personas mayores en la zona local (centros de mayores, universidades populares, etc.) | Baja |
| **RF28** | Asistente conversacional con IA | Chatbot con procesamiento de lenguaje natural que responda preguntas sobre salud, medicación, trámites y recursos, con interfaz de voz | Media |

### Módulo 7: Configuración y accesibilidad

| ID | Título | Descripción | Prioridad |
|----|--------|-------------|-----------|
| **RF29** | Modo accesible | Activar/desactivar modo de accesibilidad con tipografía ampliada (mínimo 18sp), alto contraste y navegación simplificada | Alta |
| **RF30** | Control por voz | Permitir interactuar con las funcionalidades principales mediante comandos de voz | Media |
| **RF31** | Personalización de notificaciones | Configurar tipo, frecuencia y canal de notificaciones (push, SMS, llamada) | Media |
| **RF32** | Modo sin conexión | Permitir el acceso a información esencial (medicación, contactos de emergencia, tarjeta médica) sin conexión a internet | Alta |

---

## 3.5 Requisitos no funcionales

### Accesibilidad

| ID | Título | Descripción | Prioridad |
|----|--------|-------------|-----------|
| **RNF01** | Cumplimiento WCAG 2.1 | La aplicación debe cumplir con las Pautas de Accesibilidad para el Contenido Web (WCAG) 2.1, nivel AA | Alta |
| **RNF02** | Tamaño mínimo de fuente | El tamaño de fuente base no será inferior a 18sp, con opción de ampliación hasta 24sp | Alta |
| **RNF03** | Contraste | Ratio de contraste mínimo de 4.5:1 para texto normal y 3:1 para texto grande (WCAG AA) | Alta |
| **RNF04** | Compatibilidad con lectores de pantalla | Compatible con TalkBack (Android) y VoiceOver (iOS) | Alta |

### Rendimiento

| ID | Título | Descripción | Prioridad |
|----|--------|-------------|-----------|
| **RNF05** | Tiempo de carga | La pantalla principal debe cargar en menos de 2 segundos en dispositivos de gama media | Alta |
| **RNF06** | Fluidez | La aplicación debe mantener una tasa de refresco mínima de 30 fps en todas las interacciones | Alta |
| **RNF07** | Consumo de batería | El consumo de batería en uso normal no debe superar el 5% por hora | Media |

### Seguridad y privacidad

| ID | Título | Descripción | Prioridad |
|----|--------|-------------|-----------|
| **RNF08** | Cumplimiento RGPD | El tratamiento de datos personales y de salud debe cumplir con el Reglamento (UE) 2016/679 y la LOPDGDD | Alta |
| **RNF09** | Cifrado de datos | Todos los datos en tránsito deben cifrarse mediante TLS 1.3 y los datos en reposo con AES-256 | Alta |
| **RNF10** | Autenticación segura | Soporte para autenticación biométrica (huella dactilar, reconocimiento facial) como método de acceso | Alta |
| **RNF11** | Gestión del consentimiento | El usuario debe poder dar, modificar y revocar el consentimiento para el tratamiento de sus datos de forma clara y sencilla | Alta |

### Disponibilidad y fiabilidad

| ID | Título | Descripción | Prioridad |
|----|--------|-------------|-----------|
| **RNF12** | Disponibilidad | El servicio backend debe garantizar una disponibilidad del 99,5% (menos de 44 horas de caída al año) | Alta |
| **RNF13** | Tolerancia a fallos | La aplicación debe funcionar en modo offline con las funcionalidades esenciales (medicación, emergencias, contactos) | Alta |
| **RNF14** | Copias de seguridad | Los datos del usuario deben realizarse copias de seguridad automáticas cada 24 horas | Media |

### Usabilidad

| ID | Título | Descripción | Prioridad |
|----|--------|-------------|-----------|
| **RNF15** | Puntuación SUS | La aplicación debe obtener una puntuación mínima de 68 en el System Usability Scale con usuarios mayores de 65 años | Alta |
| **RNF16** | Curva de aprendizaje | Un usuario mayor de 65 años sin experiencia previa debe poder realizar las tareas principales en menos de 10 minutos tras una breve explicación | Alta |

### Compatibilidad

| ID | Título | Descripción | Prioridad |
|----|--------|-------------|-----------|
| **RNF17** | Versiones de SO | Compatible con Android 10+ (API 29+) e iOS 14+ | Alta |
| **RNF18** | Tamaños de pantalla | Adaptable a pantallas desde 4,7 pulgadas (smartphone) hasta 12,9 pulgadas (tablet) | Media |
| **RNF19** | Idiomas | Disponible inicialmente en español (España), con arquitectura preparada para internacionalización (i18n) | Media |

---

## 3.6 Alcance y limitaciones

### 3.6.1 Alcance del proyecto (In-Scope)

**Qué SÍ incluye este proyecto:**

1. **Diseño completo de la aplicación:** Investigación de usuarios, definición de requisitos, diseño de interfaz (UI/UX), prototipos interactivos y diseño final.

2. **Desarrollo del prototipo funcional:** Implementación de las funcionalidades principales (RF01-RF20) utilizando un framework multiplataforma (Flutter o React Native).

3. **Backend básico:** API REST para gestión de usuarios, medicación, comunicaciones y datos de salud, con base de datos en la nube (Firebase o similar).

4. **Módulos funcionales implementados:**
   - Registro y gestión de perfiles
   - Gestión de medicación con recordatorios
   - Registro manual de constantes vitales
   - Chat familiar y agenda compartida
   - Botón de emergencia con geolocalización
   - Modo accesible (tipografía ampliada, alto contraste)

5. **Documentación completa:** Memoria del proyecto, manual de usuario, manual técnico y documentación de la API.

6. **Pruebas de usabilidad:** Evaluación con un grupo mínimo de 20 personas mayores de 65 años.

### 3.6.2 Fuera del alcance (Out-of-Scope)

**Qué NO incluye este proyecto:**

1. **Integración con sistemas sanitarios públicos:** No se conectará con la historia clínica electrónica del SNS ni con los sistemas del SAAD (requeriría acuerdos institucionales y homologaciones).

2. **Integración con wearables:** La sincronización con dispositivos wearables queda como requisito futuro, no se implementa en esta versión.

3. **Detección automática de caídas por hardware:** Se contempla como funcionalidad futura que requiere validación médica y certificación.

4. **Asistente de IA avanzado:** El chatbot con procesamiento de lenguaje natural se implementa en versión básica; no incluye modelos de lenguaje de gran tamaño (LLM).

5. **Videollamadas nativas:** Se integra mediante APIs de terceros (no se desarrolla un sistema propio de videoconferencia).

6. **Despliegue en tiendas de aplicaciones:** El prototipo se distribuye como APK/TestFlight, no se publica en Google Play ni App Store.

7. **Certificación como dispositivo médico:** La aplicación no se certifica como producto sanitario (requeriría cumplimiento del Reglamento UE 2017/745).

### 3.6.3 Limitaciones

| Tipo | Limitación | Impacto | Mitigación |
|------|-----------|---------|------------|
| **Temporal** | El plazo de un TFG limita el desarrollo a un prototipo funcional, no a un producto comercial completo | Alcance reducido a funcionalidades esenciales | Priorización estricta con método MoSCoW |
| **Presupuestaria** | No hay presupuesto para servicios de pago (APIs premium, servidores dedicados, licencias) | Uso de servicios gratuitos con limitaciones | Utilizar Firebase (plan Spark), APIs open source |
| **Técnica** | Experiencia limitada del desarrollador en desarrollo móvil y tecnologías de backend | Curva de aprendizaje que reduce velocidad | Documentación, tutoriales, mentoring |
| **De usuarios** | Dificultad para reclutar personas mayores para pruebas de usabilidad | Muestra reducida que puede no ser representativa | Contactar con centros de mayores, asociaciones, residencias |
| **Legal** | Los datos de salud son categoría especial según RGPD (Art. 9) | Requisitos de protección más estrictos | Cifrado extremo a extremo, minimización de datos, consentimiento explícito |
| **De infraestructura** | No se dispone de dispositivos de prueba variados | Posibles problemas de compatibilidad no detectados | Uso de emuladores + 1-2 dispositivos físicos reales |

### 3.6.4 Futuras líneas de desarrollo

1. Integración con la historia clínica electrónica del Sistema Nacional de Salud.
2. Módulo de IA avanzada para predicción de episodios de salud.
3. Integración con dispositivos IoT del hogar inteligente (domótica).
4. Plataforma web para profesionales de la salud.
5. Integración con sistemas de teleasistencia municipales y autonómicos.
6. Versión para tablet con interfaz específica.
7. Módulo de estimulación cognitiva (juegos, ejercicios de memoria).
8. Red social exclusiva para personas mayores.

**[SUGERENCIA: Incluir diagrama de roadmap con las fases futuras del proyecto]**

---

# REFERENCIAS BIBLIOGRÁFICAS PRINCIPALES

1. **INE** (2025). Censo Anual de Población 2025. Instituto Nacional de Estadística. https://www.ine.es
2. **INE** (2024). Proyección de la Población de España 2024-2074. Instituto Nacional de Estadística.
3. **INE** (2024). Movimiento Natural de la Población/Indicadores Demográficos Básicos 2024.
4. **INE** (2024). Encuesta sobre Equipamiento y Uso de Tecnologías de Información y Comunicación en los Hogares.
5. **INE** (2024). Encuesta de Condiciones de Vida (ECV) - Módulo sobre acceso a servicios.
6. **IMSERSO** (2024). Servicios Sociales dirigidos a personas mayores en España. Diciembre 2024.
7. **IMSERSO** (2024). Datos mensuales sobre cuidadores no profesionales.
8. **Ministerio de Derechos Sociales** (2026). Panel del SAAD - Primer trimestre 2026.
9. **Asociación de Directoras y Gerentes de Servicios Sociales** (2024). XXIV Dictamen del Observatorio Estatal de la Dependencia.
10. **SoledadES** (2024). Barómetro de la soledad no deseada en España 2024. Fundación ONCE y Fundación AXA.
11. **SoledadES** (2023). El coste de la soledad no deseada en España.
12. **CSIC - Envejecimiento en Red** (2025). Perfil de las Personas Mayores en España 2025.
13. **OMS** (2024). Envejecimiento y salud: Datos mundiales. Organización Mundial de la Salud.
14. **ONTSI/Red.es** (2024). El uso de las tecnologías en personas mayores.
15. **Fundación MAPFRE - Ageingnomics** (2024). Monitor de Empresas de la Economía Sénior.
16. **Fundación BBVA** (2023). Estudio sobre la percepción de la tecnología.

---

# ÍNDICE DE SUGERENCIAS PARA GRÁFICOS/INFOGRAFÍAS

| Sección | Tipo | Descripción | Fuente de datos |
|---------|------|-------------|-----------------|
| 2.2 | Gráfico de líneas | Evolución población 65+ en España (1975-2025) | INE |
| 2.2 | Pirámide poblacional | Comparativa 2025 vs 2050 | INE Proyecciones |
| 2.2 | Gráfico de barras | Esperanza de vida por sexo (2014-2024) | INE MNP |
| 2.3 | Gráfico de líneas | Evolución beneficiarios y lista de espera SAAD | IMSERSO |
| 2.3 | Infografía | Perfil del solicitante de dependencia | Panel SAAD |
| 2.3 | Gráfico circular | Distribución de soledad no deseada por edad | SoledadES |
| 2.3 | Gráfico de barras | Uso de internet por grupo de edad | INE |
| 3.1 | Tabla comparativa | Análisis de aplicaciones competidoras | Investigación propia |
| 3.1 | Mapa de posicionamiento | Precio vs. funcionalidades de apps existentes | Investigación propia |
| 3.2 | User Personas | 4 tarjetas de perfiles de usuario | Investigación propia |
| 3.2 | Tabla resumen | Necesidades y frecuencias por perfil | Investigación propia |
| Mapa territorial | Mapa de calor | Envejecimiento por provincias | INE/CSIC |

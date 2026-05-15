import { useEffect, useRef } from 'react';

// ===== SVG ICONS =====
const Icons = {
  Pill: () => (
    <svg width="28" height="28" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round">
      <path d="M10.5 20.5L17 14l-3-3-6.5 6.5a3.5 3.5 0 1 0 3 3z"/>
      <path d="M13.5 3.5L7 10l3 3 6.5-6.5a3.5 3.5 0 1 0-3-3z"/>
      <circle cx="14" cy="14" r="0.5" fill="currentColor"/>
    </svg>
  ),
  SOS: () => (
    <svg width="28" height="28" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round">
      <path d="M10.5 7.5L3 12l7.5 4.5"/>
      <path d="M13.5 7.5L21 12l-7.5 4.5"/>
      <path d="M9 12h6"/>
    </svg>
  ),
  Heart: () => (
    <svg width="28" height="28" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round">
      <path d="M19 14c1.5-1.5 2.5-3.5 2.5-5.5A4.5 4.5 0 0 0 12 5.5 4.5 4.5 0 0 0 2.5 8.5c0 2 1 4 2.5 5.5L12 22l7-8z"/>
    </svg>
  ),
  Calendar: () => (
    <svg width="28" height="28" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round">
      <rect x="3" y="4" width="18" height="18" rx="2" ry="2"/>
      <line x1="16" y1="2" x2="16" y2="6"/>
      <line x1="8" y1="2" x2="8" y2="6"/>
      <line x1="3" y1="10" x2="21" y2="10"/>
      <circle cx="12" cy="15" r="1" fill="currentColor"/>
      <circle cx="16" cy="15" r="1" fill="currentColor"/>
      <circle cx="8" cy="15" r="1" fill="currentColor"/>
    </svg>
  ),
  Family: () => (
    <svg width="28" height="28" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round">
      <path d="M17 21v-2a4 4 0 0 0-4-4H5a4 4 0 0 0-4 4v2"/>
      <circle cx="9" cy="7" r="4"/>
      <path d="M23 21v-2a4 4 0 0 0-3-3.87"/>
      <path d="M16 3.13a4 4 0 0 1 0 7.75"/>
    </svg>
  ),
  FontSize: () => (
    <svg width="28" height="28" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.8" strokeLinecap="round" strokeLinejoin="round">
      <path d="M4 7V4h16v3"/>
      <path d="M9 20h6"/>
      <path d="M12 4v16"/>
    </svg>
  ),
  Contrast: () => (
    <svg width="28" height="28" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round">
      <circle cx="12" cy="12" r="10"/>
      <path d="M12 2a10 10 0 0 1 0 20V2z" fill="currentColor" opacity="0.3"/>
    </svg>
  ),
  Touch: () => (
    <svg width="28" height="28" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round">
      <path d="M14 2a2 2 0 0 1 2 2v4l3 1.5a2 2 0 0 1 1 1.73V16a4 4 0 0 1-4 4h-5a4 4 0 0 1-2.12-.64l-3-2a1.5 1.5 0 0 1 .6-2.7l2.52-.5A2 2 0 0 1 11 16.5V6a2 2 0 0 1 2-2h1z"/>
    </svg>
  ),
  Rocket: () => (
    <svg width="28" height="28" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round">
      <path d="M4.5 16.5c-1.5 1.26-2 5-2 5s3.74-.5 5-2c.71-.84.7-2.13-.09-2.91a2.18 2.18 0 0 0-2.91-.09z"/>
      <path d="M12 15l-3-3a22 22 0 0 1 2-3.95A12.88 12.88 0 0 1 22 2c0 2.72-.78 7.5-6 11a22.35 22.35 0 0 1-4 2z"/>
      <path d="M9 12H4s.55-3.03 2-4"/>
      <path d="M12 15v5s3.03-.55 4-2"/>
    </svg>
  ),
  Check: () => (
    <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.5" strokeLinecap="round" strokeLinejoin="round">
      <polyline points="20 6 9 17 4 12"/>
    </svg>
  ),
  External: () => (
    <svg width="16" height="16" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
      <path d="M18 13v6a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V8a2 2 0 0 1 2-2h6"/>
      <polyline points="15 3 21 3 21 9"/>
      <line x1="10" y1="14" x2="21" y2="3"/>
    </svg>
  ),
  Download: () => (
    <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round">
      <path d="M21 15v4a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2v-4"/>
      <polyline points="7 10 12 15 17 10"/>
      <line x1="12" y1="15" x2="12" y2="3"/>
    </svg>
  ),
  PhoneIcon: () => (
    <svg width="28" height="28" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round">
      <path d="M22 16.92v3a2 2 0 0 1-2.18 2 19.79 19.79 0 0 1-8.63-3.07 19.5 19.5 0 0 1-6-6 19.79 19.79 0 0 1-3.07-8.67A2 2 0 0 1 4.11 2h3a2 2 0 0 1 2 1.72 12.84 12.84 0 0 0 .7 2.81 2 2 0 0 1-.45 2.11L8.09 9.91a16 16 0 0 0 6 6l1.27-1.27a2 2 0 0 1 2.11-.45 12.84 12.84 0 0 0 2.81.7A2 2 0 0 1 22 16.92z"/>
    </svg>
  ),
  SmsIcon: () => (
    <svg width="28" height="28" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round">
      <path d="M21 15a2 2 0 0 1-2 2H7l-4 4V5a2 2 0 0 1 2-2h14a2 2 0 0 1 2 2z"/>
    </svg>
  ),
  Gps: () => (
    <svg width="28" height="28" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.5" strokeLinecap="round" strokeLinejoin="round">
      <circle cx="12" cy="10" r="3"/>
      <path d="M12 2a8 8 0 0 0-8 8c0 5.4 8 12 8 12s8-6.6 8-12a8 8 0 0 0-8-8z"/>
    </svg>
  ),
};

// ===== ANIMATION HOOK =====
function useScrollReveal() {
  useEffect(() => {
    const observer = new IntersectionObserver(
      (entries) => {
        entries.forEach(entry => {
          if (entry.isIntersecting) {
            entry.target.classList.add('visible');
          }
        });
      },
      { threshold: 0.08, rootMargin: '0px 0px -40px 0px' }
    );

    const targets = document.querySelectorAll('.animate-on-scroll, .stagger-children');
    targets.forEach(t => observer.observe(t));
    return () => targets.forEach(t => observer.unobserve(t));
  }, []);
}

// ===== FEATURES DATA =====
const features = [
  {
    icon: Icons.Pill,
    color: '#007AFF',
    bg: '#E8F1FF',
    title: 'Recordatorio de medicación',
    desc: 'Programa medicamentos con dosis y frecuencia. WorkManager garantiza los avisos incluso tras reiniciar el dispositivo. Nunca olvides una toma.',
  },
  {
    icon: Icons.SOS,
    color: '#FF3B30',
    bg: '#FFE5E3',
    title: 'SOS real con ubicación',
    desc: 'Un toque envía SMS, WhatsApp y llama a tus contactos con tu localización GPS exacta. Incluye enlace a Google Maps para una respuesta rápida.',
  },
  {
    icon: Icons.Heart,
    color: '#34C759',
    bg: '#E8F8ED',
    title: 'Seguimiento de salud',
    desc: 'Registra presión arterial, glucosa y peso con gráficas interactivas. Exporta tu historial a CSV para compartirlo con tu médico.',
  },
  {
    icon: Icons.Calendar,
    color: '#AF52DE',
    bg: '#F3E8FF',
    title: 'Calendario compartido',
    desc: 'Citas médicas y eventos visibles para toda la familia. Sincronización automática con Firebase para manteneros al día.',
  },
  {
    icon: Icons.Family,
    color: '#FF9500',
    bg: '#FFF3E0',
    title: 'Círculo familiar',
    desc: 'Invita a familiares con roles personalizados (admin, cuidador, viewer). Todos al tanto del bienestar de tu ser querido en tiempo real.',
  },
];

const uxPillars = [
  { icon: Icons.FontSize, color: '#007AFF', bg: '#E8F1FF', title: 'Texto grande', desc: 'Fuente de 18sp+ para lectura sin esfuerzo' },
  { icon: Icons.Contrast, color: '#34C759', bg: '#E8F8ED', title: 'Alto contraste', desc: 'Cumple estándares WCAG AA de accesibilidad' },
  { icon: Icons.Touch, color: '#AF52DE', bg: '#F3E8FF', title: 'Botones grandes', desc: 'Objetos táctiles de 64dp mínimo' },
  { icon: Icons.Rocket, color: '#FF9500', bg: '#FFF3E0', title: 'Rendimiento', desc: 'Optimizado desde Android 8.1 (API 27)' },
];

const techStack = [
  'Kotlin', 'Jetpack Room', 'WorkManager', 'Firebase Auth',
  'Firestore', 'FCM Push', 'Google Location', 'Material 3',
  'Coroutines & Flow', 'Activity Result API', 'Android API 27+',
];

// ===== NAV LINK =====
function NavLink({ href, children }) {
  return (
    <a
      href={href}
      onClick={(e) => {
        e.preventDefault();
        const el = document.querySelector(href);
        if (el) el.scrollIntoView({ behavior: 'smooth' });
      }}
    >
      {children}
    </a>
  );
}

// ===== APP =====
export default function App() {
  useScrollReveal();

  return (
    <>
      {/* ===== HEADER ===== */}
      <header className="header">
        <div className="container">
          <a href="#" className="logo" onClick={(e) => { e.preventDefault(); window.scrollTo({ top: 0, behavior: 'smooth' }); }}>
            <span className="logo-icon">H</span>
            HelpingApp
          </a>
          <nav className="nav-links">
            <NavLink href="#features">Funcionalidades</NavLink>
            <NavLink href="#sos">SOS</NavLink>
            <NavLink href="#ux">Accesibilidad</NavLink>
            <NavLink href="#tecnologia">Tecnología</NavLink>
          </nav>
        </div>
      </header>

      {/* ===== HERO ===== */}
      <section className="hero">
        <div className="container">
          <div className="hero-content">
            <div className="hero-badge">
              <Icons.Check /> Diseñado para personas mayores
            </div>
            <h1>
              Tu aliado en el<br />
              <span>cuidado de mayores</span>
            </h1>
            <p>
              HelpingApp es una aplicación Android que ayuda a personas mayores a gestionar su salud
              con recordatorios de medicación, botón de emergencia SOS y comunicación familiar.
            </p>
            <div className="hero-buttons">
              <a href="#" className="btn btn-primary">
                <Icons.Download /> Descargar APK
              </a>
              <a href="https://github.com" target="_blank" rel="noreferrer" className="btn btn-outline">
                Ver código <Icons.External />
              </a>
            </div>
          </div>

          {/* Hero image */}
          <div className="hero-image">
            <div className="hero-image-wrapper">
              <div className="hero-image-circle">
                <img
                  src="https://images.unsplash.com/photo-1576091160399-112ba8d25d1d?w=400&h=400&fit=crop&auto=format"
                  alt="Elderly care"
                  loading="eager"
                />
              </div>
              <div className="hero-floating-card" style={{ top: '20px', right: '-20px', color: '#34C759' }}>
                <Icons.Check /> Recordatorios activos
              </div>
              <div className="hero-floating-card" style={{ bottom: '40px', left: '-30px', color: '#FF3B30' }}>
                <Icons.SOS /> SOS disponible
              </div>
            </div>
          </div>
        </div>
      </section>

      {/* ===== FEATURES ===== */}
      <section className="section" id="features">
        <div className="container">
          <div className="section-header animate-on-scroll">
            <h2>Todo lo que necesitas<br />en una sola app</h2>
            <p>Cinco funcionalidades clave pensadas para la autonomía y seguridad de las personas mayores.</p>
          </div>

          {/* Screenshot showcase */}
          <div className="showcase animate-on-scroll">
            {[
              { src: 'https://images.unsplash.com/photo-1585435557343-3b092031a831?w=300&h=533&fit=crop&auto=format', label: 'Medicación' },
              { src: 'https://images.unsplash.com/photo-1559757175-5700dde675bc?w=300&h=533&fit=crop&auto=format', label: 'SOS Emergencia' },
              { src: 'https://images.unsplash.com/photo-1571019613454-1cb2f99b2d8b?w=300&h=533&fit=crop&auto=format', label: 'Seguimiento salud' },
            ].map(item => (
              <div key={item.label} className="showcase-item">
                <img src={item.src} alt={item.label} loading="lazy" />
                <div className="showcase-label">{item.label}</div>
              </div>
            ))}
          </div>

          {/* Features grid */}
          <div className="features-grid stagger-children animate-on-scroll" style={{ marginTop: '48px' }}>
            {features.map(f => (
              <article key={f.title} className="feature-card">
                <div className="feature-icon" style={{ background: f.bg, color: f.color }}>
                  <f.icon />
                </div>
                <h3>{f.title}</h3>
                <p>{f.desc}</p>
              </article>
            ))}
          </div>
        </div>
      </section>

      {/* ===== SOS HIGHLIGHT ===== */}
      <section className="section-alt" id="sos">
        <div className="container sos-section animate-on-scroll">
          <div className="sos-card">
            <h2>🆘 Emergencia con un solo toque</h2>
            <p>
              El botón SOS envía simultáneamente SMS + WhatsApp con tu ubicación GPS a todos tus
              contactos de emergencia, e inicia una llamada. Sin configuraciones complejas.
            </p>
            <a href="#" className="btn btn-accent" onClick={(e) => { e.preventDefault(); document.getElementById('features')?.scrollIntoView({ behavior: 'smooth' }); }}>
              Saber más sobre SOS
            </a>
            <div className="sos-features">
              <div className="sos-feat"><Icons.SmsIcon /> SMS automático</div>
              <div className="sos-feat"><Icons.PhoneIcon /> Llamada directa</div>
              <div className="sos-feat"><Icons.Gps /> GPS + Maps</div>
            </div>
          </div>
        </div>
      </section>

      {/* ===== ELDERLY-FRIENDLY UX ===== */}
      <section className="section" id="ux">
        <div className="container">
          <div className="section-header animate-on-scroll">
            <h2>Diseñado para ellos</h2>
            <p>Interfaz adaptada con texto grande, alto contraste y botones fáciles de pulsar.</p>
          </div>
          <div className="ux-grid stagger-children animate-on-scroll">
            {uxPillars.map(p => (
              <div key={p.title} className="ux-card">
                <div className="ux-icon" style={{ background: p.bg, color: p.color }}>
                  <p.icon />
                </div>
                <h3>{p.title}</h3>
                <p>{p.desc}</p>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* ===== TECHNOLOGY ===== */}
      <section className="section section-alt" id="tecnologia">
        <div className="container">
          <div className="section-header animate-on-scroll">
            <h2>Tecnología utilizada</h2>
            <p>Stack moderno para una app robusta, offline-first y con sincronización en la nube.</p>
          </div>
          <div className="tech-list animate-on-scroll">
            {techStack.map(t => (
              <span key={t} className="tech-item">{t}</span>
            ))}
          </div>
        </div>
      </section>

      {/* ===== FOOTER ===== */}
      <footer className="footer">
        <div className="container">
          <div className="footer-grid">
            <div className="footer-brand">
              <a href="#" className="logo" onClick={(e) => { e.preventDefault(); window.scrollTo({ top: 0, behavior: 'smooth' }); }}>
                <span className="logo-icon">H</span>
                HelpingApp
              </a>
              <p>Asistente de salud para personas mayores. Trabajo Fin de Grado — Desarrollo de Aplicaciones Android.</p>
            </div>
            <div className="footer-col">
              <h4>App</h4>
              <a href="#features" onClick={(e) => { e.preventDefault(); document.getElementById('features')?.scrollIntoView({ behavior: 'smooth' }); }}>Funcionalidades</a>
              <a href="#sos" onClick={(e) => { e.preventDefault(); document.getElementById('sos')?.scrollIntoView({ behavior: 'smooth' }); }}>SOS</a>
              <a href="#ux" onClick={(e) => { e.preventDefault(); document.getElementById('ux')?.scrollIntoView({ behavior: 'smooth' }); }}>Accesibilidad</a>
            </div>
            <div className="footer-col">
              <h4>Recursos</h4>
              <a href="https://firebase.google.com" target="_blank" rel="noreferrer">Firebase</a>
              <a href="https://developer.android.com" target="_blank" rel="noreferrer">Android Developers</a>
              <a href="https://kotlinlang.org" target="_blank" rel="noreferrer">Kotlin</a>
            </div>
            <div className="footer-col">
              <h4>Legal</h4>
              <a href="#">Política de privacidad</a>
              <a href="#">Términos de uso</a>
            </div>
          </div>
          <div className="footer-bottom">
            <span>&copy; {new Date().getFullYear()} HelpingApp — TFG</span>
            <div className="footer-social">
              <a href="https://github.com" target="_blank" rel="noreferrer" title="GitHub">
                <svg width="20" height="20" viewBox="0 0 24 24" fill="currentColor"><path d="M12 0C5.37 0 0 5.37 0 12c0 5.31 3.435 9.795 8.205 11.385.6.105.825-.255.825-.57 0-.285-.015-1.23-.015-2.235-3.015.555-3.795-.735-4.035-1.41-.135-.345-.72-1.41-1.23-1.695-.42-.225-1.02-.78-.015-.795.945-.015 1.62.87 1.845 1.23 1.08 1.815 2.805 1.305 3.495.99.105-.78.42-1.305.765-1.605-2.67-.3-5.46-1.335-5.46-5.925 0-1.305.465-2.385 1.23-3.225-.12-.3-.54-1.53.12-3.18 0 0 1.005-.315 3.3 1.23.96-.27 1.98-.405 3-.405s2.04.135 3 .405c2.295-1.56 3.3-1.23 3.3-1.23.66 1.65.24 2.88.12 3.18.765.84 1.23 1.905 1.23 3.225 0 4.605-2.805 5.625-5.475 5.925.435.375.81 1.095.81 2.22 0 1.605-.015 2.895-.015 3.3 0 .315.225.69.825.57A12.02 12.02 0 0 0 24 12c0-6.63-5.37-12-12-12z"/></svg>
              </a>
            </div>
          </div>
        </div>
      </footer>
    </>
  );
}

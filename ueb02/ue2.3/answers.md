# Aufgabe 3: Intents

## 3.4: Explicit vs Implicit Intents

### Explicit Intent:
- Man gibt die **exakte Ziel-Klasse** an: `Intent(this, SecondActivity::class.java)`
- Android weiß genau welche Activity gestartet werden soll
- Wird für **interne Navigation** innerhalb der eigenen App verwendet
- Sicher, weil man die Kontrolle hat welche Komponente gestartet wird

### Implicit Intent:
- Man gibt eine **allgemeine Aktion** an: `Intent(Intent.ACTION_VIEW, uri)`
- Android sucht selbst eine passende App die diese Aktion ausführen kann
- Der User wählt ggf. aus mehreren Apps (z.B. Chrome vs Firefox für URLs)
- Wird für **App-übergreifende Aktionen** verwendet

### Wann ist ein Implicit Intent besser?
1. **Externe Funktionalität nutzen:** Karte anzeigen, Browser öffnen, E-Mail senden — man muss das nicht selbst implementieren
2. **Flexibilität:** Der User kann seine bevorzugte App wählen (z.B. Google Maps vs Waze)
3. **Sharing:** `ACTION_SEND` lässt den User die Ziel-App wählen (WhatsApp, Telegram, etc.)
4. **Zukunftssicher:** Wenn neue Apps installiert werden, funktionieren implicit Intents automatisch damit
5. **Weniger Code:** Man muss keine Map-Ansicht oder Browser selbst bauen

### Beispiel aus unserer App:
- **Explicit:** `Intent(this, MapActivity::class.java)` — wir wissen genau welche Activity
- **Implicit:** `Intent(ACTION_VIEW, Uri.parse("geo:0,0?q=..."))` — Android findet Google Maps (oder eine andere Karten-App)

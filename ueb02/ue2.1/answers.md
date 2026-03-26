# Aufgabe 1: Hello World

## 1.1 + 1.2: Zwei Projekte erstellen
- **Empty Activity** (Jetpack Compose): UI wird deklarativ in Kotlin-Funktionen beschrieben (`@Composable`)
- **Empty Views Activity** (XML): UI wird in XML-Layouts definiert und per `findViewById()` im Code referenziert

## 1.3: Deployment
- App per "Run" (Shift+F10) auf Emulator starten
- Für physisches Gerät: USB-Debugging in Developer Options aktivieren

## 1.4: Localization
Deutsche Übersetzung in `res/values-de/strings.xml` hinzufügen (siehe strings.xml unten)

## 1.5: AndroidManifest.xml Analyse

### Die vier App-Komponenten:
1. **Activities** — Einstiegspunkte für Benutzerinteraktion (ein Screen/Fenster)
2. **Services** — Hintergrundprozesse ohne UI (z.B. Musik abspielen, Daten synchronisieren)
3. **Broadcast Receivers** — Reagieren auf systemweite Events (z.B. Akku leer, SMS empfangen)
4. **Content Providers** — Stellen Daten für andere Apps bereit (z.B. Kontakte, Fotos)

### Entry Points:
Eine Android-App kann **mehrere Entry Points** haben — im Gegensatz zu Desktop-Programmen (die genau eine `main()`-Funktion haben). Jede Activity mit einem passenden Intent-Filter kann als Einstiegspunkt dienen. Die "Haupt-Activity" wird durch den Intent-Filter `MAIN` + `LAUNCHER` definiert.

```xml
<intent-filter>
    <action android:name="android.intent.action.MAIN" />
    <category android:name="android.intent.category.LAUNCHER" />
</intent-filter>
```

Andere Apps können jede exportierte Activity direkt per Intent starten — es gibt keinen linearen Programmfluss wie bei Desktop-Apps.

### Application Context:
Der **Application Context** ist ein globales Objekt das den gesamten Lebenszyklus der App umfasst. Er bietet Zugriff auf App-Ressourcen, Systemservices und Dateipfade. Er überlebt Activity-Wechsel und Konfigurationsänderungen.

### Klasse R:
Die **R-Klasse** wird automatisch generiert und enthält Integer-IDs für alle Ressourcen (Layouts, Strings, Drawables, etc.). Statt Dateipfade zu verwenden, referenziert man Ressourcen per `R.layout.activity_main`, `R.string.app_name`, etc.

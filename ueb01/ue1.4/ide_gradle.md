# Ü 1.4 — IDE and Build System

## 4.1 Android Studio

Android Studio runterladen von https://developer.android.com/studio. Basiert auf IntelliJ IDEA, unterstützt Kotlin nativ.

### Wichtige Fenster

- **Resource Manager** — zeigt alle Ressourcen der App (Bilder, Strings, Farben, Layouts). Man kann da neue Assets importieren und eine Vorschau sehen.
- **Project Window** — die Projektstruktur. Gibts in zwei Ansichten: Android View (vereinfacht) und Project View (echte Ordnerstruktur).
- **Structure Window** — zeigt Klassen, Methoden und Properties der aktuellen Datei. Gut zum schnell navigieren.

### Emulator

Virtuelle Geräte (AVDs) erstellt man über den Device Manager. Man wählt Gerätetyp (Phone, Tablet, etc.), Android-Version und Specs (RAM, Speicher, Auflösung).

Hardware Acceleration sollte an sein (KVM auf Linux, HAXM/Hyper-V auf Windows) — sonst ist der Emulator extrem langsam.

Extended Controls: Man kann GPS-Koordinaten simulieren, Sensoren (Gyroskop, Beschleunigung), Akku-Status, Netzwerk und mehr.

### Logcat

Zeigt Log-Nachrichten der App in Echtzeit. Die Log-Levels von unwichtig bis kritisch:

- VERBOSE (V) → DEBUG (D) → INFO (I) → WARN (W) → ERROR (E) → ASSERT (A)

Filtern kann man nach Package-Name, Log-Level, Tag oder mit Regex.

Im Code loggt man so:
```kotlin
Log.d("MainActivity", "Button clicked")
Log.e("Network", "Connection failed", exception)
```

### Device File Explorer

Damit kann man das Dateisystem vom Emulator/Gerät durchsuchen — App-Daten (/data/data/), externer Speicher (/sdcard), Datenbanken, SharedPreferences.

---

## 4.2 Gradle

### Begriffe

- **Build Type** — wie die App gebaut wird. `debug` = debuggable, nicht optimiert. `release` = optimiert, Code-Obfuscation mit R8, signiert mit Release-Key.
- **Product Flavor** — verschiedene Varianten der App. z.B. `free` und `premium` mit unterschiedlichen Features, oder `staging` und `production` mit anderen API-URLs.
- **Build Variant** — Kombination aus Build Type + Flavor. z.B. `freeDebug`, `premiumRelease`.
- **Dependencies** — externe Libraries die man in `build.gradle.kts` einbindet:
```kotlin
dependencies {
    implementation("androidx.core:core-ktx:1.12.0")
    testImplementation("junit:junit:4.13.2")
}
```

### Build-Prozess (Source → APK)

Kurzfassung: Kotlin-Code → kompilieren → in Android-Format umwandeln → mit Ressourcen zusammenpacken → signieren → fertige APK.

Im Detail:
1. **Code kompilieren** — Kotlin/Java wird zu Bytecode (.class Dateien)
2. **In DEX umwandeln** — Android versteht kein normales Java-Bytecode, braucht DEX-Format (Dalvik Executable). Dabei kann R8 den Code auch verkleinern und verschleiern
3. **Ressourcen verarbeiten** — Layouts, Bilder, Strings etc. werden zusammengeführt und in Binärformat umgewandelt
4. **Alles zusammenpacken** — DEX + Ressourcen + Manifest = APK
5. **Signieren** — APK wird mit einem Key signiert (Debug oder Release)
6. **Optimieren** — zipalign macht die APK schneller beim Laden

### Gradle-Dateien

Es gibt 3 wichtige Dateien. Einfach gesagt:

**`gradle.properties`** — Einstellungen für Gradle selbst. Wie viel RAM darf der Build benutzen, welche Optionen sind an. Fasst man selten an.
```properties
org.gradle.jvmargs=-Xmx2048m
android.useAndroidX=true
```

**`settings.gradle.kts`** — sagt Gradle welche Module es gibt. Ein Projekt kann mehrere Module haben (z.B. App + Library).
```kotlin
rootProject.name = "MyApp"
include(":app")
```

**`build.gradle.kts`** — gibt es zweimal:

Project-Level = gilt für alle Module, definiert welche Plugins es gibt:
```kotlin
plugins {
    id("com.android.application") version "8.2.0" apply false
}
```

Module-Level = die Datei die man am meisten bearbeitet. Hier steht welche Android-Version, welche Libraries, wie gebaut wird:
```kotlin
android {
    compileSdk = 34
    defaultConfig { minSdk = 24 }
    buildTypes {
        release { isMinifyEnabled = true }
    }
}
```

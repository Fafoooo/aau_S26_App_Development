# Ü 1.4 — IDE and Build System

## 4.1 Android Studio

### Installation
Android Studio wird von https://developer.android.com/studio heruntergeladen und installiert.
Es basiert auf IntelliJ IDEA und unterstützt Kotlin nativ seit Version 3.0.

### Wichtige Fenster

- **Resource Manager:** Verwaltet alle App-Ressourcen (Drawables, Strings, Colors, Layouts).
  Bietet eine visuelle Vorschau und ermöglicht das Importieren neuer Assets.

- **Project Window:** Zeigt die Projektstruktur. Zwei Ansichten:
  - *Android View*: Vereinfachte Darstellung (Module, Manifests, res, java/kotlin)
  - *Project View*: Tatsächliche Dateisystem-Struktur

- **Structure Window:** Zeigt Klassen, Methoden und Properties der aktuell geöffneten Datei.
  Ermöglicht schnelle Navigation zu Membern.

### Android Emulator

- **Hardware Acceleration:** Unter Linux via KVM, unter Windows via HAXM oder Hyper-V.
  Beschleunigt die Emulation erheblich (x86/x64 System Images statt ARM).

- **Virtual Devices (AVD):** Über den Device Manager erstellbar. Konfigurierbar:
  - Gerätetyp (Phone, Tablet, Wear OS, TV)
  - API Level / Android Version
  - RAM, Storage, Screen Resolution

- **Extended Controls:** Simulation von:
  - GPS-Koordinaten (Location)
  - Sensoren (Beschleunigung, Gyroskop, Licht)
  - Akku-Status, Netzwerkbedingungen
  - Kamera, Fingerprint, Multi-Display

### Logcat

Logcat ist das zentrale Logging-Tool für Android. Es zeigt Log-Nachrichten der App und des Systems in Echtzeit.

**Log-Level (aufsteigend):**
- `VERBOSE` (V) — Detaillierte Debug-Infos
- `DEBUG` (D) — Debug-Nachrichten
- `INFO` (I) — Allgemeine Informationen
- `WARN` (W) — Warnungen
- `ERROR` (E) — Fehler
- `ASSERT` (A) — Kritische Fehler

**Filtern:**
- Nach Package-Name (eigene App)
- Nach Log-Level (z.B. nur WARN+)
- Nach Tag (frei wählbar beim Loggen: `Log.d("MyTag", "message")`)
- Regex-basierte Suche

**Verwendung im Code:**
```kotlin
import android.util.Log
Log.d("MainActivity", "Button clicked")
Log.e("Network", "Connection failed", exception)
```

### Device File Explorer
Ermöglicht das Durchsuchen des Dateisystems eines verbundenen Geräts/Emulators.
Zugriff auf: /data/data/<package> (App-Daten), /sdcard (Externer Speicher), Datenbanken, SharedPreferences.

---

## 4.2 Gradle

### Begriffe

- **Build Type:** Definiert wie die App gebaut wird.
  - `debug`: Debuggable, kein ProGuard, Signierung mit Debug-Key
  - `release`: Optimiert, ProGuard/R8 Obfuscation, signiert mit Release-Key

- **Product Flavor:** Varianten der App mit unterschiedlichen Features/Konfigurationen.
  Beispiel: `free` und `premium` Flavor mit verschiedenen Features,
  oder `staging` und `production` mit verschiedenen API-URLs.

- **Build Variant:** Kombination aus Build Type + Product Flavor.
  Beispiel: `freeDebug`, `freeRelease`, `premiumDebug`, `premiumRelease`.

- **Dependencies:** Externe Bibliotheken, die das Projekt benötigt.
  Werden in `build.gradle.kts` deklariert:
  ```kotlin
  dependencies {
      implementation("androidx.core:core-ktx:1.12.0")
      testImplementation("junit:junit:4.13.2")
  }
  ```

### Build-Prozess (Source → APK)

1. **Kotlin/Java Compiler:** Quellcode → Bytecode (.class Dateien)
2. **Desugaring:** Konvertiert neuere Java-APIs für ältere Android-Versionen
3. **R8/D8 Compiler:** .class → DEX (Dalvik Executable) Bytecode
   - R8 zusätzlich: Code-Shrinking, Obfuscation, Optimization
4. **Resource Merging:** Alle Ressourcen (layouts, strings, drawables) zusammenführen
5. **AAPT2:** Ressourcen kompilieren und in binäres Format konvertieren
6. **Packaging:** DEX + kompilierte Ressourcen + Manifest + Native Libs → APK
7. **Signing:** APK wird mit einem Keystore signiert (Debug oder Release)
8. **Alignment:** zipalign optimiert die APK für schnelleres Laden

### Gradle-Dateien

- **`gradle.properties`:**
  Projekt-weite Einstellungen und JVM-Argumente für den Gradle-Daemon.
  ```properties
  org.gradle.jvmargs=-Xmx2048m
  android.useAndroidX=true
  kotlin.code.style=official
  ```

- **`settings.gradle.kts`:**
  Definiert welche Module zum Projekt gehören und wo Plugins/Dependencies
  aufgelöst werden (Plugin-Repositories).
  ```kotlin
  pluginManagement {
      repositories { gradlePluginPortal(); google(); mavenCentral() }
  }
  rootProject.name = "MyApp"
  include(":app")
  ```

- **`build.gradle.kts` (Project-Level):**
  Definiert Plugins und Konfigurationen, die für ALLE Module gelten.
  ```kotlin
  plugins {
      id("com.android.application") version "8.2.0" apply false
      id("org.jetbrains.kotlin.android") version "1.9.0" apply false
  }
  ```

- **`build.gradle.kts` (Module-Level, z.B. :app):**
  Spezifische Konfiguration für ein Modul: SDK-Versionen, Build Types,
  Dependencies, Compile-Optionen.
  ```kotlin
  android {
      namespace = "com.example.myapp"
      compileSdk = 34
      defaultConfig {
          minSdk = 24
          targetSdk = 34
      }
      buildTypes {
          release { isMinifyEnabled = true }
      }
  }
  ```

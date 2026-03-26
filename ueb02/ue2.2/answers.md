# Aufgabe 2: Activity Lifecycle

## 2.4: Log-Level Erklärung

| Level | Methode | Zweck |
|-------|---------|-------|
| Verbose | `Log.v()` | Sehr detaillierte Debug-Infos, niedrigste Priorität |
| Debug | `Log.d()` | Entwicklung und Troubleshooting |
| Info | `Log.i()` | Allgemeine Laufzeit-Meldungen |
| Warning | `Log.w()` | Potenzielle Probleme die keine Fehler sind |
| Error | `Log.e()` | Schwerwiegende Fehler |

**Warum Log.v() und Log.d() nicht in Production?**
- Sie erzeugen sehr viel Output und belasten die Performance
- Sie können sensible Debug-Informationen enthalten (Variablenwerte, Tokens, etc.)
- In Production will man nur Warnings und Errors sehen um echte Probleme zu finden
- Android filtert Verbose-Logs auf Release-Builds teilweise automatisch heraus

## 2.6: Lifecycle-Experimente

### 1) Home-Button drücken und App wieder öffnen:
```
-- Home drücken:
MainActivity: onPause()
MainActivity: onStop()

-- App wieder öffnen:
MainActivity: onRestart()
MainActivity: onStart()
MainActivity: onResume()
```
Die Activity wird NICHT zerstört — nur pausiert und gestoppt. Beim Zurückkommen wird onRestart() aufgerufen (nicht onCreate()).

### 2) Gerät drehen (Configuration Change):
```
MainActivity: onPause()
MainActivity: onStop()
MainActivity: onDestroy()     ← Activity wird komplett zerstört!
MainActivity: onCreate()      ← und neu erstellt mit neuem Layout
MainActivity: onStart()
MainActivity: onResume()
```
Bei einer Rotation zerstört Android die Activity und erstellt sie neu, weil sich das Layout ändern könnte (portrait vs landscape). Der savedInstanceState-Bundle wird an onCreate() übergeben um Daten zu retten.

### 3) Von Activity A zu B navigieren und per Back-Button zurück:
```
-- A → B:
MainActivity: onPause()
SecondActivity: onCreate()
SecondActivity: onStart()
SecondActivity: onResume()
MainActivity: onStop()

-- Back-Button (B → A):
SecondActivity: onPause()
MainActivity: onRestart()
MainActivity: onStart()
MainActivity: onResume()
SecondActivity: onStop()
SecondActivity: onDestroy()    ← SecondActivity wird zerstört
```
MainActivity bleibt im Hintergrund (stopped aber nicht destroyed). SecondActivity wird beim Zurück-Navigieren komplett zerstört.

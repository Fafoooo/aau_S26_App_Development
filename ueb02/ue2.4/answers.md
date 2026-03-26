# Aufgabe 4: View Binding

## 4.2: Implementierung & Reflexion

### Implementierungsschritte:
1. **Gradle:** `viewBinding = true` in `build.gradle.kts` unter `buildFeatures` aktivieren
2. **Sync:** Gradle-Sync ausführen — für jede XML-Layoutdatei wird automatisch eine Binding-Klasse generiert
   - `activity_main.xml` → `ActivityMainBinding`
   - `activity_map.xml` → `ActivityMapBinding`
3. **Inflation:** In der Activity: `binding = ActivityMainBinding.inflate(layoutInflater)`
4. **Root View:** `setContentView(binding.root)` statt `setContentView(R.layout.activity_main)`
5. **Zugriff:** Direkt per `binding.viewId` (z.B. `binding.btnSend`, `binding.etAddress`)

### View Binding vs. findViewById()

| Aspekt | findViewById() | View Binding |
|--------|---------------|-------------|
| **Type Safety** | Gibt `View` zurück, manueller Cast nötig (`as Button`). Falscher Cast → ClassCastException zur Laufzeit | Generierte Klasse hat korrekte Typen. Compile-Time Fehler bei Typproblemen |
| **Null Safety** | Wenn die ID nicht im Layout existiert → `null` → NullPointerException zur Laufzeit | Nur Views mit IDs werden generiert. Fehlende IDs → Compile-Error |
| **Performance** | Durchsucht die View-Hierarchie bei jedem Aufruf (O(n)) | Binding wird einmal erstellt, alle Referenzen gecached |
| **Boilerplate** | Viel repetitiver Code: `val btn = findViewById<Button>(R.id.btn)` für jede View | Ein `binding`-Objekt, direkte Zugriffe |

### Nachteile von View Binding:
- Generiert für JEDE XML-Datei eine Binding-Klasse → erhöht Build-Zeit leicht
- Für Compose-basierte UIs komplett irrelevant (nur für XML-Layouts)
- Kein Data-Binding: Man kann keine Variablen direkt im XML verwenden

### Wann trotzdem findViewById()?
- In sehr einfachen, einmaligen Fällen (z.B. Dialog mit einem Button)
- In Legacy-Code der nicht refactored werden soll
- Wenn man dynamisch generierte Views hat (die nicht im XML stehen)

## Bonus: Data Binding vs View Binding

| Aspekt | View Binding | Data Binding |
|--------|-------------|-------------|
| Zweck | Views referenzieren ohne findViewById | Views referenzieren + Daten direkt im XML binden |
| XML | Normales Layout-XML | `<layout>` Tag mit `<data>` Block nötig |
| Expressions | Nicht möglich | `@{user.name}` direkt im XML |
| Observability | Nein | Ja (`LiveData`, `Observable`) |
| Komplexität | Minimal | Höher (Annotation Processing, Lernkurve) |
| Build-Zeit | Kaum Einfluss | Langsamer (Annotation Processing) |

**Wann Data Binding?** Wenn man MVVM konsequent umsetzen will und Daten direkt im XML anzeigen möchte (z.B. `android:text="@{viewModel.userName}"`).

**Wann View Binding?** Für die meisten Fälle ausreichend und empfohlen — einfacher, schneller, weniger Error-prone.

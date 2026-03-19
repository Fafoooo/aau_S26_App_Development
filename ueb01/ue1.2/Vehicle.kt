import kotlin.random.Random
import kotlinx.coroutines.*          // NEU: Coroutines importieren

// Vehicle-Klasse (Ü 1.2 — Coroutines)
// AENDERUNGEN gegenueber 1.1 sind mit "NEU" markiert
//
// COROUTINE vs THREAD:
//   Thread = schwer (~1MB Speicher pro Thread), vom Betriebssystem verwaltet.
//   Coroutine = leichtgewichtig, tausende auf wenigen Threads.
//   Wenn eine Coroutine pausiert (delay), kann der Thread andere ausfuehren.
//
// SUSPEND:
//   Eine suspend-Funktion kann pausieren ohne den Thread zu blockieren.
//   Kann nur aus einer Coroutine oder anderer suspend-Funktion aufgerufen werden.

class Vehicle(
    val id: Int,
    var name: String,
    val brand: Brand,
    val workshops: MutableList<Workshop> = mutableListOf(),
    var weight: Int,
    val maxWeight: Int,
    var speed: Double = 0.0,
    val maxSpeed: Double = 200.0
) {
    // NEU: zaehlt die zurueckgelegte Distanz im Rennen
    var distanceCovered: Double = 0.0
        private set  // private set = nur die Klasse selbst darf den Wert aendern

    val isOverloaded: Boolean
        get() = weight > maxWeight

    fun accelerate(): Double {
        speed = (speed + Random.nextDouble(10.0, 50.0)).coerceAtMost(maxSpeed)
        return speed
    }

    fun brake(): Double {
        speed = (speed - Random.nextDouble(10.0, 50.0)).coerceAtLeast(0.0)
        return speed
    }

    // Identisch zu 1.1 — fuer normale Fahrten (nicht im Rennen)
    fun drive(km: Int) {
        println("--- $name startet eine Fahrt über $km km ---")
        repeat(km) { kilometer ->
            val actions = (3..5).random()
            repeat(actions) {
                val newSpeed = if (Random.nextBoolean()) accelerate() else brake()
                println("  Km ${kilometer + 1}: Speed = ${"%.1f".format(newSpeed)} km/h")
            }
        }
        println("--- Fahrt beendet ---")
    }

    // NEU: suspend-Funktion fuer das Rennen.
    // suspend = kann pausieren ohne den Thread zu blockieren.
    // delay(100) gibt den Thread frei — andere Fahrzeuge koennen in der
    // Zwischenzeit ihre raceStep() ausfuehren. Nach 100ms geht es weiter.
    suspend fun raceStep() {
        if (Random.nextBoolean()) accelerate() else brake()
        distanceCovered += speed / 3600.0 * 100  // zurueckgelegte Strecke berechnen
        delay(100)  // 100ms Pause — Thread ist frei fuer andere Coroutines
    }

    fun printInfo() {
        println("=== Vehicle Info ===")
        println("ID: $id")
        println("Name: $name")
        println("Brand: ${brand.name} (${brand.country})")
        println("Phone: ${brand.phone}")
        println("Email: ${brand.email ?: "N/A"}")
        println("Weight: $weight / $maxWeight kg")
        println("Speed: ${"%.1f".format(speed)} / ${"%.1f".format(maxSpeed)} km/h")
        println("Workshops: ${workshops.size}")
        workshops.forEach { println("  - ${it.name} (${it.postcode} ${it.city})") }
        if (isOverloaded) {
            println("!!! WARNING: Overloaded !!!")
        }
        println("====================")
    }

    fun findWorkshop(postcode: String): Workshop? {
        return workshops.find { it.postcode == postcode }
    }
}

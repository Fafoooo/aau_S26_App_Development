import kotlinx.coroutines.*
import kotlin.random.Random

/**
 * Ü 1.2 - Kotlin Coroutines
 *
 * COROUTINE vs THREAD:
 * - Ein Thread ist eine Betriebssystem-Einheit mit eigenem Stack (ca. 1MB).
 *   Threads sind "schwer" — tausende Threads belasten das System erheblich.
 * - Eine Coroutine ist eine leichtgewichtige Abstraktion, die auf Threads
 *   gemappt wird. Tausende Coroutines können auf wenigen Threads laufen.
 *   Sie werden kooperativ geplant (nicht preemptiv wie Threads).
 *
 * SUSPEND KEYWORD:
 * - `suspend` markiert eine Funktion, die ihren Ablauf unterbrechen kann,
 *   OHNE einen Thread zu blockieren. Sie kann nur aus einer Coroutine oder
 *   einer anderen suspend-Funktion aufgerufen werden.
 * - Beim Aufruf von z.B. delay() wird die Coroutine pausiert und der Thread
 *   kann andere Coroutines ausführen. Nach Ablauf der Verzögerung wird die
 *   Coroutine dort fortgesetzt, wo sie unterbrochen wurde (Continuation).
 */

// Vehicle-Klasse aus Ü 1.1 wiederverwenden (hier vereinfacht eingebettet)
data class RaceBrand(val name: String, val country: String = "Austria", val phone: String, val email: String? = null)

class RaceVehicle(
    val id: Int,
    var name: String,
    val brand: RaceBrand,
    var speed: Double = 0.0,
    val maxSpeed: Double = 200.0
) {
    var distanceCovered: Double = 0.0
        private set

    fun accelerate(): Double {
        speed = (speed + Random.nextDouble(10.0, 50.0)).coerceAtMost(maxSpeed)
        return speed
    }

    fun brake(): Double {
        speed = (speed - Random.nextDouble(10.0, 50.0)).coerceAtLeast(0.0)
        return speed
    }

    /**
     * Suspend-Funktion: Simuliert einen Renn-Schritt.
     * delay() pausiert die Coroutine ohne den Thread zu blockieren —
     * andere Fahrzeuge können in der Zwischenzeit fahren.
     */
    suspend fun raceStep() {
        if (Random.nextBoolean()) accelerate() else brake()
        distanceCovered += speed / 3600.0 * 100  // Distanz in ~100ms Intervallen
        delay(100)  // 100ms Pause — gibt den Thread für andere Coroutines frei
    }
}

/**
 * Simuliert ein Rennen zwischen mehreren Fahrzeugen mit Coroutines.
 * Jedes Fahrzeug läuft in einer eigenen Coroutine (leichtgewichtig, nicht Thread).
 * Die Coroutines laufen "gleichzeitig" (concurrent) auf wenigen Threads.
 */
suspend fun simulateRace(vehicles: List<RaceVehicle>, durationSeconds: Int = 5) {
    println("🏁 RACE START! Dauer: ${durationSeconds}s")
    println("Teilnehmer: ${vehicles.joinToString { it.name }}")
    println()

    // coroutineScope wartet bis ALLE Kind-Coroutines fertig sind
    coroutineScope {
        vehicles.forEach { vehicle ->
            // launch startet eine neue Coroutine für jedes Fahrzeug
            launch {
                val steps = durationSeconds * 10  // 10 Steps pro Sekunde (100ms delay)
                repeat(steps) {
                    vehicle.raceStep()
                }
            }
        }
    }

    // Ergebnisse sortiert nach Distanz
    println("\n🏁 RACE RESULTS:")
    println("-".repeat(50))
    vehicles.sortedByDescending { it.distanceCovered }
        .forEachIndexed { index, vehicle ->
            println("${index + 1}. ${vehicle.name} — ${"%.2f".format(vehicle.distanceCovered)} km " +
                    "(Endgeschwindigkeit: ${"%.1f".format(vehicle.speed)} km/h)")
        }
}

fun main() = runBlocking {
    val vehicles = listOf(
        RaceVehicle(1, "VW Golf", RaceBrand("VW", "Germany", "+49-555-1234"), maxSpeed = 200.0),
        RaceVehicle(2, "BMW M3", RaceBrand("BMW", "Germany", "+49-555-5678"), maxSpeed = 250.0),
        RaceVehicle(3, "Audi RS6", RaceBrand("Audi", "Germany", "+49-555-9999"), maxSpeed = 230.0),
        RaceVehicle(4, "Fiat Panda", RaceBrand("Fiat", "Italy", "+39-555-0000"), maxSpeed = 140.0)
    )

    simulateRace(vehicles, durationSeconds = 5)
}

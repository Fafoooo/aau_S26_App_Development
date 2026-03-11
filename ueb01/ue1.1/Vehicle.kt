import kotlin.random.Random

/**
 * Ü 1.1 - Simple Kotlin Example
 * Vehicle-Klasse mit Computed Properties, Custom Getters und Methoden.
 */
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
    /**
     * Computed Property: Dynamisch berechnet ob Fahrzeug überladen ist.
     * Kein gespeichertes Feld — Custom Getter wird bei jedem Zugriff ausgewertet.
     */
    val isOverloaded: Boolean
        get() = weight > maxWeight

    /**
     * Erhöht die Geschwindigkeit um einen zufälligen Wert (10..50),
     * maximal bis maxSpeed. Gibt die neue Geschwindigkeit zurück.
     */
    fun accelerate(): Double {
        speed = (speed + Random.nextDouble(10.0, 50.0)).coerceAtMost(maxSpeed)
        return speed
    }

    /**
     * Verringert die Geschwindigkeit um einen zufälligen Wert (10..50),
     * minimal bis 0.0. Gibt die neue Geschwindigkeit zurück.
     */
    fun brake(): Double {
        speed = (speed - Random.nextDouble(10.0, 50.0)).coerceAtLeast(0.0)
        return speed
    }

    /**
     * Simuliert eine Fahrt über [km] Kilometer.
     * Pro Kilometer werden 3-5 zufällige Aktionen (beschleunigen/bremsen) ausgeführt.
     * Verwendet repeat() statt klassischer for-Schleifen (Kotlin-idiomatisch).
     */
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

    /**
     * Gibt alle Fahrzeugdetails aus.
     * Verwendet String Templates und Elvis-Operator für nullable Email.
     */
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

    /**
     * Sucht eine Werkstatt anhand der Postleitzahl.
     * Gibt null zurück wenn keine gefunden wird (Kotlin Null Safety).
     */
    fun findWorkshop(postcode: String): Workshop? {
        return workshops.find { it.postcode == postcode }
    }
}

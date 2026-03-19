import kotlinx.coroutines.*          // NEU: Coroutines importieren

// Ü 1.2 - Kotlin Coroutines: Main / Testprogramm
// AENDERUNGEN gegenueber 1.1 sind mit "NEU" markiert

// NEU: runBlocking = Einstiegspunkt in die Coroutine-Welt.
// Normale main() kann kein suspend aufrufen, runBlocking macht das moeglich.
fun main() = runBlocking {
    val vw = Brand(name = "Volkswagen", country = "Germany", phone = "+49-555-1234")
    val bmw = Brand(name = "BMW", country = "Germany", phone = "+49-555-5678", email = "info@bmw.de")
    val audi = Brand(name = "Audi", country = "Germany", phone = "+49-555-9999")
    val fiat = Brand(name = "Fiat", country = "Italy", phone = "+39-555-0000")

    val workshopVienna = Workshop(
        name = "AutoFit Wien", country = "Austria", city = "Wien",
        street = "Mariahilfer Str. 100", phone = "+43-1-12345", postcode = "1060"
    )

    // Fahrzeuge erstellen — gleich wie in 1.1
    val golf = Vehicle(id = 1, name = "VW Golf", brand = vw, weight = 1400, maxWeight = 1500)
    val bmwM3 = Vehicle(id = 2, name = "BMW M3", brand = bmw, weight = 1600, maxWeight = 2000, maxSpeed = 250.0)
    val rs6 = Vehicle(id = 3, name = "Audi RS6", brand = audi, weight = 2000, maxWeight = 2500, maxSpeed = 230.0)
    val panda = Vehicle(id = 4, name = "Fiat Panda", brand = fiat, weight = 1000, maxWeight = 1200, maxSpeed = 140.0)

    golf.workshops.add(workshopVienna)

    val vehicles = listOf(golf, bmwM3, rs6, panda)

    // NEU: RENNSIMULATION MIT COROUTINES 
    // In 1.1 fuhr jedes Auto nacheinander (forEach → drive).
    // Hier fahren alle GLEICHZEITIG — das ist der Punkt von Coroutines.

    val durationSeconds = 5
    println("RACE START! Dauer: ${durationSeconds}s")
    println("Teilnehmer: ${vehicles.joinToString { it.name }}\n")

    // coroutineScope = wartet bis ALLE Coroutines darin fertig sind
    coroutineScope {
        vehicles.forEach { vehicle ->
            // NEU: launch = startet eine neue Coroutine pro Fahrzeug.
            // Alle laufen gleichzeitig (concurrent), nicht nacheinander.
            launch {
                repeat(durationSeconds * 10) {  // 10 Schritte pro Sekunde (je 100ms delay)
                    vehicle.raceStep()
                }
            }
        }
    }
    // Hier angekommen = alle Coroutines sind fertig (Rennen vorbei)

    // Ergebnis: sortiert nach zurueckgelegter Distanz
    println("\nRACE RESULTS:")
    println("-".repeat(50))
    vehicles.sortedByDescending { it.distanceCovered }
        .forEachIndexed { index, vehicle ->
            println("${index + 1}. ${vehicle.name} — ${"%.2f".format(vehicle.distanceCovered)} km " +
                    "(Speed: ${"%.1f".format(vehicle.speed)} km/h)")
        }
}

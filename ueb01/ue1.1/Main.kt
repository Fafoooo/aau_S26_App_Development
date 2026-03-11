/**
 * Ü 1.1 - Simple Kotlin Example: Main / Testprogramm
 *
 * Erstellt Brand, Workshop und Vehicle Objekte und testet alle Funktionen.
 */
fun main() {
    // Brands erstellen (mit Default-Werten und Named Arguments)
    val vw = Brand(name = "Volkswagen", country = "Germany", phone = "+49-555-1234")
    val bmw = Brand(name = "BMW", country = "Germany", phone = "+49-555-5678", email = "info@bmw.de")

    // Workshops erstellen (String- und Int-Konstruktor testen)
    val workshopVienna = Workshop(
        name = "AutoFit Wien",
        country = "Austria",
        city = "Wien",
        street = "Mariahilfer Str. 100",
        phone = "+43-1-12345",
        postcode = "1060"               // String-Konstruktor
    )
    val workshopKlagenfurt = Workshop(
        name = "KFZ Klagenfurt",
        country = "Austria",
        city = "Klagenfurt",
        street = "Villacher Str. 50",
        phone = "+43-463-12345",
        postcode = 9020                  // Int-Konstruktor → wird intern zu "9020"
    )
    val workshopGraz = Workshop(
        name = "Werkstatt Graz",
        country = "Austria",
        city = "Graz",
        street = "Hauptplatz 1",
        phone = "+43-316-99999",
        postcode = "8010"
    )

    // Vehicles erstellen (Named Arguments für Lesbarkeit)
    val golf = Vehicle(
        id = 1,
        name = "Golf",
        brand = vw,
        weight = 1400,
        maxWeight = 1500
    )

    // Überladenes Fahrzeug zum Testen des Custom Getters
    val overloadedBmw = Vehicle(
        id = 2,
        name = "BMW X5 (überladen)",
        brand = bmw,
        weight = 3000,                  // Absichtlich > maxWeight
        maxWeight = 2500,
        maxSpeed = 250.0
    )

    // Workshops hinzufügen
    golf.workshops.addAll(listOf(workshopVienna, workshopKlagenfurt))
    overloadedBmw.workshops.add(workshopGraz)

    // Alle Fahrzeuge in einer Liste
    val vehicles = listOf(golf, overloadedBmw)

    // Simulation: Info → Drive → Info
    vehicles.forEach { vehicle ->
        println("\n${"=".repeat(50)}")
        println("BEFORE DRIVE:")
        vehicle.printInfo()

        vehicle.drive(10)

        println("\nAFTER DRIVE:")
        vehicle.printInfo()
    }

    // Workshop-Suche testen
    println("\n--- Workshop-Suche ---")
    val found = golf.findWorkshop("9020")
    println("Workshop mit PLZ 9020: ${found?.name ?: "nicht gefunden"}")

    val notFound = golf.findWorkshop("5020")
    println("Workshop mit PLZ 5020: ${notFound?.name ?: "nicht gefunden"}")
}

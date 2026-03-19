// Ü 1.3 - Lambda- und Scope Functions
//
// Scope Functions:
//   let   | it   | gibt Lambda-Result | Null-Checks
//   run   | this | gibt Lambda-Result | Konfiguration + Berechnung
//   with  | this | gibt Lambda-Result | mehrere Ops auf einem Objekt
//   apply | this | gibt Objekt selbst | Objekt-Initialisierung
//   also  | it   | gibt Objekt selbst | Side-Effects (Logging)

//data class contact 
data class Contact(
    var name: String,
    var email: String? = null,
    var phone: String? = null,
    var age: Int? = null,
    var address: String? = null
)

// filter{} = nur Einträge behalten wo Bedingung true ist
fun filterWithEmail(contacts: List<Contact>): List<Contact> {
    return contacts.filter { it.email != null }
}

// map{} = jeden Eintrag in etwas anderes umwandeln
fun mapToNameEmail(contacts: List<Contact>): List<String> {
    return contacts
        .filter { it.email != null }
        .map { "${it.name} <${it.email}>" }
}

// mapNotNull{} = wie map, überspringt null-Werte
fun averageAge(contacts: List<Contact>): Double {
    return contacts.mapNotNull { it.age }.average()
}

// Lambda als Parameter → Suchkriterium von außen mitgeben
fun findByName(contacts: List<Contact>, predicate: (Contact) -> Boolean): Contact? {
    return contacts.find(predicate)
}

fun main() {
    // Initialize Data

    // apply: Properties direkt setzbar im Block (this = Contact, unsichtbar).
    //   Gibt immer das Objekt zurück → alice ist der fertige Contact.
    val alice = Contact(name = "Alice").apply {
        email = "alice@example.com"
        phone = "+43-664-1111111"
        age = 25
        address = "Hauptplatz 1, 9020 Klagenfurt"
    }
    val bob = Contact(name = "Bob").apply {
        email = "bob@work.at"
        age = 30
    }

    // also: Objekt ist 'it' (nicht this). Gut für Side-Effects wie println.
    //   Gibt auch das Objekt zurück. it.name weil bei also das Objekt 'it' heißt.
    val charlie = Contact(name = "Charlie", age = 22).also {
        println("Kontakt erstellt: ${it.name} (kein Email)")
    }

    val diana = Contact(name = "Diana").apply {
        email = "diana@uni.at"
        phone = "+43-660-4444444"
        age = 28
        address = "Villacher Str. 10, 9020 Klagenfurt"
    }

    // run: wie apply (this = Contact), aber gibt den LETZTEN Wert im Block zurück.
    //   Ohne 'this' am Ende wäre eve = der phone-String statt der Contact.
    val eve = Contact(name = "Eve").run {
        email = "eve@startup.io"
        age = 35
        phone = "+43-650-5555555"
        this  // gibt den Contact zurück, nicht den letzten Wert
    }




    val contacts = listOf(alice, bob, charlie, diana, eve)

    println("\n--- Alle Kontakte ---")
    contacts.forEach { println("  ${it.name}, Email: ${it.email ?: "N/A"}, Alter: ${it.age ?: "?"}") }

    println("\n--- Kontakte mit Email ---")
    filterWithEmail(contacts).forEach { println("  ${it.name}: ${it.email}") }

    println("\n--- Name <Email> Liste ---")
    mapToNameEmail(contacts).forEach { println("  $it") }

    println("\n--- Durchschnittsalter ---")
    println("  Durchschnitt: ${"%.1f".format(averageAge(contacts))} Jahre")

    // let: nur ausfuehren wenn nicht null (?.let), sonst Elvis
    println("\n--- let: Nur wenn Email vorhanden ---")
    charlie.email?.let { println("  Charlie's Email: $it") }
        ?: println("  Charlie hat keine Email-Adresse")
    alice.email?.let { println("  Alice's Email: $it") }

    // with: 'this' = diana, Properties direkt ohne diana. davor
    println("\n--- with ---")
    with(diana) {
        println("  Name: $name")
        println("  Email: $email")
        println("  Telefon: ${phone ?: "N/A"}")
        println("  Adresse: ${address ?: "N/A"}")
    }

    // findByName mit verschiedenen Lambdas als Suchkriterium
    println("\n--- findByName ---")
    val found = findByName(contacts) { it.name == "Bob" }
    found?.let { println("  Gefunden: ${it.name}, Alter: ${it.age}") }
        ?: println("  Nicht gefunden")

    val over25 = findByName(contacts) { (it.age ?: 0) > 25 }
    println("  Erster ueber 25: ${over25?.name ?: "keiner"}")
}

/**
 * Ü 1.3 - Lambda- und Scope Functions
 *
 * Programm zur Verarbeitung einer Kontaktliste mit Lambda-Funktionen
 * und Scope-Functions.
 *
 * ┌──────────┬───────────────────┬────────────────┬──────────────────────────────────┐
 * │ Function │ Context (this/it) │ Return Value   │ Typische Anwendung               │
 * ├──────────┼───────────────────┼────────────────┼──────────────────────────────────┤
 * │ let      │ it (Lambda-Arg)   │ Lambda-Result  │ Null-Check + Transformation      │
 * │ run      │ this (Receiver)   │ Lambda-Result  │ Objekt-Konfiguration + Berechnung│
 * │ with     │ this (Receiver)   │ Lambda-Result  │ Mehrere Ops auf einem Objekt     │
 * │ apply    │ this (Receiver)   │ Context-Objekt │ Objekt-Initialisierung           │
 * │ also     │ it (Lambda-Arg)   │ Context-Objekt │ Side-Effects (Logging etc.)      │
 * └──────────┴───────────────────┴────────────────┴──────────────────────────────────┘
 */

/**
 * Data class Contact mit nullable Properties für Email, Phone und Age.
 */
data class Contact(
    var name: String,
    var email: String? = null,
    var phone: String? = null,
    var age: Int? = null,
    var address: String? = null
)

/**
 * Filtert Kontakte mit gültiger (nicht-null) Email-Adresse.
 * Verwendet die Lambda-Funktion filter{}.
 */
fun filterWithEmail(contacts: List<Contact>): List<Contact> {
    return contacts.filter { it.email != null }
}

/**
 * Erstellt eine Liste von "Name <email>" Strings.
 * Verwendet map{} zur Transformation der Kontaktliste.
 */
fun mapToNameEmail(contacts: List<Contact>): List<String> {
    return contacts
        .filter { it.email != null }
        .map { "${it.name} <${it.email}>" }
}

/**
 * Berechnet das Durchschnittsalter aller Kontakte (mit bekanntem Alter).
 * Verwendet mapNotNull{} und average() — rein funktionaler Stil.
 */
fun averageAge(contacts: List<Contact>): Double {
    return contacts
        .mapNotNull { it.age }       // Nur nicht-null Alter, Auto-Unboxing
        .average()                     // Durchschnitt berechnen
}

/**
 * Sucht einen Kontakt anhand des Namens.
 * Akzeptiert eine Lambda-Funktion als Suchkriterium → flexibel erweiterbar.
 */
fun findByName(contacts: List<Contact>, predicate: (Contact) -> Boolean): Contact? {
    return contacts.find(predicate)
}

fun main() {
    // ============================================================
    // INITIALISIERUNG mit Scope-Functions (apply, also)
    // ============================================================

    // apply: Konfiguriert ein Objekt direkt nach der Erstellung.
    // Innerhalb des Blocks ist 'this' der Contact → Properties direkt setzbar.
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

    // also: Führt Side-Effects aus (z.B. Logging) und gibt das Objekt zurück.
    val charlie = Contact(name = "Charlie", age = 22).also {
        println("Kontakt erstellt: ${it.name} (kein Email)")
    }

    val diana = Contact(name = "Diana").apply {
        email = "diana@uni.at"
        phone = "+43-660-4444444"
        age = 28
        address = "Villacher Str. 10, 9020 Klagenfurt"
    }

    // run: Konfiguriert Objekt UND berechnet/returned etwas.
    val eve = Contact(name = "Eve").run {
        email = "eve@startup.io"
        age = 35
        phone = "+43-650-5555555"
        this  // Gibt den konfigurierten Contact zurück
    }

    val contacts = listOf(alice, bob, charlie, diana, eve)

    // ============================================================
    // VERARBEITUNG mit Lambda-Funktionen
    // ============================================================

    println("\n--- Alle Kontakte ---")
    contacts.forEach { println("  ${it.name}, Email: ${it.email ?: "N/A"}, Alter: ${it.age ?: "?"}") }

    // Filtering: Nur Kontakte mit Email
    println("\n--- Kontakte mit Email ---")
    val withEmail = filterWithEmail(contacts)
    withEmail.forEach { println("  ${it.name}: ${it.email}") }

    // Mapping: Name + Email als Strings
    println("\n--- Name <Email> Liste ---")
    val nameEmails = mapToNameEmail(contacts)
    nameEmails.forEach { println("  $it") }

    // Accumulation: Durchschnittsalter
    println("\n--- Durchschnittsalter ---")
    val avgAge = averageAge(contacts)
    println("  Durchschnitt: ${"%.1f".format(avgAge)} Jahre")

    // ============================================================
    // SCOPE FUNCTIONS im Einsatz
    // ============================================================

    println("\n--- let: Nur wenn Email vorhanden ---")
    // let: Wird nur ausgeführt wenn der Wert nicht null ist (?.let).
    // 'it' ist der nicht-null Wert innerhalb des Blocks.
    charlie.email?.let { email ->
        println("  Charlie's Email: $email")
    } ?: println("  Charlie hat keine Email-Adresse")

    alice.email?.let { email ->
        println("  Alice's Email: $email")
    }

    println("\n--- with: Mehrere Operationen auf einem Objekt ---")
    // with: Führt mehrere Operationen auf einem Objekt aus,
    // ohne es jedes Mal referenzieren zu müssen. 'this' ist das Objekt.
    with(diana) {
        println("  Name: $name")
        println("  Email: $email")
        println("  Telefon: ${phone ?: "N/A"}")
        println("  Adresse: ${address ?: "N/A"}")
        println("  Ist volljährig: ${(age ?: 0) >= 18}")
    }

    println("\n--- findByName (Lambda-basierte Suche) ---")
    // Lambda als Suchkriterium: flexibel, wiederverwendbar
    val found = findByName(contacts) { it.name == "Bob" }
    found?.let {
        println("  Gefunden: ${it.name}, Alter: ${it.age}")
    } ?: println("  Nicht gefunden")

    // Suche mit komplexerem Kriterium (auch eine Lambda)
    val over25 = findByName(contacts) { (it.age ?: 0) > 25 }
    println("  Erster Kontakt über 25: ${over25?.name ?: "keiner"}")
}

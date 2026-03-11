/**
 * Ü 1.1 - Simple Kotlin Example
 * Data class Workshop mit sekundärem Konstruktor für Int-Postleitzahl.
 * Postcode wird intern immer als String gespeichert.
 */
data class Workshop(
    val name: String,
    val country: String,
    val city: String,
    val street: String,
    val phone: String,
    val postcode: String
) {
    /** Sekundärer Konstruktor: akzeptiert postcode als Int */
    constructor(
        name: String,
        country: String,
        city: String,
        street: String,
        phone: String,
        postcode: Int
    ) : this(name, country, city, street, phone, postcode.toString())
}

// Ü 1.2 - Kotlin Coroutines
// Identisch zu 1.1 — keine Aenderungen noetig
data class Workshop(
    val name: String,
    val country: String,
    val city: String,
    val street: String,
    val phone: String,
    val postcode: String
) {
    constructor(
        name: String,
        country: String,
        city: String,
        street: String,
        phone: String,
        postcode: Int
    ) : this(name, country, city, street, phone, postcode.toString())
}

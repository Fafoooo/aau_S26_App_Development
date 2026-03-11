/**
 * Ü 1.1 - Simple Kotlin Example
 * Data class Brand mit Default-Werten und Nullable Email.
 */
data class Brand(
    val name: String,
    val country: String = "Austria",
    val phone: String,
    val email: String? = null
)

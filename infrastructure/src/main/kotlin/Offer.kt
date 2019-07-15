data class Offer(val id: String, val name: String) : Comparable<Offer> {
    override fun compareTo(other: Offer): Int =
        (this.name.compareTo(other.name)).takeUnless { it == 0 } ?: this.id.compareTo(other.id)
}
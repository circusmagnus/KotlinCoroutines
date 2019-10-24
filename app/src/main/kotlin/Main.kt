import kotlinx.coroutines.cancel
import kotlin.random.Random

fun main() {
    val playground = Playground(OffersRepository(), SellersRepository(), ConsoleDisplay())

    val availableNames = listOf(
        "Krzesło",
        "Łóżko",
        "Telefon",
        "Rower",
        "Spodnie",
        "Zegarek",
        "Fortepian",
        "Mandarynka",
        "Wół",
        "Fajka"
    )

    val queries = generateSequence { Random.nextInt(0, availableNames.lastIndex) }
        .take(100)
        .map { availableNames[it] }
        .toList()

    playground.showSortedOffers(queries)
    playground.cancel()
}
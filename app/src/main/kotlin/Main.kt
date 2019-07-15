import kotlinx.coroutines.cancel
import kotlin.random.Random

fun main() {
    val playground = Playground(OffersRepository(), SellersRepository(), ConsoleDisplay())
    playground.startAnimation()

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
        .take(10)
        .map { availableNames[it] }
        .toList()

    playground.showSortedOffers(queries)
    playground.cancel()
}
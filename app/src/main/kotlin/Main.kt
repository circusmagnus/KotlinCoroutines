import kotlinx.coroutines.*
import kotlin.random.Random

fun main() {
    runBlocking {
        val playgroundScope = this + Job(parent = coroutineContext[Job])
        val playground = Playground(OffersRepository(), SellersRepository(), ConsoleDisplay(), playgroundScope)

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
            .take(10_000_000)
            .map { availableNames[it] }
            .toList()

        playground.startAnimation()
        playground.showSellersForOffers(queries)
        delay(10_000)
        playground.cancel()
        println("Canceled!")
    }
}
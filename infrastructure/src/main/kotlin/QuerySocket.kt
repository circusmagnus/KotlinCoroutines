import kotlin.random.Random

class QuerySocket {

    fun setListener(onQuery: (String) -> Unit) {
        Thread {
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

            queries.forEach { onQuery(it) }
        }.start()
    }
}
import kotlinx.coroutines.*

class Playground(
    private val offersRepository: BlockingOffersRepository,
    private val display: Display
) {

    fun run() {
        runBlocking {
            val anim = launch {
                while (true) {
                    delay(200); display.showNewLine(".")
                }
            }
            val offers = getOffers()
            anim.cancelAndJoin()
            display.showNewLine("Done. Offers: $offers")
        }
    }

    private suspend fun getOffers() = withContext(Dispatchers.IO) { offersRepository.getOffersBlocking("Krzesło") }
}
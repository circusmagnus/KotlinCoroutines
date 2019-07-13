import kotlinx.coroutines.*

class Playground(
    private val offersRepository: BlockingOffersRepository,
    private val display: Display
) {

    fun run() {
        runBlocking {
            val fetchOffers = async(Dispatchers.IO) { offersRepository.getOffersBlocking("Krzesło") }

            val anim = launch {
                while (isActive) {
                    delay(200); display.showNewLine(".")
                }
            }
            val offers = fetchOffers.await()
            anim.cancelAndJoin()
            display.showNewLine("Done. Offers: $offers")
        }
    }
}
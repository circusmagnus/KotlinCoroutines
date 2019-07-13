import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class Playground(
    private val offersRepository: BlockingOffersRepository,
    private val display: Display
) {

    fun run() {
        runBlocking {
            repeat(3) { delay(200); display.showNewLine(".") }
            val offers = withContext(Dispatchers.IO) { offersRepository.getOffersBlocking("Krzesło") }
            display.showNewLine("Done. Offers: $offers")
            repeat(3) { delay(200); display.showNewLine(".") }
        }
    }
}
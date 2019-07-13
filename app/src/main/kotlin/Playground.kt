import kotlinx.coroutines.*

class Playground(
    private val offersRepository: BlockingOffersRepository,
    private val display: Display
) {

    fun run() {
        runBlocking {
            val anim = launch {
                while (isActive) {
                    delay(200); display.showNewLine(".")
                }
            }
            launch(Dispatchers.IO) {
                val result = offersRepository.getOffersBlocking("Krzesło")
                anim.cancel()
                display.showNewLine("Done. Offers: $result")
            }
        }

//        repeat(3) { Thread.sleep(200); display.showNewLine(".") }
//        val result = offersRepository.getOffersBlocking("Krzesło")
//        display.showNewLine("Done. Offers: $result")
//        repeat(3) { Thread.sleep(200); display.showNewLine(".") }
    }

}
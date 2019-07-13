class Playground(
    private val offersRepository: BlockingOffersRepository,
    private val display: Display
) {

    fun run() {
        repeat(3) { Thread.sleep(200); display.showNewLine(".") }
        val result = offersRepository.getOffersBlocking("Krzesło")
        display.showNewLine("Done. Offers: $result")
        repeat(3) { Thread.sleep(200); display.showNewLine(".") }
    }

}
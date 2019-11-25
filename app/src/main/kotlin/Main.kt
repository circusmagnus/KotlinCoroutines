import kotlinx.coroutines.cancel

fun main() {
    val playground = Playground(OffersRepository(), QuerySocket(), ConsoleDisplay())
    playground.startAnimation()

    playground.startListening()

    Thread.sleep(1000)
    playground.cancel()
    Thread.sleep(3000)
}
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.consumeEach
import kotlin.coroutines.CoroutineContext

class Playground(
    private val offersRepository: OffersRepository,
    private val querySocket: QuerySocket,
    private val display: Display
) : CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Default + SupervisorJob()

    private val displayActor: SendChannel<String> = DisplayActor()

    private fun DisplayActor(): SendChannel<String> {
        val mailBox = Channel<String>()
        launch {
            mailBox.consumeEach { message -> display.showNewLine(message) }
        }
        return mailBox
    }

    fun startListening() {
        launch {
            querySocket.setListener { query -> }
        }
    }

    fun startAnimation() {
        launch { runDotAnim() }
    }

    private suspend fun runDotAnim() = coroutineScope {
        while (isActive) {
            delay(200)
            displayActor.send(".")
        }
    }

    private suspend fun getOffers(query: String): List<Offer> = withContext(Dispatchers.IO) {
        offersRepository.getOffersBlocking(query)
    }

    suspend fun showSortedOffers(queries: List<String>) = coroutineScope {

        val queriesChannel = Channel<String>()
        val unsortedOffersChannel = Channel<List<Offer>>()

        launch {
            queries.forEach { queriesChannel.send(it) }
            queriesChannel.close()
        }

        launch {
            coroutineScope {
                repeat(4) {
                    launch(Dispatchers.IO) {
                        for (query in queriesChannel) unsortedOffersChannel.send(getOffers(query))
                    }
                }
            }
            unsortedOffersChannel.close()
        }

        repeat(4) {
            launch(Dispatchers.Default) {
                for (unsorted in unsortedOffersChannel) {
                    unsorted.sorted().forEach { sorted -> displayActor.send(sorted.toString()) }
                }
            }
        }
    }

}
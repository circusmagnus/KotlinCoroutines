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

    fun showSortedOffers(queryList: List<String>) {
        runBlocking {
            produceQueries(queryList)
                .let { queries -> produceOffersFromDb(queries) }
                .let { offers -> sortAndDisplay(offers) }
        }
    }

    private fun CoroutineScope.produceQueries(queries: List<String>): ReceiveChannel<String> {
        val output = Channel<String>()
        launch {
            for (query in queries) output.send(query)
            output.close()
        }
        return output
    }

    private fun CoroutineScope.produceOffersFromDb(queriesChannel: ReceiveChannel<String>): ReceiveChannel<List<Offer>> {
        val output = Channel<List<Offer>>()
        launch {
            coroutineScope {
                repeat(4) {
                    launch { for (query in queriesChannel) output.send(getOffers(query)) }
                }
            }
            output.close()
        }
        return output
    }

    private fun CoroutineScope.sortAndDisplay(unsortedOffers: ReceiveChannel<List<Offer>>) = repeat(4) {
        launch {
            for (offers in unsortedOffers) {
                val sorted = offers.sorted()
                display.showNewLine("just sorted ${sorted.size} offers in $this")
            }
        }
    }
}
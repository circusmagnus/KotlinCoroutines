fun main(){
    val playground = Playground(BlockingOffersRepository(), ConsoleDisplay())
    playground.run()
}
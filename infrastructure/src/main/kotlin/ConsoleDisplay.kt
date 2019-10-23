interface Display {
    fun show(what: String)
    fun showNewLine(what: String)
}

class ConsoleDisplay : Display {

    private var dotsShown = 0
    override fun show(what: String) {
        if (what == ".") {
            dotsShown++
            require(dotsShown < 20) { "Too many dots shown" }
        }
        print(what)
    }

    override fun showNewLine(what: String) {
        if (what == ".") {
            dotsShown++
            require(dotsShown < 20) { "Too many dots shown" }
        }
        println(what)
    }
}
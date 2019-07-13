interface Display {
    fun show(what: String)
    fun showNewLine(what: String)
}

class ConsoleDisplay : Display {
    override fun show(what: String) {
        print(what)
    }

    override fun showNewLine(what: String) {
        println(what)
    }
}
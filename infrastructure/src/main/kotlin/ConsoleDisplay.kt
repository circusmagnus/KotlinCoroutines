interface Display {
    fun show(what: String)
    fun showNewLine(what: String)
}

class ConsoleDisplay : Display {

    private var lineNumber = 0

    override fun show(what: String) {
        print("${++lineNumber}: $what")
    }

    override fun showNewLine(what: String) {
        println("${++lineNumber}: $what")
    }
}
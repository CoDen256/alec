package coden.display.displays

interface MessageDisplay: ErrorDisplay {
    fun displayPrompt(message: String)
    fun displayMessage(message: String)
}
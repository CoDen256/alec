package coden.alec.app.views

interface MessageDisplay: ErrorDisplay {
    fun displayPrompt(message: String)
    fun displayMessage(message: String)
}
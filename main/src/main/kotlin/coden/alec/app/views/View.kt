package coden.alec.app.views

interface View: ErrorView {
    fun displayPrompt(message: String)
    fun displayMessage(message: String)
}
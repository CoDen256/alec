package coden.alec.app.views

interface View {
    fun displayPrompt(message: String)
    fun displayMessage(message: String)
    fun displayMenu(message: String)
    fun displayError(message: String)
}
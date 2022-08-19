package coden.alec.app.states

import coden.alec.bot.messages.MessageResource
import coden.alec.bot.presenter.View

fun interface Action {
    fun execute(view: View, messages: MessageResource)
}


object DisplayStartMessage: Action {
    override fun execute(view: View, messages: MessageResource) {
        view.displayMessage(messages.startMessage)
    }
}
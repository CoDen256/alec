package coden.alec.app.states

import coden.alec.bot.messages.MessageResource
import coden.alec.bot.presenter.View
import coden.alec.interactors.definer.scale.ListScalesRequest
import coden.alec.interactors.definer.scale.ListScalesResponse

fun interface Action {
    fun execute(useCaseFactory: UseCaseFactory, view: View, messages: MessageResource)
}


object DisplayHelpMessage: Action {
    override fun execute(useCaseFactory: UseCaseFactory, view: View, messages: MessageResource) {
        view.displayMessage(messages.startMessage)
    }
}

object ListScales: Action {
    override fun execute(useCaseFactory: UseCaseFactory, view: View, messages: MessageResource) {
        val listScales = useCaseFactory.listScales()
        val response = listScales.execute(ListScalesRequest()) as ListScalesResponse
        response.scales.onSuccess {
            if (it.isEmpty()){
                view.displayMessage(messages.listScalesEmptyMessage)
            }else {
                view.displayMessage(messages.listScalesMessage + it)
            }
        }.onFailure {
            view.displayError("${messages.errorMessage} ${it.message}")
        }
    }
}
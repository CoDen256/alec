package coden.alec.app.states

import coden.alec.bot.messages.MessageResource
import coden.alec.bot.presenter.View
import coden.alec.interactors.definer.scale.CreateScaleRequest
import coden.alec.interactors.definer.scale.CreateScaleResponse
import coden.alec.interactors.definer.scale.ListScalesRequest
import coden.alec.interactors.definer.scale.ListScalesResponse

fun interface Action {
    fun execute(useCaseFactory: UseCaseFactory, view: View, messages: MessageResource): Boolean
}


object DisplayHelpMessage: Action {
    override fun execute(useCaseFactory: UseCaseFactory, view: View, messages: MessageResource): Boolean {
        view.displayMessage(messages.startMessage)
        return true
    }
}

object ListScales: Action {
    override fun execute(useCaseFactory: UseCaseFactory, view: View, messages: MessageResource): Boolean {
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
        return true;
    }
}

object CreateInvalidScale: Action {
    override fun execute(useCaseFactory: UseCaseFactory, view: View, messages: MessageResource): Boolean {
        view.displayError("Invalid format")
        return true;
    }
}

object DisplayScaleCreated: Action {
    override fun execute(useCaseFactory: UseCaseFactory, view: View, messages: MessageResource): Boolean {
        val createScale = useCaseFactory.createScale()
        val response = createScale.execute(CreateScaleRequest(
            name = "",
            unit = "",
            mapOf(
                1L to ""
            )
        )) as CreateScaleResponse
        response.scaleId.onSuccess {
            view.displayMessage("Added id: $it")
        }.onFailure {
            view.displayError("${messages.errorMessage} ${it.message}")
        }
        return true;
    }
}

object CreateScalePromptName: Action {
    override fun execute(useCaseFactory: UseCaseFactory, view: View, messages: MessageResource): Boolean {
        view.displayPrompt("Input the name of the scale:")
        return true;
    }
}
package coden.alec.app.states

import coden.alec.bot.messages.MessageResource
import coden.alec.bot.presenter.View
import coden.alec.interactors.definer.scale.CreateScaleRequest
import coden.alec.interactors.definer.scale.CreateScaleResponse
import coden.alec.interactors.definer.scale.ListScalesRequest
import coden.alec.interactors.definer.scale.ListScalesResponse

interface Action {
    fun execute(ctx: ActionContext)
}

data class ActionContext(
    val useCaseFactory: UseCaseFactory,
    val view: View,
    val messages: MessageResource,
    val args: String
)


object DisplayHelpMessage: Action {
    override fun execute(ctx: ActionContext) {
        ctx.view.displayMessage(ctx.messages.startMessage)
    }
}

object GetScalesAndDisplay: Action {
    override fun execute(ctx: ActionContext) {
        val listScales = ctx.useCaseFactory.listScales()
        val response = listScales.execute(ListScalesRequest()) as ListScalesResponse
        response.scales.onSuccess {
            if (it.isEmpty()){
                ctx.view.displayMessage(ctx.messages.listScalesEmptyMessage)
            }else {
                ctx.view.displayMessage(ctx.messages.listScalesMessage + it)
            }
        }.onFailure {
            ctx.view.displayError("${ctx.messages.errorMessage} ${it.message}")
        }
    }
}

object FailOnInvalidScale: Action {
    override fun execute(ctx: ActionContext) {
        ctx.view.displayError("Invalid format")
    }
}

object CreateScaleAndDisplay: Action {
    override fun execute(ctx: ActionContext) {
        val args = ctx.args.split("\n")
        val name = args[0]
        val unit = args[1]
        val divisions = HashMap<Long, String>()
        for (arg in args.subList(2, args.size)) {
            val division = arg.split("-")
            divisions[division[0].toLong()] = division[1]
        }
        val createScale = ctx.useCaseFactory.createScale()
        val response = createScale.execute(CreateScaleRequest(
            name = name,
            unit = unit,
            divisions
        )) as CreateScaleResponse
        response.scaleId.onSuccess {
            ctx.view.displayMessage("Added id: $it")
        }.onFailure {
            ctx.view.displayError("${ctx.messages.errorMessage} ${it.message}")
        }
    }
}

object CreateScalePromptName: Action {
    override fun execute(ctx: ActionContext) {
        ctx.view.displayPrompt("Input the name of the scale:")
    }
}
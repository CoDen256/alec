package coden.alec.app.states

import coden.alec.bot.messages.MessageResource
import coden.alec.bot.presenter.View
import coden.alec.interactors.definer.scale.CreateScaleRequest
import coden.alec.interactors.definer.scale.CreateScaleResponse
import coden.alec.interactors.definer.scale.ListScalesRequest
import coden.alec.interactors.definer.scale.ListScalesResponse

interface Action {
    fun execute(ctx: ActionContext)

    operator fun plus(other: Action): Action{
        return object : Action {
            override fun execute(ctx: ActionContext) {
                this@Action.execute(ctx)
                other.execute(ctx)
            }
        }

    }
}

data class ActionContext(
    val useCaseFactory: UseCaseFactory,
    val view: View,
    val messages: MessageResource,
    val args: String,
    val buffer: MutableList<String>
)


object DisplayHelp: Action {
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
        ctx.view.displayError("Invalid format of the scale")
    }
}

object CreateScaleAndDisplay: Action {
    override fun execute(ctx: ActionContext) {
        val args = if (ctx.buffer.isEmpty()) ctx.args.split("\n") else ctx.buffer + ctx.args
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

object FailOnScaleName: Action {
    override fun execute(ctx: ActionContext) {
        ctx.view.displayError("Invalid format of the name")
    }
}

object PromptScaleName: Action {
    override fun execute(ctx: ActionContext) {
        ctx.view.displayPrompt("Input the name of the scale:")
    }
}

object FailOnScaleUnit: Action {
    override fun execute(ctx: ActionContext) {
        ctx.view.displayError("Invalid format of the unit")
    }
}

object PromptScaleUnit: Action {
    override fun execute(ctx: ActionContext) {
        ctx.buffer.add(ctx.args)
        ctx.view.displayPrompt("Input the name of the unit:")
    }
}

object FailOnScaleDivisions: Action {
    override fun execute(ctx: ActionContext) {
        ctx.view.displayError("Invalid format of the divisions")
    }
}

object PromptScaleDivisions: Action {
    override fun execute(ctx: ActionContext) {
        ctx.buffer.add(ctx.args)
        ctx.view.displayPrompt("Input the divisions:")
    }
}
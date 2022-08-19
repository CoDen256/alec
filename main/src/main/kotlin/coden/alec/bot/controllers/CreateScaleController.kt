package coden.alec.bot.controllers

import coden.alec.bot.handler.Handler
import coden.alec.bot.utils.send
import coden.alec.core.CreateScaleActivator
import coden.alec.core.CreateScaleResponder
import coden.alec.core.Response
import coden.alec.interactors.definer.scale.CreateScaleRequest
import coden.alec.interactors.definer.scale.CreateScaleResponse
import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.Message

class CreateScaleController (
    private val createScaleActivator: CreateScaleActivator,
    private val bot: Bot,
) {

    fun handle(message: Message, args: List<String>) {
        createScaleActivator.execute(CreateScaleRequest(
            "",
            "",
            mapOf(1L to "")
        ))
    }


}

class CreateScaleHandler (
    private val bot: Bot,
    private val controller: CreateScaleController
        ): Handler {

    private val args = ArrayList<String>()

    fun handle(message: Message, args: List<String>): Boolean{
        if (args.isEmpty()){
            bot.send(message, "please args")
            return false
        }
        else {
            controller.handle(message, args)
            return true
        }
    }

    override fun handleArguments(message: Message): Boolean{
        message.text?.let { args.add(it) }
        if (args.size < 3){
            bot.send(message, "ok, now other arg:")
            return false
        }
        bot.send(message, "Good: $args")
        controller.handle(message, args)
        return true
    }
}

class CreateScalePresenter (
    private val bot: Bot,
    private val message: Message
): CreateScaleResponder {
    override fun submit(response: Response) { // decopule from bot
        response as CreateScaleResponse
        response.scaleId.onSuccess {
            bot.send(message, "Id of new scale: $it")
        }
    }
}

class CreateScaleInlinePresenter (
    private val bot: Bot,
    private val message: Message
): CreateScaleResponder {
    override fun submit(response: Response) { // decopule from bot
        response as CreateScaleResponse
        response.scaleId.onSuccess {
            bot.send(message, "Id of new scale: $it")
        }
    }
}
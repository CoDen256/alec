package coden.alec.bot.controllers

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
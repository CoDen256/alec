package coden.alec.bot.controllers

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
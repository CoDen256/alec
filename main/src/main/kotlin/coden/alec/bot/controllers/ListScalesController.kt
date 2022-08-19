//package coden.alec.bot.controllers
//
//import coden.alec.bot.utils.edit
//import coden.alec.bot.utils.send
//import coden.alec.core.ListScalesActivator
//import coden.alec.core.ListScalesResponder
//import coden.alec.core.Response
//import coden.alec.interactors.definer.scale.ListScalesRequest
//import coden.alec.interactors.definer.scale.ListScalesResponse
//import com.github.kotlintelegrambot.Bot
//import com.github.kotlintelegrambot.entities.ChatId
//import com.github.kotlintelegrambot.entities.InlineKeyboardMarkup
//import com.github.kotlintelegrambot.entities.Message
//import com.github.kotlintelegrambot.entities.keyboard.InlineKeyboardButton
//
//class ListScalesController(
//    private val listScalesActivator: ListScalesActivator,
//    private val bot: Bot,
//) {
//
//    fun handle(message: Message) {
//        val response = listScalesActivator.execute(ListScalesRequest())
//    }
//
//
//}
//
//class ListScalesPresenter(
//    private val bot: Bot,
//    private val message: Message
//) : ListScalesResponder {
//    override fun submit(response: Response) {
//        response as ListScalesResponse
//        response.scales.onSuccess {
//            if (it.isEmpty()) {
//                bot.send(message, "Empty")
//            } else {
//                bot.send(message, it.toString())
//            }
//        }
//    }
//}
//
//class ListScalesInlinePresenter(
//    private val bot: Bot,
//    private val message: Message
//) : ListScalesResponder {
//    override fun submit(response: Response) {
//        response as ListScalesResponse
//        response.scales.onSuccess {
//            if (it.isEmpty()) {
//
//                bot.send(message, "Empty")
//            } else {
//                val inlineKeyboardMarkup = InlineKeyboardMarkup.create(
//                    listOf(
//                        InlineKeyboardButton.CallbackData(
//                            text = "Test Inline Button 2",
//                            callbackData = "testButton"
//                        )
//                    ),
//                    listOf(InlineKeyboardButton.CallbackData(text = "Show alert 2", callbackData = "showAlert"))
//                )
//
//                bot.edit(message, it.toString(), replyMarkup = inlineKeyboardMarkup)
//            }
//        }
//    }
//}
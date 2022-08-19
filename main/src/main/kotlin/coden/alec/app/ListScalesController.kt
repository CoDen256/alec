package coden.alec.app

import coden.alec.bot.presenter.View
import coden.alec.core.ListScalesActivator
import coden.alec.core.ListScalesResponder
import coden.alec.core.Response
import coden.alec.interactors.definer.scale.ListScalesRequest
import coden.alec.interactors.definer.scale.ListScalesResponse

class ListScalesController(
    private val listScalesActivator: ListScalesActivator,
    private val listScalesResponder: ListScalesResponder
) {

    fun handle() {
        listScalesResponder.submit(listScalesActivator.execute(ListScalesRequest()))
    }

}

class ListScalesPresenter(
    private val view: View
) : ListScalesResponder {
    override fun submit(response: Response) {
        response as ListScalesResponse
        response.scales.onSuccess {
            if (it.isEmpty()) {
                view.displayMessage("List is empty")
            } else {
                view.displayMessage(it.toString())
            }
        }
    }
}

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
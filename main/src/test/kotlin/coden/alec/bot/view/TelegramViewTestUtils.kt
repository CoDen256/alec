package coden.alec.bot.view

import coden.bot.sender.TelegramMessage
import coden.bot.sender.TelegramMessageSender
import coden.bot.view.format.TelegramMenuFormatter
import coden.menu.MenuView

class RecordingMessageSender : TelegramMessageSender {

    val recordedSends = ArrayList<SendRequest>()
    val recordedEdits = ArrayList<SendRequest>()

    override fun send(chatId: Long, message: TelegramMessage) {
        recordedSends.add(SendRequest(chatId, null, message))
    }

    override fun edit(chatId: Long, messageId: Long, message: TelegramMessage) {
        recordedEdits.add(SendRequest(chatId, messageId, message))
    }

    data class SendRequest (val chatId: Long, val messageId: Long?, val message: TelegramMessage)

}

class DummyMenuFormatter : TelegramMenuFormatter {
    override fun format(menu: MenuView): TelegramMessage {
        return TelegramMessage(content = menu.description, null)
    }
}


package coden.alec.bot.view

import coden.alec.app.views.View
import coden.alec.bot.TelegramMessage
import coden.alec.bot.TelegramMessageSender
import coden.menu.MenuView
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow

internal class TelegramViewTest {
    @Test
    internal fun create() {
        assertDoesNotThrow {
            CommonTelegramView(TelegramContext(0), RecordingMessageSender())
        }
    }

    @Test
    internal fun send() {
        val sender = RecordingMessageSender()
        val view = CommonTelegramView(
            TelegramContext(0),
            sender
        )

        view.displayMessage("hustensaft")

        assertEquals("hustensaft", sender.recordedSends[0].message)
        assertNull( sender.recordedSends[0].replyMarkup)
    }

    companion object {

        class CommonTelegramView(private val context: TelegramContext,
                                 private val messageSender: TelegramMessageSender): View {

            override fun displayPrompt(message: String) {
                messageSender.send(context.chatId, TelegramMessage(
                    message, null
                )
                )
            }

            override fun displayMessage(message: String) {
                messageSender.send(context.chatId, TelegramMessage(
                    message, null
                ))
            }

            override fun displayMenu(menu: MenuView) {
//                messageSender.send(context.chatId, TelegramMessage(
//                    message, null
//                ))
            }

            override fun displayError(message: String) {
                messageSender.send(context.chatId, TelegramMessage(
                    message, null
                ))
            }

        }

        class TelegramContext(val chatId: Long)

        class RecordingMessageSender : TelegramMessageSender {

            val recordedSends = ArrayList<TelegramMessage>()
            val recordedEdits = ArrayList<TelegramMessage>()
            override fun send(chatId: Long, message: TelegramMessage) {
                recordedSends.add(message)
            }

            override fun edit(chatId: Long, messageId: Long, message: TelegramMessage) {
                recordedEdits.add(message)
            }

        }
    }

}

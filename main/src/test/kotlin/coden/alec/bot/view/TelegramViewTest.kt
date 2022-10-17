package coden.alec.bot.view

import coden.alec.bot.sender.TelegramMessage
import coden.alec.bot.sender.TelegramMessageSender
import coden.alec.bot.view.format.TelegramMenuFormatter
import coden.menu.MenuView
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow

internal class TelegramViewTest {
    @Test
    internal fun create() {
        assertDoesNotThrow {
            CommonTelegramView(TelegramContext(0), RecordingMessageSender(), DummyMenuFormatter()
            )
        }
    }

    @Test
    internal fun send() {
        val sender = RecordingMessageSender()
        val view = CommonTelegramView(
            TelegramContext(0),
            sender,
            DummyMenuFormatter()
        )

        view.displayMessage("hustensaft")

        assertEquals(1, sender.recordedSends.size)
        assertEquals("hustensaft", sender.recordedSends[0].message)
        assertNull( sender.recordedSends[0].replyMarkup)
    }

    @Test
    internal fun sendError() {
        val sender = RecordingMessageSender()

        val view = CommonTelegramView(
            TelegramContext(0),
            sender,
            DummyMenuFormatter()
        )

        view.displayError("hustensaft")


        assertEquals(1, sender.recordedSends.size)
        assertEquals("hustensaft", sender.recordedSends[0].message)
        assertNull( sender.recordedSends[0].replyMarkup)
    }

    @Test
    internal fun sendPrompt() {
        val sender = RecordingMessageSender()

        val view = CommonTelegramView(
            TelegramContext(0),
            sender,
            DummyMenuFormatter()
        )

        view.displayError("hustensaft")


        assertEquals(1, sender.recordedSends.size)
        assertEquals("hustensaft", sender.recordedSends[0].message)
        assertNull( sender.recordedSends[0].replyMarkup)
    }

    @Test
    internal fun sendMenu() {
        val sender = RecordingMessageSender()

        val view = CommonTelegramView(
            TelegramContext(0),
            sender,
            DummyMenuFormatter()
        )

        view.displayMenu(MenuView("Description", emptyList(), null))


        assertEquals(1, sender.recordedSends.size)
        assertEquals("Description", sender.recordedSends[0].message)
        assertNull( sender.recordedSends[0].replyMarkup)
    }

    companion object {

        class DummyMenuFormatter: TelegramMenuFormatter {
            override fun format(menu: MenuView): TelegramMessage {
                return TelegramMessage(message = menu.description, null)
            }
        }


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

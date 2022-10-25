package coden.alec.bot.view

import coden.menu.MenuView
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow

internal class TelegramInlineViewTest {

    @Test
    internal fun create() {
        assertDoesNotThrow {
            TelegramMenuDisplay(
                TelegramMessageContext(0, 1),
                RecordingMessageSender(),
                DummyMenuFormatter()
            )
        }
    }

    @Test
    internal fun send() {
        val sender = RecordingMessageSender()
        val view = TelegramMenuDisplay(
            TelegramMessageContext(20, 112),
            sender,
            DummyMenuFormatter()
        )


        view.displayMessage("hustensaft")

        assertEquals(0, sender.recordedSends.size)
        assertEquals(1, sender.recordedEdits.size)
        assertEquals(
            "hustensaft", sender.recordedEdits[0].message.content
        )
        assertEquals(20, sender.recordedEdits[0].chatId)
        assertEquals(112, sender.recordedEdits[0].messageId)
        assertNull(sender.recordedEdits[0].message.replyMarkup)
    }

    @Test
    internal fun sendMenu() {
        val sender = RecordingMessageSender()
        val view = TelegramMenuDisplay(
            TelegramMessageContext(0, 1),
            sender,
            DummyMenuFormatter()
        )


        view.displayMenu(MenuView("Description", emptyList(), null))

        assertEquals(0, sender.recordedSends.size)
        assertEquals(1, sender.recordedEdits.size)
        assertEquals(
            "Description", sender.recordedEdits[0].message.content
        )
        assertEquals(0, sender.recordedEdits[0].chatId)
        assertEquals(1, sender.recordedEdits[0].messageId)
        assertNull(sender.recordedEdits[0].message.replyMarkup)
    }

    @Test
    internal fun error() {
        val sender = RecordingMessageSender()
        val view = TelegramMenuDisplay(
            TelegramMessageContext(0, 1),
            sender,
            DummyMenuFormatter()
        )


        view.displayError("hustensaft")

        assertEquals(0, sender.recordedSends.size)
        assertEquals(1, sender.recordedEdits.size)
        assertEquals(
            "hustensaft", sender.recordedEdits[0].message.content
        )
        assertEquals(0, sender.recordedEdits[0].chatId)
        assertEquals(1, sender.recordedEdits[0].messageId)
        assertNull(sender.recordedEdits[0].message.replyMarkup)
    }

    @Test
    internal fun prompt() {
        val sender = RecordingMessageSender()
        val view = TelegramMenuDisplay(
            TelegramMessageContext(30, 41),
            sender,
            DummyMenuFormatter()
        )


        view.displayPrompt("hustensaft")

        assertEquals(0, sender.recordedSends.size)
        assertEquals(1, sender.recordedEdits.size)
        assertEquals(
            "hustensaft", sender.recordedEdits[0].message.content
        )
        assertEquals(30, sender.recordedEdits[0].chatId)
        assertEquals(41, sender.recordedEdits[0].messageId)
        assertNull(sender.recordedEdits[0].message.replyMarkup)
    }
}
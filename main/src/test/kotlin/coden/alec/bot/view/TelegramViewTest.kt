package coden.alec.bot.view

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
        assertEquals(0, sender.recordedEdits.size)
        assertEquals("hustensaft", sender.recordedSends[0].message.content)
        assertEquals(0, sender.recordedSends[0].chatId)
        assertNull( sender.recordedSends[0].message.replyMarkup)
    }

    @Test
    internal fun sendError() {
        val sender = RecordingMessageSender()

        val view = CommonTelegramView(
            TelegramContext(10),
            sender,
            DummyMenuFormatter()
        )

        view.displayError("hustensaft")


        assertEquals(1, sender.recordedSends.size)
        assertEquals(0, sender.recordedEdits.size)
        assertEquals("hustensaft", sender.recordedSends[0].message.content)
        assertEquals(10, sender.recordedSends[0].chatId)
        assertNull( sender.recordedSends[0].message.replyMarkup)
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
        assertEquals(0, sender.recordedEdits.size)
        assertEquals("hustensaft", sender.recordedSends[0].message.content)
        assertEquals(0, sender.recordedSends[0].chatId)
        assertNull( sender.recordedSends[0].message.replyMarkup)
    }

    @Test
    internal fun sendMenu() {
        val sender = RecordingMessageSender()

        val view = CommonTelegramView(
            TelegramContext(12),
            sender,
            DummyMenuFormatter()
        )

        view.displayMenu(MenuView("Description", emptyList(), null))

        assertEquals(1, sender.recordedSends.size)
        assertEquals(0, sender.recordedEdits.size)
        assertEquals("Description" +
                "", sender.recordedSends[0].message.content)
        assertEquals(12, sender.recordedSends[0].chatId)
        assertNull( sender.recordedSends[0].message.replyMarkup)
    }

}

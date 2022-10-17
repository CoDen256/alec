package coden.alec.bot.sender

interface TelegramMessageSender {
    fun send(chatId: Long, message: TelegramMessage)
    fun edit(chatId: Long, messageId: Long, message: TelegramMessage)
}
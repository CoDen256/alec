package coden.alec.bot.view

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.Message

data class TelegramContext(
    val bot: Bot,
    val current: Message

    )
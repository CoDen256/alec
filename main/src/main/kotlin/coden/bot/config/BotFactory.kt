package coden.bot.config

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.logging.LogLevel

interface BotFactory {
    fun build(parameters: BotConfigurationParameters): Bot
}

data class BotConfigurationParameters(
    val token: String,
    val log: LogLevel,
)
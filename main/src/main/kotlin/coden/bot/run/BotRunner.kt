package coden.bot.run

import coden.bot.config.BotConfigurationParameters
import coden.bot.config.BotFactory

class BotRunner (
    private val parameters: BotConfigurationParameters,
    private val botFactory: BotFactory
) {

    fun run() {
        botFactory
            .build(parameters)
            .startPolling()
    }
}
package coden.bot.run

import coden.display.AppRunner
import coden.bot.config.BotConfigurationParameters
import coden.bot.config.BotFactory

class BotRunner (
    private val parameters: BotConfigurationParameters,
    private val botFactory: BotFactory
): AppRunner {

    override fun run() {
        botFactory
            .build(parameters)
            .startPolling()
    }
}
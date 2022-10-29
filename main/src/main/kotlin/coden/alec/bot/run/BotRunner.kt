package coden.alec.bot.run

import coden.alec.app.AppRunner
import coden.alec.bot.config.BotConfigurationParameters
import coden.alec.bot.config.BotFactory

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
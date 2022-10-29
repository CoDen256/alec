package coden.alec.bot

import coden.alec.app.AppRunner
import coden.bot.run.BotRunner

class BotRunnerAdapter(private val botRunner: BotRunner): AppRunner{
    override fun run() {
        botRunner.run()
    }

}
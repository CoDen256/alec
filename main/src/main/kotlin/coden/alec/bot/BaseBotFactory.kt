package coden.alec.bot

import coden.alec.bot.config.BotConfigurationParameters
import coden.alec.bot.config.BotFactory
import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch
import com.github.kotlintelegrambot.dispatcher.Dispatcher

class BaseBotFactory(private val configurator: BotDispatcherConfigurator) : BotFactory {

    override fun build(parameters: BotConfigurationParameters): Bot = bot {
        token = parameters.token
        logLevel = parameters.log

        dispatch {
            configurator.apply(this)
        }
    }
}

interface BotDispatcherConfigurator{
    fun apply(dispatcher: Dispatcher){
        dispatcher.configure()
    }

    fun Dispatcher.configure()
}
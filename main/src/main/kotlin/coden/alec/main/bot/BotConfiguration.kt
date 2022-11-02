package coden.alec.main.bot

import coden.alec.app.AppRunner
import coden.alec.bot.AlecBotConfigurator
import coden.alec.bot.BotRunnerAdapter
import coden.alec.main.resources.CommandNames
import coden.bot.BaseBotFactory
import coden.bot.BotDispatcherConfigurator
import coden.bot.config.BotConfigurationParameters
import coden.bot.config.BotFactory
import coden.bot.context.ContextObserver
import coden.bot.run.BotRunner
import coden.display.menu.MenuPresenter
import coden.fsm.CommandExecutor
import com.github.kotlintelegrambot.logging.LogLevel
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
@EnableConfigurationProperties(AlecBotProperties::class, CommandNames::class)
class BotConfiguration {

    @Bean
    fun configurator(observer: ContextObserver,
                     commandExecutor: CommandExecutor,
                     menuPresenter: MenuPresenter,
                     commandNames: CommandNames
    ): BotDispatcherConfigurator {
        return AlecBotConfigurator(
            observer,
            commandExecutor,
            menuPresenter,
            commandNames
        )
    }

    @Bean
    fun botFactory(configurator: BotDispatcherConfigurator): BotFactory {
        return BaseBotFactory(configurator)
    }

    @Bean
    fun params(properties: AlecBotProperties): BotConfigurationParameters {
        return BotConfigurationParameters(
            properties.token,
            LogLevel.Error,
        )
    }

    @Bean
    fun botRunner(params: BotConfigurationParameters, botFactory: BotFactory): BotRunner {
        return BotRunner(
            params,
            botFactory
        )
    }

    @Bean("telegram")
    fun telegram(botRunner: BotRunner): AppRunner {
        return BotRunnerAdapter(botRunner)
    }

}
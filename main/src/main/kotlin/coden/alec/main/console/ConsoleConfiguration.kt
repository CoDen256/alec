package coden.alec.main.console

import coden.alec.app.AppRunner
import coden.alec.console.AlecConsoleConfigurator
import coden.alec.console.ConsoleRunnerAdapter
import coden.alec.main.annotations.ConsoleEnabled
import coden.alec.main.resources.CommandNames
import coden.console.BaseConsoleDispatcherBuilder
import coden.console.ConsoleRunner
import coden.console.dispatcher.ConsoleDispatcherBuilder
import coden.console.dispatcher.ConsoleDispatcherConfigurator
import coden.console.read.CommandParser
import coden.console.read.CommandReader
import coden.display.displays.ErrorDisplay
import coden.display.menu.MenuPresenter
import coden.fsm.CommandExecutor
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(CommandNames::class)
@ConsoleEnabled
class ConsoleConfiguration {

    @Bean
    fun dispatcherBuilder(): ConsoleDispatcherBuilder{
        return BaseConsoleDispatcherBuilder()
    }

    @Bean
    fun consoleConfigurator(commandExecutor: CommandExecutor,
                            menuPresenter: MenuPresenter,
                            commandNames: CommandNames,
                     ): ConsoleDispatcherConfigurator{
        return AlecConsoleConfigurator(
            commandExecutor, menuPresenter, commandNames
        )
    }

    @Bean
    fun consoleRunner(
        commandParser: CommandParser,
        commandReader: CommandReader,
        dispatcherBuilder: ConsoleDispatcherBuilder,
        configurator: ConsoleDispatcherConfigurator,
        @Qualifier("errorDisplay") display: ErrorDisplay
    ): ConsoleRunner {
        return ConsoleRunner(
            commandParser, commandReader, dispatcherBuilder, configurator, display
        )
    }

    @Bean("console")
    fun console(consoleRunner: ConsoleRunner): AppRunner {
        return ConsoleRunnerAdapter(consoleRunner)
    }

}
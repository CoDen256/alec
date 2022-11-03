package coden.alec.main.console

import coden.alec.main.annotations.ConsoleEnabled
import coden.bot.context.Context
import coden.bot.context.proxy.ContextBasedTelegramMessageDisplay
import coden.bot.sender.TelegramMessageSender
import coden.bot.view.format.TelegramMenuFormatter
import coden.console.BaseConsoleMenuFormatter
import coden.console.view.ConsoleMenuDisplay
import coden.console.view.ConsoleMenuFormatter
import coden.console.view.ConsoleMessageDisplay
import coden.display.displays.MenuDisplay
import coden.display.displays.MessageDisplay
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@ConsoleEnabled
class ConsoleDisplayConfiguration {

    @Bean
    fun formatter(): ConsoleMenuFormatter{
        return BaseConsoleMenuFormatter()
    }

    @Bean("consoleMenuDisplay")
    fun menuDisplay(formatter: ConsoleMenuFormatter): MenuDisplay {
        return ConsoleMenuDisplay(formatter)
    }

    @Bean("consoleMessageDisplay")
    fun messageDisplay(): MessageDisplay {
        return ConsoleMessageDisplay()
    }
}
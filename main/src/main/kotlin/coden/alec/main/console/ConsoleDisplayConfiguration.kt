package coden.alec.main.console

import coden.alec.main.annotations.ConsoleEnabled
import coden.console.BaseConsoleMenuFormatter
import coden.console.view.ConsoleMenuDisplay
import coden.console.view.ConsoleMenuFormatter
import coden.console.view.ConsoleMessageDisplay
import coden.display.displays.ErrorDisplay
import coden.display.displays.MenuDisplay
import coden.display.displays.MessageDisplay
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@ConsoleEnabled
class ConsoleDisplayConfiguration {

    @Bean
    fun consoleFormatter(): ConsoleMenuFormatter{
        return BaseConsoleMenuFormatter()
    }

    @Bean
    fun consoleMenuDisplay(formatter: ConsoleMenuFormatter): MenuDisplay {
        return ConsoleMenuDisplay(formatter)
    }

    @Bean
    fun consoleMessageDisplay(): MessageDisplay {
        return ConsoleMessageDisplay()
    }

    @Bean("errorDisplay")
    fun errorDisplay(consoleMessageDisplay: MessageDisplay): ErrorDisplay {
        return consoleMessageDisplay
    }
}
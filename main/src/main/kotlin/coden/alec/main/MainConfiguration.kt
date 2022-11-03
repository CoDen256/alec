package coden.alec.main

import coden.alec.app.AppRunner
import coden.display.displays.ErrorDisplay
import coden.display.displays.MenuDisplay
import coden.display.displays.MessageDisplay
import coden.display.menu.MenuNavigator
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.scheduling.annotation.Async

@Configuration
class MainConfiguration {

    @Bean
    @Primary
    fun messageDisplay(@Qualifier("telegramMessageDisplay") display: MessageDisplay): MessageDisplay {
        return display
    }

    @Bean("errorDisplay")
    @Primary
    fun errorDisplay(display: MessageDisplay): ErrorDisplay {
        return display
    }

    @Bean
    @Primary
    fun menuDisplay(@Qualifier("telegramMenuDisplay") menuDisplay: MenuDisplay): MenuDisplay{
        return menuDisplay
    }

    @Bean
    @Primary
    fun menuNavigator(@Qualifier("telegramMenuNavigator") menuNavigator: MenuNavigator): MenuNavigator{
        return menuNavigator
    }

    //    @EventListener(ApplicationReadyEvent::class)
    @Async
    @Bean
    fun runner(@Qualifier("telegram") runner: AppRunner): AppRunner {
        runner.run()
        return runner
    }
}
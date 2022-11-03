package coden.alec.main.bot

import coden.bot.menu.TelegramAggregatedMenuNavigator
import coden.display.menu.MenuNavigator
import coden.menu.LayoutBasedMenuNavigatorFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class BotMenuNavigatorConfiguration {
    @Bean("telegramMenuNavigator")
    fun navigator(layoutBasedMenuNavigatorFactory: LayoutBasedMenuNavigatorFactory): MenuNavigator {
        return TelegramAggregatedMenuNavigator(layoutBasedMenuNavigatorFactory)
    }
}
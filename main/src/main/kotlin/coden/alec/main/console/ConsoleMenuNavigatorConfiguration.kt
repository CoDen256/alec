package coden.alec.main.console

import coden.console.menu.ConsoleMenuReindexingNavigator
import coden.display.menu.MenuNavigator
import coden.menu.LayoutBasedMenuNavigatorFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ConsoleMenuNavigatorConfiguration {
    @Bean("consoleMenuNavigator")
    fun menuNavigator(layoutNavigator: LayoutBasedMenuNavigatorFactory): MenuNavigator{
        return ConsoleMenuReindexingNavigator(
            layoutNavigator.newMenuNavigator()
        )
    }
}
package coden.alec.main.console

import coden.alec.main.annotations.ConsoleEnabled
import coden.console.menu.ConsoleMenuReindexingNavigator
import coden.display.menu.MenuNavigator
import coden.menu.LayoutBasedMenuNavigatorFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@ConsoleEnabled
class ConsoleMenuNavigatorConfiguration {
    @Bean
    fun menuNavigator(layoutNavigator: LayoutBasedMenuNavigatorFactory): MenuNavigator{
        return ConsoleMenuReindexingNavigator(
            layoutNavigator.newMenuNavigator()
        )
    }
}
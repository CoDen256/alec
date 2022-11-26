package coden.alec.main.base

import coden.alec.app.fsm.*
import coden.alec.main.resources.Messages
import coden.display.displays.MenuDisplay
import coden.display.displays.MessageDisplay
import coden.display.menu.BaseMenuPresenter
import coden.display.menu.MenuNavigator
import coden.display.menu.MenuPresenter
import coden.menu.ItemLayout.Companion.itemLayout
import coden.menu.LayoutBasedMenuNavigatorFactory
import coden.menu.MenuLayout
import coden.menu.MenuLayout.Companion.menuLayout
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(Messages::class)
class MenuConfiguration {

    @Bean
    fun menu(messages: Messages): MenuLayout{ // deserialize from configuration properties
        return menuLayout(
            "Choose anything from  the main menu", itemLayout(messages.menuBack),
            itemLayout("Start",  action = HelpCommand),
            itemLayout(
                "Scale Management", "Scales are needed define factors",
                itemLayout("List all Scales", action = ListScalesCommand),
                itemLayout("Create Scale", action = CreateScaleCommandNoArgs),
                itemLayout("Delete Scale", action = DeleteScaleCommandNoArgs),
                itemLayout("Purge Scale", action = PurgeScaleCommandNoArgs)
            ),
            itemLayout(
                "Factors", "Factor management",
                itemLayout("Start", action = HelpCommand),
            ),
            itemLayout(
                "Raters", "Rater management",
                itemLayout("Start", action = HelpCommand),
            )
        )
    }

    @Bean
    fun layoutNavigator(menuLayout: MenuLayout): LayoutBasedMenuNavigatorFactory{
        return LayoutBasedMenuNavigatorFactory(menuLayout)
    }

    @Bean
    fun presenter(
        display: MessageDisplay,
        menuDisplay: MenuDisplay,
        menuNavigator: MenuNavigator
    ): MenuPresenter {
        return BaseMenuPresenter(display, menuDisplay, menuNavigator)
    }
}
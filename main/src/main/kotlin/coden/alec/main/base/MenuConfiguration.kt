package coden.alec.main.base

import coden.alec.app.fsm.CreateScaleCommandNoArgs
import coden.alec.app.fsm.HelpCommand
import coden.alec.app.fsm.ListScalesCommand
import coden.menu.ItemLayout.Companion.itemLayout
import coden.menu.LayoutBasedMenuNavigatorFactory
import coden.menu.MenuLayout
import coden.menu.MenuLayout.Companion.menuLayout
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class MenuConfiguration {

    @Bean
    fun menu(): MenuLayout{
        return menuLayout(
            "Choose anything from  the main menu", itemLayout("Back"),
            itemLayout(
                "Common", "Common commands",
                itemLayout("Start", action = HelpCommand),
                itemLayout(
                    "Help",
                    "Help commands",
                    itemLayout("Help again", action = HelpCommand)
                )
            ),
            itemLayout(
                "Scales", "Scale management",
                itemLayout("Display all Scales", action = ListScalesCommand),
                itemLayout("Create Scale", action = CreateScaleCommandNoArgs)
            ),
            itemLayout(
                "Factors", "Factor management",
                itemLayout("Start", action = HelpCommand),
                itemLayout("Help", action = HelpCommand)
            ),
            itemLayout(
                "Raters", "Rater management",
                itemLayout("Start", action = HelpCommand),
                itemLayout("Help", action = HelpCommand)
            )
        )
    }

    @Bean
    fun layoutNavigator(menuLayout: MenuLayout): LayoutBasedMenuNavigatorFactory{
        return LayoutBasedMenuNavigatorFactory(menuLayout)
    }

}
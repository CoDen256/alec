package coden.alec.console

import coden.alec.app.fsm.CreateScaleCommandNoArgs
import coden.alec.app.fsm.HelpCommand
import coden.alec.app.fsm.ListScalesCommand
import coden.console.menu.ConsoleMenuReindexingNavigator
import coden.menu.ItemLayout
import coden.menu.MenuLayout
import coden.menu.LayoutBasedMenuNavigator
import org.junit.jupiter.api.Test

internal class ConsoleAppTest{
    @Test
    internal fun name() {
        val menu = MenuLayout.menuLayout(
            "Choose anything from  the main menu", ItemLayout.itemLayout("Back"), ItemLayout.itemLayout(
                "Common", "Common commands",
                ItemLayout.itemLayout("Start", action = HelpCommand),
                ItemLayout.itemLayout("Help", action = HelpCommand)
            ),
            ItemLayout.itemLayout(
                "Scales", "Scale management",
                ItemLayout.itemLayout("View all Scales", action = ListScalesCommand),
                ItemLayout.itemLayout("Create Scale", action = CreateScaleCommandNoArgs)
            ),
            ItemLayout.itemLayout(
                "Factors", "Factor management",
                ItemLayout.itemLayout("Start", action = HelpCommand),
                ItemLayout.itemLayout("Help", action = HelpCommand)
            ),
            ItemLayout.itemLayout(
                "Raters", "Rater management",
                ItemLayout.itemLayout("Start", action = HelpCommand),
                ItemLayout.itemLayout("Help", action = HelpCommand)
            )
        )

        val controller = ConsoleMenuReindexingNavigator(LayoutBasedMenuNavigator(menu))

//        val app = ConsoleApp(MenuNavigatorFactory(menu), )
    }
}
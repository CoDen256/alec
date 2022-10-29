package coden.alec.console.menu

import coden.alec.app.fsm.CreateScaleCommandNoArgs
import coden.alec.app.fsm.HelpCommand
import coden.alec.app.fsm.ListScalesCommand
import coden.menu.ItemLayout
import coden.menu.MenuLayout
import coden.menu.LayoutBasedMenuNavigator
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class ConsoleMenuReindexingNavigatorTest{
    @Test
    internal fun navigate() {
        val menu = MenuLayout.menuLayout(
            "Choose anything from the main menu", ItemLayout.itemLayout("Back"),
            ItemLayout.itemLayout(
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

        assertEquals("Choose anything from the main menu", controller.createMainMenu().description )
        assertEquals("Common commands", controller.navigate("1").getOrThrow().menu.description )
        assertEquals("Choose anything from the main menu", controller.navigate("3").getOrThrow().menu.description )
        assertEquals("Factor management", controller.navigate("3").getOrThrow().menu.description )
        assertEquals("Factor management", controller.navigate("1").getOrThrow().menu.description )
        assertEquals("Factor management", controller.navigate("2").getOrThrow().menu.description )
        assertEquals("Choose anything from the main menu", controller.navigate("3").getOrThrow().menu.description )
        assertEquals("Scale management", controller.navigate("2").getOrThrow().menu.description )
        assertEquals(ListScalesCommand, controller.navigate("1").getOrThrow().action)

        assertFalse(controller.navigate("123123").isSuccess)
    }
}
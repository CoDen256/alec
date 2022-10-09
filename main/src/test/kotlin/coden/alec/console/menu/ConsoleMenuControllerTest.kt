package coden.alec.console.menu

import coden.alec.app.fsm.CreateScaleCommandNoArgs
import coden.alec.app.fsm.HelpCommand
import coden.alec.app.fsm.ListScalesCommand
import coden.menu.ItemLayout.Companion.itemLayout
import coden.menu.MenuLayout.Companion.menuLayout
import coden.menu.MenuNavigator
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class ConsoleMenuControllerTest {
    @Test
    internal fun navigate() {
        val menu = menuLayout(
        "Choose anything from  the main menu", itemLayout("Back"), itemLayout(
            "Common", "Common commands",
            itemLayout("Start", action = HelpCommand),
            itemLayout("Help", action = HelpCommand)
        ),
                itemLayout(
            "Scales", "Scale management",
                    itemLayout("View all Scales", action = ListScalesCommand),
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

        val controller = ConsoleMenuController(MenuNavigator(menu))
        println(controller.createMain())
        println(controller.navigate("1").getOrThrow().message)
        println(controller.navigate("2").getOrThrow().message)
        println(controller.navigate("2").getOrThrow().message)
        println(controller.navigate("0").getOrThrow().message)
        println(controller.navigate("2").getOrThrow().message)

        assertThrows<IllegalArgumentException> {
            controller.navigate("123123").getOrThrow()
        }

        println(controller.navigate("0").getOrThrow().message)
        println(controller.navigate("2").getOrThrow().message)

    }
}
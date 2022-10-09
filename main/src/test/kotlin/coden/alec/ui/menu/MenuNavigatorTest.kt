package coden.alec.ui.menu

import coden.alec.ui.menu.MenuLayout.Companion.menuLayout
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class MenuNavigatorTest{
    @Test
    internal fun navigate() {
        val menu = menuLayout("Main Menu",
            ItemLayout.itemLayout("Back"),
            ItemLayout.itemLayout("View Scales", action = EmptyCommand())
        )

        val navigator = MenuNavigator(menu)

        val mainMenu = navigator.createMainMenu()
        assertEquals("Main Menu", mainMenu.description)
        assertEquals("View Scales", mainMenu.itemRows[0].name)
        assertThat(mainMenu.itemRows[0].id).isNotEmpty()

    }
}
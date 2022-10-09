package coden.alec.ui.menu

import coden.alec.ui.menu.MenuLayout.Companion.menuLayout
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

internal class MenuNavigatorTest{
    @Test
    internal fun createMainMenu() {
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

    @Test
    internal fun navigateToAction() {
        //setup
        val menu = menuLayout("Main Menu",
            ItemLayout.itemLayout("Back"),
            ItemLayout.itemLayout("View Scales", action = EmptyCommand())
        )

        val navigator = MenuNavigator(menu)
        val mainMenu = navigator.createMainMenu()
        (0..3).forEach { _ ->
            // exercise
            val navigationResult = navigator.navigate(mainMenu.itemRows[0].id)

            // Verify
            // Action to perform
            assertNotNull(navigationResult)
            assertNotNull(navigationResult.action)
            assertNull(navigationResult.backItemView)
            assertInstanceOf(EmptyCommand::class.java, navigationResult.action)

            // Menu to display
            assertEquals("Main Menu", navigationResult.menu.description)
            assertEquals("View Scales", navigationResult.menu.itemRows[0].name)
            assertThat(mainMenu.itemRows[0].id).isNotEmpty()
        }

    }

    @Test
    internal fun navigateToTwoActions() {
        //setup
        val menu = menuLayout("Main Menu",
            ItemLayout.itemLayout("Back"),
            ItemLayout.itemLayout("View Scales", action = EmptyCommand("v")),
            ItemLayout.itemLayout("List Scales", action = EmptyCommand("l"))
        )

        val navigator = MenuNavigator(menu)
        val mainMenu = navigator.createMainMenu()
        // exercise
        val viewScalesResult = navigator.navigate(mainMenu.itemRows[0].id)

        // Verify
        // Action to perform
        assertNotNull(viewScalesResult)
        assertNotNull(viewScalesResult.action)
        assertNull(viewScalesResult.backItemView)
        assertInstanceOf(EmptyCommand::class.java, viewScalesResult.action)
        assertEquals("v", viewScalesResult.action!!.arguments.getOrNull())

        // Menu to display
        assertEquals("Main Menu", viewScalesResult.menu.description)
        assertEquals("View Scales", viewScalesResult.menu.itemRows[0].name)
        assertEquals("List Scales", viewScalesResult.menu.itemRows[1].name)
        assertThat(viewScalesResult.menu.itemRows[0].id).isNotEmpty()


        // exercise again to list scales
        val listScalesResult = navigator.navigate(mainMenu.itemRows[1].id)

        // Verify
        // Action to perform
        assertNotNull(listScalesResult)
        assertNotNull(listScalesResult.action)
        assertNull(listScalesResult.backItemView)
        assertInstanceOf(EmptyCommand::class.java, listScalesResult.action)
        assertEquals("l", listScalesResult.action!!.arguments.getOrNull())

        // Menu to display
        assertEquals("Main Menu", listScalesResult.menu.description)
        assertEquals("View Scales", listScalesResult.menu.itemRows[0].name)
        assertEquals("List Scales", listScalesResult.menu.itemRows[1].name)
        assertThat(listScalesResult.menu.itemRows[1].id).isNotEmpty()
    }
}
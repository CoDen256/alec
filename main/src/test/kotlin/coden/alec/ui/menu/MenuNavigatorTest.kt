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
            ItemLayout.itemLayout("View Scales", action = TestCommand())
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
            ItemLayout.itemLayout("View Scales", action = TestCommand())
        )

        val navigator = MenuNavigator(menu)
        val mainMenu = navigator.createMainMenu()
        (0..3).forEach { _ ->
            // exercise
            val navigationResult = navigator.navigate(mainMenu.itemRows[0].id)

            // Verify
            // Action to perform
            assertNotNull(navigationResult.action)
            assertInstanceOf(TestCommand::class.java, navigationResult.action)

            // Menu to display
            assertEquals("Main Menu", navigationResult.menu.description)
            assertEquals("View Scales", navigationResult.menu.itemRows[0].name)
            assertNull(navigationResult.menu.backItemView)
            assertThat(mainMenu.itemRows[0].id).isNotEmpty()
        }

    }

    @Test
    internal fun navigateToTwoActions() {
        //setup
        val menu = menuLayout("Main Menu",
            ItemLayout.itemLayout("Back"),
            ItemLayout.itemLayout("View Scales", action = TestCommand("v")),
            ItemLayout.itemLayout("List Scales", action = TestCommand("l"))
        )

        val navigator = MenuNavigator(menu)
        val mainMenu = navigator.createMainMenu()
        // exercise
        navigator.navigate(mainMenu.itemRows[0].id).also {
            // Verify
            // Action to perform
            assertNotNull(it.action)
            assertInstanceOf(TestCommand::class.java, it.action)
            assertEquals("v", it.action!!.arguments.getOrNull())

            // Menu to display
            assertEquals("Main Menu", it.menu.description)
            assertEquals("View Scales", it.menu.itemRows[0].name)
            assertEquals("List Scales", it.menu.itemRows[1].name)
            assertThat(it.menu.itemRows[0].id).isNotEmpty()
            assertNull(it.menu.backItemView)
        }


        // exercise again to list scales
        navigator.navigate(mainMenu.itemRows[1].id).also {
            // Verify
            // Action to perform
            assertNotNull(it.action)
            assertInstanceOf(TestCommand::class.java, it.action)
            assertEquals("l", it.action!!.arguments.getOrNull())

            // Menu to display
            assertEquals("Main Menu", it.menu.description)
            assertEquals("View Scales", it.menu.itemRows[0].name)
            assertEquals("List Scales", it.menu.itemRows[1].name)
            assertThat(it.menu.itemRows[1].id).isNotEmpty()
            assertNull(it.menu.backItemView)

        }
    }

    @Test
    internal fun navigateOneLevelDownAndBack() {
        //setup
        val menuLayout = menuLayout("Main Menu",
            ItemLayout.itemLayout("Back"),
            ItemLayout.itemLayout("Scales Commands",
                children = listOf(ItemLayout.itemLayout("View Scales", action = TestCommand("v")))),
        )

        val navigator = MenuNavigator(menuLayout)
        val mainMenu = navigator.createMainMenu()
        // exercise
        val result = navigator.navigate(mainMenu.itemRows[0].id).also {
            // Verify
            // Action to perform
            assertNull(it.action)

            // Menu to display
            assertEquals("Scales Commands", it.menu.description)
            assertEquals("View Scales", it.menu.itemRows[0].name)
            assertThat(it.menu.itemRows[0].id).isNotEmpty()
            assertNotNull(it.menu.backItemView)
        }

//        val actionResult = navigator.navigate(result.menu.itemRows[0].id).also {
//            // Verify
//            // Action to perform
//            assertNotNull(it.action)
//            assertInstanceOf(TestCommand::class.java, it.action)
//            assertEquals("v", it.action!!.arguments.getOrNull())
//
//            // Menu to display
//            assertEquals("Scales Commands", it.menu.description)
//            assertEquals("View Scales", it.menu.itemRows[0].name)
//            assertThat(it.menu.itemRows[0].id).isNotEmpty()
//            assertNotNull(it.backItemView)
//        }
//
//        navigator.navigate(actionResult.backItemView!!.id).also {
//            // Verify
//            // Action to perform
//            assertNull(it.action)
//
//            // Menu to display
//            assertEquals("Main Menu", it.menu.description)
//            assertEquals("Scales Commands", it.menu.itemRows[0].name)
//            assertThat(it.menu.itemRows[0].id).isNotEmpty()
//            assertNull(it.backItemView)
//        }
    }
}
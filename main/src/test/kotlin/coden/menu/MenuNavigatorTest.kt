package coden.menu

import coden.menu.MenuLayout.Companion.menuLayout
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.util.UUID

internal class MenuNavigatorTest {
    @Test
    internal fun createMainMenu() {
        val menu = menuLayout(
            "Main Menu",
            ItemLayout.itemLayout("Back"),
            ItemLayout.itemLayout("View Scales", action = TestCommand())
        )

        val navigator = LayoutBasedMenuNavigator(menu)

        val mainMenu = navigator.createMainMenu()
        assertEquals("Main Menu", mainMenu.description)
        assertEquals("View Scales", mainMenu.items[0].name)
        assertThat(mainMenu.items[0].id).isNotEmpty()
    }

    @Test
    internal fun navigateToAction() {
        //setup
        val menu = menuLayout(
            "Main Menu",
            ItemLayout.itemLayout("Back"),
            ItemLayout.itemLayout("View Scales", action = TestCommand("t"))
        )

        val navigator = LayoutBasedMenuNavigator(menu)
        val mainMenu = navigator.createMainMenu()
        (0..3).forEach { _ ->
            // exercise
            val navigationResult = navigator.navigate(mainMenu.items[0].id)
            verifyNavResult(navigationResult, "Main Menu", "View Scales", false,"t")
        }

    }

    @Test
    internal fun navigateToTwoActions() {
        //setup
        val menu = menuLayout(
            "Main Menu",
            ItemLayout.itemLayout("Back"),
            ItemLayout.itemLayout("View Scales", action = TestCommand("v")),
            ItemLayout.itemLayout("List Scales", action = TestCommand("l"))
        )

        val navigator = LayoutBasedMenuNavigator(menu)
        val mainMenu = navigator.createMainMenu()
        // exercise
        val viewScalesResult = navigator.navigate(mainMenu.items[0].id)
        verifyNavResult(viewScalesResult, "Main Menu", "View Scales", false, "v")

        // exercise again to list scales
        val listScalesResult = navigator.navigate(mainMenu.items[1].id)
        verifyNavResult(listScalesResult, "Main Menu", "View Scales", false, "l")
    }

    @Test
    internal fun navigateOneLevelDownAndBack() {
        //setup
        val menuLayout = menuLayout(
            "Main Menu",
            ItemLayout.itemLayout("Back"),
            ItemLayout.itemLayout(
                "Scales Commands",
                children = listOf(
                    ItemLayout.itemLayout(
                        "View Scales", action = TestCommand("v")
                    )
                )
            ),
        )

        val navigator = LayoutBasedMenuNavigator(menuLayout)
        val mainMenu = navigator.createMainMenu()
        // exercise
        val scalesItems = mainMenu.items[0]
        val scalesResult = navigator.navigate(scalesItems.id)
        verifyNavResult(scalesResult,"Scales Commands", "View Scales" )

        val viewScalesItem = scalesResult.menu.items[0]
        val viewScalesResult = navigator.navigate(viewScalesItem.id)
        verifyNavResult(viewScalesResult, "Scales Commands","View Scales", actionName = "v" )

        val backResult = navigator.navigate(viewScalesResult.menu.backItemView!!.id)
        verifyNavResult(backResult,
            "Main Menu",
            "Scales Commands",
            false)
    }

    @Test
    internal fun navigateTwoLevelDownAndTwoLevelBack() {
        //setup
        /*
        MainMenu:
            Scales Commands:
                View Scales(+v):
                    View Scales detailed(+d)


         */
        val menuLayout = menuLayout(
            "Main Menu",
            ItemLayout.itemLayout("Back"),
            ItemLayout.itemLayout(
                "Scales Commands", description = "Select one of the Scales Commands",
                children = listOf(
                    ItemLayout.itemLayout(
                        "View Scales", action = TestCommand("v"),
                        children = listOf(
                            ItemLayout.itemLayout(
                                "View Scales detailed", action = TestCommand("d")
                            )
                        )
                    )
                )
            ),
        )

        val navigator = LayoutBasedMenuNavigator(menuLayout)
        val mainMenu = navigator.createMainMenu()
        // exercise
        val scalesItem = mainMenu.items[0]
        val scalesResultMenu = navigator.navigate(scalesItem.id)
        verifyNavResult(scalesResultMenu, "Select one of the Scales Commands", "View Scales")

        val viewScalesItem = scalesResultMenu.menu.items[0]
        val viewScalesResultMenu = navigator.navigate(viewScalesItem.id)
        verifyNavResult(viewScalesResultMenu, "View Scales", "View Scales detailed", actionName = "v")

        val detailedScalesItem = viewScalesResultMenu.menu.items[0]
        val sameViewScalesResultMenu = navigator.navigate(detailedScalesItem.id)
        verifyNavResult(sameViewScalesResultMenu, "View Scales", "View Scales detailed", actionName = "d")

        val backItem = viewScalesResultMenu.menu.backItemView!!
        val backToScalesResultMenu = navigator.navigate(backItem.id)
        verifyNavResult(backToScalesResultMenu, "Select one of the Scales Commands", "View Scales")

        val viewScalesItem2 = backToScalesResultMenu.menu.items[0]
        val viewScalesResultMenu2 = navigator.navigate(viewScalesItem2.id)
        verifyNavResult(viewScalesResultMenu2, "View Scales", "View Scales detailed", actionName = "v")

        val backItem2 = viewScalesResultMenu2.menu.backItemView!!
        val backToScalesResultMenu2 = navigator.navigate(backItem2.id)
        verifyNavResult(backToScalesResultMenu2, "Select one of the Scales Commands", "View Scales")

        val backToTopItem = backToScalesResultMenu2.menu.backItemView!!
        val backToTopItemMainMenuResult = navigator.navigate(backToTopItem.id)
        verifyNavResult(backToTopItemMainMenuResult, "Main Menu", "Scales Commands", false)


        val sameResult = navigator.navigate(backToTopItem.id)
        verifyNavResult(sameResult, "Main Menu", "Scales Commands", false)

        val sameResult2 = navigator.navigate(UUID.randomUUID().toString())
        verifyNavResult(sameResult2, "Main Menu", "Scales Commands", false)

        val sameResult3 = navigator.navigate(viewScalesItem2.id)
        verifyNavResult(sameResult3, "Main Menu", "Scales Commands", false)

    }


    @Test
    internal fun canNavigate() {
        val menuLayout = menuLayout(
            "Main Menu",
            ItemLayout.itemLayout("Back"),
            ItemLayout.itemLayout(
                "Scales Commands", description = "Select one of the Scales Commands",
                children = listOf(
                    ItemLayout.itemLayout(
                        "View Scales", action = TestCommand("v"),
                        children = listOf(
                            ItemLayout.itemLayout(
                                "View Scales detailed", action = TestCommand("d")
                            )
                        )
                    )
                )
            ),
            ItemLayout.itemLayout("Common Commands", action = TestCommand("c")))

        val navigator = LayoutBasedMenuNavigator(menuLayout)
        val mainMenu = navigator.createMainMenu()

        assertFalse(navigator.canNavigate(UUID.randomUUID().toString()))
        assertFalse(navigator.canNavigate(UUID.randomUUID().toString()))
        assertTrue(navigator.canNavigate(mainMenu.items[0].id))
        assertTrue(navigator.canNavigate(mainMenu.items[1].id))

        val sub = navigator.navigate(mainMenu.items[0].id)
        assertFalse(navigator.canNavigate(mainMenu.items[0].id))
        assertFalse(navigator.canNavigate(mainMenu.items[1].id))
        assertTrue(navigator.canNavigate(sub.menu.items[0].id))
        assertTrue(navigator.canNavigate(sub.menu.backItemView!!.id))

        val subsub = navigator.navigate(sub.menu.items[0].id)
        assertFalse(navigator.canNavigate(mainMenu.items[0].id))
        assertFalse(navigator.canNavigate(mainMenu.items[1].id))
        assertFalse(navigator.canNavigate(sub.menu.items[0].id))
        assertTrue(navigator.canNavigate(subsub.menu.backItemView!!.id))
        assertTrue(navigator.canNavigate(subsub.menu.items[0].id))
    }

    private fun verifyNavResult(
        result: NavigationResult,
        menuDescription: String,
        firstItemName: String,
        backItemPresent: Boolean = true,
        actionName: String? = null,
    ) {
        actionName?.let {
            assertNotNull(result.action)
            assertInstanceOf(TestCommand::class.java, result.action)
            assertEquals(actionName, result.action!!.arguments.getOrNull())
        } ?: assertNull(result.action)



        // Menu to display
        val scalesResultMenu = result.menu
        val viewScalesItem = scalesResultMenu.items[0]
        assertEquals(menuDescription, scalesResultMenu.description)
        assertEquals(firstItemName, viewScalesItem.name)
        assertThat(viewScalesItem.id).isNotEmpty()

        if (backItemPresent) {
            assertNotNull(scalesResultMenu.backItemView)
        }else {
            assertNull(scalesResultMenu.backItemView)
        }
    }


}
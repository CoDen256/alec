package coden.menu

import coden.menu.ItemLayout.Companion.itemLayout
import coden.menu.MenuLayout.Companion.menuLayout
import coden.fsm.BaseCommand
import coden.menu.InvalidBackItemException
import coden.menu.InvalidMenuLayoutException
import com.google.common.truth.Truth.assertThat
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test


internal class MenuLayoutTest {
    @Test
    fun createBackItem() {
        val layout = itemLayout(name = "Back")
        assertNotNull(itemLayout(name = ""))
        assertEquals("Back", layout.name)
        assertThat( layout.children).isEmpty()
        assertNull( layout.description)
        assertNull( layout.action)
    }

    @Test
    internal fun createItem() {
        val layout = itemLayout(name = "Common",
            description = "Common Commands",
            action = TestCommand(),
            children = listOf(itemLayout(name = "Help"), itemLayout("Start"))
        )
        assertNotNull(layout)
        assertEquals("Common", layout.name)
        assertEquals( "Common Commands", layout.description)
        assertInstanceOf(TestCommand::class.java, layout.action)
        assertThat(layout.children).isNotEmpty()
        assertEquals("Help", layout.children[0].name)
        assertEquals("Start", layout.children[1].name)
    }

    @Test
    fun createMenu() {
        val menu = menuLayout(
            "Main Menu", itemLayout("Back"),
            itemLayout("Common", "Common Commands",
                itemLayout("Start", action = TestCommand()),
                itemLayout("Help", action = TestCommand())
            ),
            itemLayout("Scales", "Scale Commands", action = TestCommand()),
            itemLayout("Rater", "Rater Commands", action = TestCommand()),
            itemLayout("Factors", "Factor Commands", action = TestCommand()),
        )

        assertThat(menu.items).isNotEmpty()
        assertEquals(4, menu.items.size)

    }

    @Test
    internal fun invalidBackItem() {
        assertThrows(InvalidBackItemException::class.java) {
            menuLayout("Main Menu", itemLayout("Back", action = TestCommand()), itemLayout("Inner child"))
        }
        assertThrows(InvalidBackItemException::class.java) {
            menuLayout("Main Menu", itemLayout("Back", "Description"), itemLayout("Inner child"))
        }
        assertThrows(InvalidBackItemException::class.java) {
            menuLayout("Main Menu", itemLayout("Back", children = listOf(itemLayout("Inner child"))), itemLayout("Inner child"))
        }

    }

    @Test
    internal fun invalidMenuLayout() {
        assertThrows(InvalidMenuLayoutException::class.java) {
            menuLayout("Main Menu", itemLayout("Back"))
        }
    }
}

class TestCommand(args: String?=null): BaseCommand(args)
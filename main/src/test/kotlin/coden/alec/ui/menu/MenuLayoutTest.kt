package coden.alec.ui.menu

import coden.alec.ui.menu.ItemLayout.Companion.itemLayout
import coden.alec.ui.menu.MenuLayout.Companion.menuLayout
import coden.fsm.BaseCommand
import com.google.common.truth.Truth.assertThat
import net.bytebuddy.pool.TypePool.Empty
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
            action = EmptyCommand(),
            children = listOf(itemLayout(name = "Help"), itemLayout("Start"))
        )
        assertNotNull(layout)
        assertEquals("Common", layout.name)
        assertEquals( "Common Commands", layout.description)
        assertInstanceOf(EmptyCommand::class.java, layout.action)
        assertThat(layout.children).isNotEmpty()
        assertEquals("Help", layout.children[0].name)
        assertEquals("Start", layout.children[1].name)
    }

    @Test
    fun createMenu() {
        val menu = menuLayout(
            "Main Menu", itemLayout("Back"),
            itemLayout("Common", "Common Commands",
                itemLayout("Start", action = EmptyCommand()),
                itemLayout("Help", action = EmptyCommand())
            ),
            itemLayout("Scales", "Scale Commands", action = EmptyCommand()),
            itemLayout("Rater", "Rater Commands", action = EmptyCommand()),
            itemLayout("Factors", "Factor Commands", action = EmptyCommand()),
        )

        assertThat(menu.items).isNotEmpty()
        assertEquals(4, menu.items.size)

    }

    @Test
    internal fun invalidBackItem() {
        assertThrows(InvalidBackItemException::class.java) {
            menuLayout("Main Menu", itemLayout("Back", action = EmptyCommand()))
        }
        assertThrows(InvalidBackItemException::class.java) {
            menuLayout("Main Menu", itemLayout("Back", "Description"))
        }
        assertThrows(InvalidBackItemException::class.java) {
            menuLayout("Main Menu", itemLayout("Back", children = listOf(itemLayout("Inner child"))))
        }

    }
}

class EmptyCommand: BaseCommand()
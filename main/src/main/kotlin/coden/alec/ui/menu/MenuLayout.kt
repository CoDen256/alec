package coden.alec.ui.menu

import coden.fsm.Command

class MenuLayout private constructor(
    val name: String,
    val description: String,
    val items: List<MenuLayout>,
    val action: Command?
) {
    companion object {
        fun layout(name: String, description: String = name, vararg items: MenuLayout): MenuLayout =
            MenuLayout(name, description, items.toList(), null)

        fun action(name: String, action: Command): MenuLayout =
            MenuLayout(name, name, emptyList(), action)
    }
}

class BackLayout private constructor(
    val description: String
) {
    companion object {
        fun back(description: String): BackLayout
            = BackLayout(description)
    }
}
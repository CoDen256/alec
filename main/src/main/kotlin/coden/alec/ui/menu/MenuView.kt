package coden.alec.ui.menu

import coden.fsm.Command

class MenuView(
    val description: String,
    val itemRows: List<MenuItemView>,
    val backItem: MenuItemView?,
    val action: Command? = null
)

class MenuItemView(
    val name: String,
    val id: String
)
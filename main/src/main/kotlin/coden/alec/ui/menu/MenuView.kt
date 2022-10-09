package coden.alec.ui.menu

import coden.fsm.Command


class NavigationResult(
    val menu: MenuView,
    val action: Command?,
    val backItemView: ItemView?
)

class MenuView(
    val description: String,
    val itemRows: List<ItemView>,
)

class ItemView(
    val name: String,
    val id: String
)
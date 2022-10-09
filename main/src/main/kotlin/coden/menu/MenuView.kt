package coden.menu

import coden.fsm.Command


class NavigationResult(
    val menu: MenuView,
    val action: Command?,
)

class MenuView(
    val description: String,
    val items: List<ItemView>,
    val backItemView: ItemView?
)

class ItemView(
    val name: String,
    val id: String
)
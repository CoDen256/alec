package coden.alec.ui.menu

class MenuView(
    val description: String,
    val itemRows: List<MenuItemView>,
    val backItem: MenuItemView?
)

class MenuItemView(
    val name: String,
    val id: String
)
package coden.alec.ui.menu

import coden.fsm.Command

class MenuView(
    val description: String,
    val itemRows: List<ItemView>,
)

class ItemView(
    val name: String,
    val id: String
)
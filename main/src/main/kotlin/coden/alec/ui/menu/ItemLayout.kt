package coden.alec.ui.menu

import coden.fsm.Command

class ItemLayout private constructor(val name: String, val description: String?, val children: List<ItemLayout>, val action: Command?) {


    companion object {
        fun itemLayout(
            name: String, description: String? = null,
            children: List<ItemLayout> = listOf(),
            action: Command? = null
        ): ItemLayout {
            return ItemLayout(name, description, children, action)
        }

        fun itemLayout(
            name: String, description: String? = null,
            vararg children: ItemLayout,
            action: Command? = null
        ): ItemLayout {
            return ItemLayout(name, description, children.toList(), action)
        }
    }

}

package coden.menu

import coden.fsm.Command

class MenuLayout private constructor(
    val description: String,
    val backItem: ItemLayout,
    val items: List<ItemLayout>,
) {
    companion object {
        fun menuLayout(name: String, backLayout: ItemLayout, vararg items: ItemLayout): MenuLayout {
            if (backLayout.action != null){
                throw InvalidBackItemException("Back Item should not contain action")
            }
            if (backLayout.description != null){
                throw InvalidBackItemException("Back Item should not contain description")
            }
            if (backLayout.children.isNotEmpty()){
                throw InvalidBackItemException("Back Item should not contain children")
            }
            if (items.isEmpty()){
                throw InvalidMenuLayoutException("Menu should contain at least one child")
            }
            return MenuLayout(name, backLayout, items.toList())
        }
    }
}

class InvalidMenuLayoutException(message: String): RuntimeException(message)
class InvalidBackItemException(msg: String) : RuntimeException(msg)


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

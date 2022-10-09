package coden.alec.ui.menu

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
            return MenuLayout(name, backLayout, items.toList())
        }
    }
}
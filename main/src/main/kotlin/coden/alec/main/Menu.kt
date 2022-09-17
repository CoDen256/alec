package coden.alec.main


data class Menu (
    val name: String,
    val description: String,
    val children: List<Menu>,
    val parent: Menu? = null,
)

data class MenuItem(
    val name: String,
    val description: String,
    val children: List<MenuItem>
) {
    fun toMenu(parent: MenuItem? = null): Menu {
        return Menu(name, description, children.map { it.toMenu(this) }, )
    }
}
package coden.alec.main


data class Menu (
    val name: String,
    val description: String = name,
    val items: List<Menu> = emptyList(),
)
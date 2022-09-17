package coden.alec.main

import coden.fsm.Command


data class Menu (
    val name: String,
    val description: String,
    val items: List<Menu>,
    val action: Command?
) {

    companion object {
        fun set(name: String, description: String = name, vararg items: Menu): Menu =
            Menu(name, description, items.toList(), null)

        fun menu(name: String,
                 description: String,
                 vararg items: Menu): Menu =
            set(name, description, *items)

        fun action(name: String, action: Command): Menu =
            Menu(name, name, emptyList(), action)
    }

}

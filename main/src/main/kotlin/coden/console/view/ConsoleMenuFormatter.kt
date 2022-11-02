package coden.console.view

import coden.menu.MenuView

interface ConsoleMenuFormatter {
    fun format(menu: MenuView): String
}
package coden.display.menu

import coden.fsm.Command

interface MenuPresenter {
    fun displayMenu()
    fun navigate(dest: String): Command?
}
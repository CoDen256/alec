package coden.alec.app.menu

import coden.fsm.Command

interface MenuExecutor {
    fun displayMenu()
    fun navigate(dest: String): Command?
}
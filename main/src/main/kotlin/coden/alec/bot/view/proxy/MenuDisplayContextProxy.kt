package coden.alec.bot.view.proxy

import coden.alec.app.views.MenuDisplay
import coden.alec.bot.context.Context
import coden.alec.bot.context.ContextProxy
import coden.menu.MenuView

open class MenuDisplayContextProxy(contextSupplier: () -> Context, displayFactory: (Context) -> MenuDisplay): ContextProxy<MenuDisplay>(contextSupplier, displayFactory),
    MenuDisplay {

    override fun displayMenu(menu: MenuView) {
        withContext { displayMenu(menu) }
    }
}
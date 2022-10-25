package coden.menu

class LayoutBasedMenuNavigatorFactory(private val menuLayout: MenuLayout) {
    fun newMenuNavigator(): LayoutBasedMenuNavigator {
        return LayoutBasedMenuNavigator(menuLayout)
    }
}
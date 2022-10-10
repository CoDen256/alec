package coden.alec.bot.menu

class TelegramMenuNavigator(
    private val navigator: MenuNavigator,
    private val itemsPerRow: Int = 4
) {
    fun createMain(): Pair<String, ReplyMarkup>{
        return menuViewToTelegramMarkup(navigator.createMain())
    }

    fun navigate(destination: String): Triple<String, ReplyMarkup, Command?> {
        val menuView = navigator.navigate(destination)
        val markup = menuViewToTelegramMarkup(menuView)
        return Triple(markup.first, markup.second, menuView.action)
    }

    private fun menuViewToTelegramMarkup(menuView: MenuView): Pair<String, ReplyMarkup> {
        return menuView.description to menuToMarkup(menuView.itemRows, menuView.backItem)
    }

    private fun menuToMarkup(items: List<MenuItemView>, backView: MenuItemView?): InlineKeyboardMarkup {
        val result = ArrayList<List<InlineKeyboardButton>>()
        generateSequence(0) { n -> n + itemsPerRow }
            .take((items.size + (itemsPerRow - 1)) / itemsPerRow)
            .forEach { tupleIndex ->
                val row = ArrayList<InlineKeyboardButton>()
                (tupleIndex until (tupleIndex + itemsPerRow).coerceAtMost(items.size)).map {
                    row.add(menuItemToButton(items[it]))
                }
                result.add(row)
            }
        backView?.let {
            result.add(listOf(menuItemToButton(it)))
        }
        return InlineKeyboardMarkup.create(result)
    }

    private fun menuItemToButton(item: MenuItemView): InlineKeyboardButton {
        return InlineKeyboardButton.CallbackData(item.name, callbackData = item.id)
    }
}

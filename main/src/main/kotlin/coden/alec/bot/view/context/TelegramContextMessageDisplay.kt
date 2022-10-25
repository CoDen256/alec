package coden.alec.bot.view.context

import coden.alec.bot.context.Context
import coden.alec.bot.context.TelegramChatContext
import coden.alec.bot.context.TelegramMessageContext
import coden.alec.bot.sender.TelegramMessageSender
import coden.alec.bot.view.display.TelegramInlineMenuDisplay
import coden.alec.bot.view.display.TelegramMenuDisplay
import coden.alec.bot.view.display.TelegramMessageDisplay
import coden.alec.bot.view.format.TelegramMenuFormatter
import coden.alec.bot.view.proxy.MenuDisplayContextProxy
import coden.alec.bot.view.proxy.MessageDisplayContextProxy

class TelegramContextMessageDisplay(contextSupplier: () -> Context, messageSenderFactory: (Context)-> TelegramMessageSender)
    : MessageDisplayContextProxy(contextSupplier, {
    TelegramMessageDisplay(TelegramChatContext( it.chatId), messageSenderFactory(it))
})


class TelegramContextMenuDisplay(
    contextSupplier: () -> Context,
    messageSenderFactory: (Context)-> TelegramMessageSender,
    menuFormatterFactory: (Context) -> TelegramMenuFormatter
): MenuDisplayContextProxy(contextSupplier, { ctx ->
    ctx.messageId?.let {
        TelegramInlineMenuDisplay(TelegramMessageContext(ctx.chatId, it), messageSenderFactory(ctx), menuFormatterFactory(ctx))
    } ?:
    TelegramMenuDisplay(TelegramChatContext(ctx.chatId), messageSenderFactory(ctx), menuFormatterFactory(ctx))
})

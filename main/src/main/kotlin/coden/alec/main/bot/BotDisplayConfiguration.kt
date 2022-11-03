package coden.alec.main.bot

import coden.bot.context.Context
import coden.bot.context.ContextObserver
import coden.bot.context.proxy.ContextBasedTelegramMenuDisplay
import coden.bot.context.proxy.ContextBasedTelegramMessageDisplay
import coden.bot.sender.BaseMessageSender
import coden.bot.sender.TelegramMessageSender
import coden.bot.view.format.ReplyMarkupFormatter
import coden.bot.view.format.TelegramMenuFormatter
import coden.display.displays.MenuDisplay
import coden.display.displays.MessageDisplay
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class BotDisplayConfiguration {

    @Bean
    fun contextObserver(): ContextObserver {
        return ContextObserver()
    }

    @Bean
    fun senderFactory(): (Context) -> TelegramMessageSender {
        return { ctx -> BaseMessageSender(ctx.bot) }
    }

    @Bean
    fun menuFormatterFactory(): (Context) -> TelegramMenuFormatter {
        return { ReplyMarkupFormatter(4) }
    }

    @Bean("telegramMenuDisplay")
    fun menuDisplay(
        contextProvider: () -> Context,
        senderFactory: (Context) -> TelegramMessageSender,
        formatterFactory: (Context) -> TelegramMenuFormatter
    ): MenuDisplay {
        return ContextBasedTelegramMenuDisplay(
            contextProvider,
            senderFactory,
            formatterFactory
        )
    }

    @Bean("telegramMessageDisplay")
    fun messageDisplay(
        contextProvider: () -> Context,
        senderFactory: (Context) -> TelegramMessageSender,
        formatterFactory: (Context) -> TelegramMenuFormatter
    ): MessageDisplay {
        return ContextBasedTelegramMessageDisplay(
            contextProvider,
            senderFactory,
        )
    }

}
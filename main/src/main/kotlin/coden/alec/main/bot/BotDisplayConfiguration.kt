package coden.alec.main.bot

import coden.alec.main.annotations.BotEnabled
import coden.bot.context.Context
import coden.bot.context.ContextObserver
import coden.bot.context.proxy.ContextBasedTelegramMenuDisplay
import coden.bot.context.proxy.ContextBasedTelegramMessageDisplay
import coden.bot.sender.BaseMessageSender
import coden.bot.sender.TelegramMessageSender
import coden.bot.view.format.ReplyMarkupFormatter
import coden.bot.view.format.TelegramMenuFormatter
import coden.display.displays.ErrorDisplay
import coden.display.displays.MenuDisplay
import coden.display.displays.MessageDisplay
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@BotEnabled
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

    @Bean
    fun botMenuDisplay(
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

    @Bean
    fun botMessageDisplay(
        contextProvider: () -> Context,
        senderFactory: (Context) -> TelegramMessageSender,
        formatterFactory: (Context) -> TelegramMenuFormatter
    ): MessageDisplay {
        return ContextBasedTelegramMessageDisplay(
            contextProvider,
            senderFactory,
        )
    }

    @Bean("errorDisplay")
    fun errorDisplay(botMessageDisplay: MessageDisplay): ErrorDisplay {
        return botMessageDisplay
    }
}
package coden.alec.main.config

import coden.alec.app.ListScalesController
import coden.alec.app.ListScalesPresenter
import coden.alec.app.states.*
import coden.alec.bot.AlecBot
import coden.alec.bot.presenter.TelegramView
import coden.alec.bot.presenter.View
import coden.alec.console.AlecConsole
import coden.alec.console.ConsoleView
import coden.alec.core.ListScalesResponder
import coden.alec.data.ScaleGateway
import coden.alec.interactors.definer.scale.ListScalesInteractor
import gateway.memory.ScaleInMemoryGateway
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.Async


@Configuration
@EnableConfigurationProperties(AlecBotProperties::class, Messages::class)
class AlecBotConfiguration {


//    @Bean
//    fun scalesGateway(): ScaleGateway {
//        return ScaleInMemoryGateway()
//    }
//
//
//    @Bean
//    fun telegramView(): TelegramView{
//        return TelegramView()
//    }
//
//    @Bean
//    fun consoleResponder(): ListScalesResponder {
//        return ListScalesPresenter(ConsoleView())
//    }
//
//    @Bean("telResponder")
//    fun telegramResponder(telegramView: View): ListScalesResponder {
//        return ListScalesPresenter(telegramView)
//    }
//
//    @Bean("telegram")
//    fun telegramListScaleController(gateway: ScaleGateway, @Qualifier("telResponder") telegramResponder: ListScalesResponder): ListScalesController {
//        return ListScalesController(ListScalesInteractor(gateway), telegramResponder)
//    }
//
//
//    @Bean("console")
//    fun consoleListScaleController(gateway: ScaleGateway): ListScalesController {
//        return ListScalesController(ListScalesInteractor(gateway), consoleResponder())
//    }

//    @Bean
//    @Async
//    fun alecBot(@Qualifier("telegram") telegram: ListScalesController,
//                properties: AlecBotProperties,
//                messages: Messages,
//                telegramView: TelegramView
//
//    ): AlecBot{
//        return AlecBot(telegram,telegramView,  properties.token, messages).also {
//            it.launch()
//        }
//    }
//
//    @Bean
//    @Async
//    fun alecConsole(@Qualifier("console") console: ListScalesController) : AlecConsole{
//        return AlecConsole(console).also {
//            it.launch()
//        }
//    }


    @Bean
    fun view(): View {
        return ConsoleView()
    }

    val fsm = arrayListOf(
        Entry.entry(StartState, StartState, HelpCommand, DisplayStartMessage)
    )
    @Bean
    fun stateExecutor(messages: Messages, view: View): StateExecutor {
        return StateExecutor(StartState,
            fsm,
            view,
            messages
        )
    }
}
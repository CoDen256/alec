package coden.alec.main.config

import coden.alec.app.FiniteStateMachine
import coden.alec.app.FiniteStateMachineTable
import coden.alec.app.ListScalesController
import coden.alec.app.actuator.BaseHelpActuator
import coden.alec.app.actuator.BaseScaleActuator
import coden.alec.app.actuator.HelpActuator
import coden.alec.app.actuator.ScaleActuator
import coden.alec.app.states.*
import coden.alec.app.states.State.*
import coden.alec.bot.AlecBot
import coden.alec.bot.messages.MessageResource
import coden.alec.bot.presenter.TelegramInlineView
import coden.alec.bot.presenter.TelegramView
import coden.alec.bot.presenter.View
import coden.alec.console.ConsoleView
import coden.alec.core.CreateScaleActivator
import coden.alec.core.ListScalesActivator
import coden.alec.data.ScaleGateway
import coden.alec.interactors.definer.scale.CreateScaleInteractor
import coden.alec.interactors.definer.scale.ListScalesInteractor
import coden.alec.main.config.table.HelpTable
import coden.alec.main.config.table.ScaleTable
import gateway.memory.ScaleInMemoryGateway
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.Async


@Configuration
@EnableConfigurationProperties(AlecBotProperties::class, Messages::class)
class AlecBotConfiguration {


    @Bean
    fun scalesGateway(): ScaleGateway {
        return ScaleInMemoryGateway()
    }
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


//
//    @Bean
//    @Async
//    fun alecConsole(@Qualifier("console") console: ListScalesController) : AlecConsole{
//        return AlecConsole(console).also {
//            it.launch()
//        }
//    }


    @Bean
    fun useCaseFactory(scalesGateway: ScaleGateway): UseCaseFactory {
        return object : UseCaseFactory {
            override fun listScales(): ListScalesActivator {
                return ListScalesInteractor(scalesGateway)
            }

            override fun createScale(): CreateScaleActivator {
                return CreateScaleInteractor(scalesGateway)
            }
        }
    }

    @Bean
    fun view(): TelegramView {
        return TelegramView()
    }

    @Bean
    fun scaleActuator(view: View, useCaseFactory: UseCaseFactory, messages: MessageResource): ScaleActuator {
        return BaseScaleActuator(useCaseFactory, view, messages)
    }

    @Bean
    fun helpActuator(view: View, useCaseFactory: UseCaseFactory, messages: MessageResource): HelpActuator {
        return BaseHelpActuator(useCaseFactory, view, messages)
    }


    @Bean
    fun helpFSM(help: HelpActuator): FiniteStateMachineTable {
        return HelpTable(help)
    }

    @Bean
    fun scaleFSM(scale: ScaleActuator): FiniteStateMachineTable {
        return ScaleTable(scale)
    }

    @Bean("fsm")
    fun fsm(tables:List<FiniteStateMachineTable>): FiniteStateMachine {
        return FiniteStateMachine(
            Start, FiniteStateMachineTable(tables.reduce { t1, t2 -> t1 + t2 })
        )
    }

    @Bean
    fun stateExecutor(@Qualifier("fsm") fsm: FiniteStateMachine): StateExecutor {
        return StateExecutor(fsm)
    }

    @Bean
    fun scale(view: TelegramInlineView, useCaseFactory: UseCaseFactory, messages: MessageResource): BaseScaleActuator {
        return BaseScaleActuator(useCaseFactory, view, messages)
    }

    @Bean("messageFSM")
    fun messageFSM(scale: BaseScaleActuator): FiniteStateMachine {
        return FiniteStateMachine(Start,
            FiniteStateMachineTable(
                Entry.entry(Start, ListScalesInlineCommand) {scale.getAndDisplayScales(it); Start}
            )
        )
    }

    @Bean
    fun inlineTelegram(): TelegramInlineView{
        return TelegramInlineView()
    }

    @Bean
    @Async
    fun alecBot(properties: AlecBotProperties, telegramView: TelegramView, inline: TelegramInlineView,
        stateExecutor: StateExecutor,  @Qualifier("messageFSM") messageFSM: FiniteStateMachine
    ): AlecBot {
        return AlecBot(telegramView, inline, properties.token, stateExecutor, messageFSM).also {
            it.launch()
        }
    }

}
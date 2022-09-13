package coden.alec.main.config

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Configuration


@Configuration
@EnableConfigurationProperties(AlecBotProperties::class, Messages::class)
class AlecConfiguration {



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


//    @Bean
//    fun scalesGateway(): ScaleGateway {
//        return ScaleInMemoryGateway()
//    }
//
//    @Bean
//    fun useCaseFactory(scalesGateway: ScaleGateway): UseCaseFactory {
//        return object : UseCaseFactory {
//            override fun listScales(): ListScalesActivator {
//                return ListScalesInteractor(scalesGateway)
//            }
//
//            override fun createScale(): CreateScaleActivator {
//                return CreateScaleInteractor(scalesGateway)
//            }
//        }
//    }
//
//    @Bean("telegramView")
//    fun view(): TelegramView {
//        return TelegramView()
//    }
//
//    @Bean
//    fun scaleActuator(view: View, useCaseFactory: UseCaseFactory, messages: MessageResource): ScaleActuator {
//        return BaseScaleActuator(useCaseFactory, view, messages)
//    }
//
//    @Bean
//    fun helpActuator(view: View, useCaseFactory: UseCaseFactory, messages: MessageResource): HelpActuator {
//        return BaseHelpActuator(useCaseFactory, view, messages)
//    }
//
//
//    @Bean
//    fun helpFSM(help: HelpActuator): FSM {
//        return HelpTable(help)
//    }
//
//    @Bean
//    fun scaleFSM(scale: ScaleActuator): FSM {
//        return ScaleTable(scale)
//    }
//
//    @Bean("fsm")
//    fun fsm(tables:List<FSM>): FSMTable {
//        return FSMTable(
//            Start, FSM(tables.reduce { t1, t2 -> t1 + t2 })
//        )
//    }
//
//    @Bean
//    fun stateExecutor(@Qualifier("fsm") fsm: FSMTable): StateExecutor {
//        return StateExecutor(fsm)
//    }
//
//    @Bean
//    fun scale(view: TelegramInlineView, useCaseFactory: UseCaseFactory, messages: MessageResource): BaseScaleActuator {
//        return BaseScaleActuator(useCaseFactory, view, messages)
//    }
//
//    @Bean("messageFSM")
//    fun messageFSM(scale: BaseScaleActuator): FSMTable {
//        return FSMTable(Start,
//            FSM(
//                Entry.entry(Start, ListScalesInlineCommand) {scale.getAndDisplayScales(it); Start}
//            )
//        )
//    }
//
//    @Bean
//    fun inlineTelegram(): TelegramInlineView {
//        return TelegramInlineView()
//    }
//
//    @Bean
//    @Async
//    fun alecBot(properties: AlecBotProperties, telegramView: TelegramView, inline: TelegramInlineView,
//                stateExecutor: StateExecutor, @Qualifier("messageFSM") messageFSM: FSMTable
//    ): AlecBot {
//        return AlecBot(telegramView, inline, properties.token, stateExecutor, messageFSM).also {
//            it.launch()
//        }
//    }

}
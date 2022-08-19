package coden.alec.main.config

import coden.alec.app.states.*
import coden.alec.app.states.Entry.Companion.entry
import coden.alec.bot.presenter.View
import coden.alec.console.ConsoleView
import coden.alec.core.CreateScaleActivator
import coden.alec.core.ListScalesActivator
import coden.alec.data.ScaleGateway
import coden.alec.interactors.definer.scale.CreateScaleInteractor
import coden.alec.interactors.definer.scale.ListScalesInteractor
import gateway.memory.ScaleInMemoryGateway
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


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
    fun view(): View {
        return ConsoleView()
    }

    val fsm = arrayListOf(
        entry(StartState, StartState,
            eq(HelpCommand),
            DisplayHelpMessage),

        entry(StartState, StartState,
            eq(ListScalesCommand),
            GetScalesAndDisplay),

        entry(StartState, StartState,
            eq(CreateScaleCommand) * CreateScaleArgsAreValid,
            CreateScaleAndDisplay
        ),
        entry(StartState, StartState,
            eq(CreateScaleCommand) * not(CreateScaleArgsAreValid),
            FailOnInvalidScale
        ),

//        entry(StartState,             CreateScaleNoArgumentCommand,   ScaleWaitForNameState,    CreateScalePromptName),
//        entry(ScaleWaitForNameState,  TextCommand,                    ScaleWaitForNameState,    CreateScalePromptName)
    )

    @Bean
    fun stateExecutor(useCaseFactory: UseCaseFactory, messages: Messages, view: View): StateExecutor {
        return StateExecutor(
            StartState,
            fsm,
            view,
            useCaseFactory,
            messages
        )
    }
}
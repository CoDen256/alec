package coden.alec.main.config

import coden.alec.app.FiniteStateMachine
import coden.alec.app.actuator.BaseHelpActuator
import coden.alec.app.actuator.BaseScaleActuator
import coden.alec.app.actuator.HelpActuator
import coden.alec.app.actuator.ScaleActuator
import coden.alec.app.states.*
import coden.alec.app.states.Entry.Companion.entry
import coden.alec.app.states.State.*
import coden.alec.bot.messages.MessageResource
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

    @Bean
    fun scaleActuator(view: View, useCaseFactory: UseCaseFactory, messages: MessageResource): ScaleActuator {
        return BaseScaleActuator(useCaseFactory, view, messages)
    }

    @Bean
    fun helpActuator(view: View, useCaseFactory: UseCaseFactory, messages: MessageResource): HelpActuator {
        return BaseHelpActuator(useCaseFactory, view, messages)
    }


    @Bean
    fun stateExecutor(fsm: FiniteStateMachine): StateExecutor {
        return StateExecutor(fsm)
    }


    @Bean
    fun fsm(
        scale: ScaleActuator,
        help: HelpActuator
    ): FiniteStateMachine {
        return FiniteStateMachine(
            Start, arrayListOf(

                entry(Start, HelpCommand, Start, help::displayHelp),
                entry(Start, ListScalesCommand, Start, scale::getAndDisplayScales),

                entry(Start, CreateScaleCommand::class, Start, scale::createAndDisplayScale),

                entry(Start, CreateScaleCommandNoArgs, WaitScaleName, scale::displayScaleNamePrompt),
                entry(WaitScaleName, TextCommand::class, WaitScaleUnit, {
                    scale.handleScaleName(it) &&
                    scale.displayScaleUnitPrompt(it)
                }),
                entry(WaitScaleUnit, TextCommand::class, WaitScaleDivision, {
                    scale.handleScaleUnit(it) &&
                    scale.displayScaleDivisionsPrompt(it)
                }),
                entry(WaitScaleDivision, TextCommand::class, WaitScaleDivision, {
                    scale.handleScaleDivisions(it) &&
                    scale.createAndDisplayScale(it)
                })
            )
        )
    }

}
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

                entry(Start, HelpCommand) { help.displayHelp(it); Start },

                entry(Start, ListScalesCommand) {scale.getAndDisplayScales(it); Start},

                entry(Start, CreateScaleCommand::class) {scale.createAndDisplayScale(it); Start},

                entry(Start, CreateScaleCommandNoArgs) {scale.displayScaleNamePrompt(it); WaitScaleName},

                entry(WaitScaleName, TextCommand::class) { when {
                    scale.isValidScaleName(it) -> {
                        scale.handleScaleName(it)
                        scale.displayScaleUnitPrompt(it)
                        WaitScaleUnit
                    }
                    else -> {
                        scale.rejectScaleName(it)
                        scale.displayScaleNamePrompt(it)
                        WaitScaleName
                    }
                } },
                entry(WaitScaleUnit, TextCommand::class) { when {
                    !scale.isValidScaleUnit(it) -> {
                        scale.handleScaleUnit(it)
                        scale.displayScaleDivisionsPrompt(it)
                        WaitScaleDivision
                    }
                    else -> {
                        scale.rejectScaleUnit(it)
                        scale.displayScaleUnitPrompt(it)
                        WaitScaleUnit
                    }
                } },

                entry(WaitScaleDivision, TextCommand::class) { when {
                    !scale.isValidScaleDivisions(it) -> {
                        scale.rejectScaleDivisions(it)
                        scale.displayScaleDivisionsPrompt(it)
                        WaitScaleDivision
                    }
                    !scale.isValidScale(it) -> {
                        scale.rejectScale(it)
                        Start
                    }
                    else -> {
                        scale.handleScaleDivisions(it)
                        scale.createAndDisplayScale(it)
                        Start
                    }
                } },
            )
        )
    }

}
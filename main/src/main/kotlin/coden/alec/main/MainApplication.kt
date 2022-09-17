package coden.alec.main

import coden.alec.app.actuators.BaseHelpActuator
import coden.alec.app.actuators.BaseScaleActuator
import coden.alec.app.fsm.Start
import coden.alec.app.messages.MessageResource
import coden.alec.bot.AlecTelegramBot
import coden.alec.bot.MenuControllerFactory
import coden.alec.bot.TelegramContext
import coden.alec.bot.TelegramView
import coden.alec.console.ConsoleView
import coden.alec.core.CreateScaleActivator
import coden.alec.core.ListScalesActivator
import coden.alec.core.UseCaseFactory
import coden.alec.interactors.definer.scale.CreateScaleInteractor
import coden.alec.interactors.definer.scale.ListScalesInteractor
import coden.alec.main.config.AlecBotProperties
import coden.alec.main.config.table.HelpTable
import coden.alec.main.config.table.ScaleTable
import coden.fsm.FSM
import coden.fsm.StateExecutor
import gateway.memory.ScaleInMemoryGateway
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class MainApplication

fun main(args: Array<String>) {
//    runApplication<MainApplication>(*args)
    val messages = MessageResource()
    messages.errorMessage = "error"
    messages.startMessage = "start"
    messages.listScalesMessage = "list scales"
    messages.listScalesEmptyMessage = "empty"
    val botProperties = AlecBotProperties()
    botProperties.token = "5402767430:AAEXVe8s8IAow9z9Ip69NCU9JnUaEHIgcrw"

    val scalesGateway = ScaleInMemoryGateway()
    val useCaseFactory = object : UseCaseFactory {
        override fun listScales(): ListScalesActivator {
            return ListScalesInteractor(scalesGateway)
        }

        override fun createScale(): CreateScaleActivator {
            return CreateScaleInteractor(scalesGateway)
        }
    }
    val menu = Menu("Main Menu", description="Choose anything",
        listOf(Menu("1"), Menu("2"),
            Menu("3"), Menu("4"),
            Menu("5"),
            Menu("6"),
            Menu("7"),
            Menu("8"),
            Menu("9"),
        )
        )


    val ctx = TelegramContext()
    val telegramView = TelegramView(ctx, menu)
    val consoleView = ConsoleView()


        val view = telegramView
//    val view = consoleView



//        MenuItem(
//            name="Scales",
//                    action = null,
//            desciprion="What you want to do with scales"
//            children = listOf(
//                    MenuItem(
//                        name="List Scale"
//                        action = ListScalesCommand
//                    )
//            )

//        )
//                MenuItem()
//                MenuItem()


    val scaleActuator = BaseScaleActuator(useCaseFactory, view, messages)
    val helpActuator = BaseHelpActuator(useCaseFactory, view, messages)

    val stateExecutor = StateExecutor(FSM(Start, HelpTable(helpActuator) + ScaleTable(scaleActuator)))

    val menuControllerFactory = MenuControllerFactory(
        menu, stateExecutor, messages, 2
    )

    val bot = AlecTelegramBot(botProperties.token, ctx, stateExecutor, menuControllerFactory)
    bot.launch()
}

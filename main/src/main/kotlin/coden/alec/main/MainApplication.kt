package coden.alec.main

import coden.alec.app.actuators.BaseHelpActuator
import coden.alec.app.actuators.BaseScaleActuator
import coden.alec.app.fsm.CreateScaleCommandNoArgs
import coden.alec.app.fsm.HelpCommand
import coden.alec.app.fsm.ListScalesCommand
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
import coden.alec.main.Menu.Companion.action
import coden.alec.main.Menu.Companion.menu
import coden.alec.main.Menu.Companion.set
import coden.alec.main.config.AlecBotProperties
import coden.alec.main.config.table.HelpTable
import coden.alec.main.config.table.ScaleTable
import coden.fsm.FSM
import coden.fsm.StateExecutor
import com.github.kotlintelegrambot.logging.LogLevel
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
    messages.backButtonMessage = "<-- Back"
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

    val menu = menu("Main Menu", "Choose anything from  the main menu",
        set("Common", "Common commands",
            action("Start", action = HelpCommand),
            action("Help", action = HelpCommand)),

        set("Scales", "Scale management",
            action("View all Scales", action = ListScalesCommand),
            action("Create Scale", action = CreateScaleCommandNoArgs)),

        set("Factors", "Factor management",
            action("Start", action = HelpCommand),
            action("Help", action = HelpCommand)),

        set("Raters", "Rater management",
            action("Start", action = HelpCommand),
            action("Help", action = HelpCommand)),
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

    val bot = AlecTelegramBot(botProperties.token, log = LogLevel.Error, ctx, stateExecutor, menuControllerFactory)
    bot.launch()
}

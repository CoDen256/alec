package coden.alec.main

import coden.alec.app.actuators.BaseHelpActuator
import coden.alec.app.actuators.BaseScaleActuator
import coden.alec.app.fsm.CreateScaleCommandNoArgs
import coden.alec.app.fsm.HelpCommand
import coden.alec.app.fsm.ListScalesCommand
import coden.alec.app.fsm.Start
import coden.alec.ui.menu.MenuNavigatorFactory
import coden.alec.app.messages.MessageResource
import coden.alec.bot.*
import coden.alec.bot.menu.TelegramMenuNavigatorManager
import coden.alec.bot.view.TelegramView
import coden.alec.console.ConsoleApp
import coden.alec.console.view.ConsoleView
import coden.alec.core.*
import coden.alec.interactors.definer.scale.CreateScaleInteractor
import coden.alec.interactors.definer.scale.ListScalesInteractor
import coden.alec.ui.menu.MenuLayout.Companion.action
import coden.alec.ui.menu.MenuLayout.Companion.layout
import coden.alec.main.config.AlecBotProperties
import coden.alec.main.config.table.HelpTable
import coden.alec.main.config.table.ScaleTable
import coden.alec.ui.menu.BackItemLayout
import coden.alec.ui.menu.MenuLayout
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
    messages.menuBackMessage = "<-- Back"
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

        override fun deleteScale(): DeleteScaleActivator {
            TODO("Not yet implemented")
        }

        override fun purgeScale(): PurgeScaleActivator {
            TODO("Not yet implemented")
        }

        override fun updateScale(): UpdateScaleActivator {
            TODO("Not yet implemented")
        }
    }

    val menu = layout(
        "Main Menu", "Choose anything from  the main menu",
        layout(
            "Common", "Common commands",
            action("Start", action = HelpCommand),
            action("Help", action = HelpCommand)
        ),
        layout(
            "Scales", "Scale management",
            action("View all Scales", action = ListScalesCommand),
            action("Create Scale", action = CreateScaleCommandNoArgs)
        ),
        layout(
            "Factors", "Factor management",
            action("Start", action = HelpCommand),
            action("Help", action = HelpCommand)
        ),
        layout(
            "Raters", "Rater management",
            action("Start", action = HelpCommand),
            action("Help", action = HelpCommand)
        )
    )


    val ctx = TelegramContext()
    val telegramView = TelegramView(ctx, menu)
    val consoleView = ConsoleView()


//        val view = telegramView
    val view = consoleView


    val scaleActuator = BaseScaleActuator(useCaseFactory, view, messages)
    val helpActuator = BaseHelpActuator(useCaseFactory, view, messages)

    val stateExecutor = StateExecutor(FSM(Start, HelpTable(helpActuator) + ScaleTable(scaleActuator)))

    val menuNagivatorFactory = MenuNavigatorFactory(
        menu, BackItemLayout.back(messages.menuBackMessage)
    )

    val manager = TelegramMenuNavigatorManager(menuNagivatorFactory)

//    val bot = AlecTelegramBot(botProperties.token, log = LogLevel.Error, ctx, stateExecutor, manager)
//    bot.launch()

    val app = ConsoleApp(consoleView, stateExecutor, menuNagivatorFactory)
    app.start()
}

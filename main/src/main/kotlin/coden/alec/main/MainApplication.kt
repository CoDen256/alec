package coden.alec.main

import coden.alec.app.actuators.BaseHelpActuator
import coden.alec.app.actuators.BaseScaleActuator
import coden.alec.app.fsm.CreateScaleCommandNoArgs
import coden.alec.app.fsm.HelpCommand
import coden.alec.app.fsm.ListScalesCommand
import coden.alec.app.fsm.Start
import coden.alec.app.menu.BaseMenuPresenter
import coden.alec.app.menu.MenuPresenter
import coden.alec.app.messages.MessageResource
import coden.alec.bot.run.BotRunner
import coden.alec.bot.context.Context
import coden.alec.bot.context.ContextObserver
import coden.alec.bot.context.proxy.ContextBasedTelegramMenuDisplay
import coden.alec.bot.context.proxy.ContextBasedTelegramMessageDisplay
import coden.alec.bot.menu.TelegramAggregatedMenuNavigator
import coden.alec.bot.sender.BaseMessageSender
import coden.alec.bot.sender.TelegramMessageSender
import coden.alec.bot.view.format.ReplyMarkupFormatter
import coden.alec.bot.view.format.TelegramMenuFormatter
import coden.alec.console.ConsoleApp
import coden.alec.console.menu.ConsoleMenuReindexingNavigator
import coden.alec.console.view.ConsoleDisplay
import coden.alec.console.view.ConsoleMenuDisplay
import coden.alec.console.view.ConsoleMenuFormatter
import coden.alec.core.*
import coden.alec.interactors.definer.scale.CreateScaleInteractor
import coden.alec.interactors.definer.scale.ListScalesInteractor
import coden.alec.main.config.AlecBotProperties
import coden.alec.main.config.table.HelpTable
import coden.alec.main.config.table.ScaleTable
import coden.fsm.FSM
import coden.fsm.StateBasedCommandExecutor
import coden.menu.ItemLayout.Companion.itemLayout
import coden.menu.LayoutBasedMenuNavigatorFactory
import coden.menu.MenuLayout.Companion.menuLayout
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

    val menu = menuLayout(
        "Choose anything from  the main menu", itemLayout("Back"),
        itemLayout(
            "Common", "Common commands",
            itemLayout("Start", action = HelpCommand),
            itemLayout("Help", "Help commands", itemLayout("Help again", action = HelpCommand))
        ),
        itemLayout(
            "Scales", "Scale management",
            itemLayout("Display all Scales", action = ListScalesCommand),
            itemLayout("Create Scale", action = CreateScaleCommandNoArgs)
        ),
        itemLayout(
            "Factors", "Factor management",
            itemLayout("Start", action = HelpCommand),
            itemLayout("Help", action = HelpCommand)
        ),
        itemLayout(
            "Raters", "Rater management",
            itemLayout("Start", action = HelpCommand),
            itemLayout("Help", action = HelpCommand)
        )
    )

    val messageSenderFactory: (Context) -> TelegramMessageSender =
        {BaseMessageSender(it.bot)}
    val menuFormatterFactory: (Context) -> TelegramMenuFormatter =
        { ReplyMarkupFormatter(4) }

    val contextObserver = ContextObserver()
    val contextProvider = contextObserver

    val telMessageDisplay = ContextBasedTelegramMessageDisplay(contextProvider, messageSenderFactory)
    val telMenuDisplay = ContextBasedTelegramMenuDisplay(contextProvider, messageSenderFactory, menuFormatterFactory)


    val consoleMenuFormatter = ConsoleMenuFormatter()
    val consoleDisplay = ConsoleDisplay()
    val consoleMenuDisplay = ConsoleMenuDisplay(consoleMenuFormatter)



    val display = telMessageDisplay
//    val display = consoleDisplay

//    val menuDisplay = telMenuDisplay
    val menuDisplay = consoleMenuDisplay


    val scaleActuator = BaseScaleActuator(useCaseFactory, display, messages)
    val helpActuator = BaseHelpActuator(useCaseFactory, display, messages)

    val commandExecutor = StateBasedCommandExecutor(FSM(Start, HelpTable(helpActuator) + ScaleTable(scaleActuator)))

    val layoutBasedMenuNavigatorFactory = LayoutBasedMenuNavigatorFactory(menu)

    val consoleNavigator = ConsoleMenuReindexingNavigator(layoutBasedMenuNavigatorFactory.newMenuNavigator())
    val telNavigator = TelegramAggregatedMenuNavigator(layoutBasedMenuNavigatorFactory)

//    val menuNavigator = telNavigator
    val menuNavigator = consoleNavigator


    val menuPresenter: MenuPresenter = BaseMenuPresenter(
        display, menuDisplay, menuNavigator
    )

    val bot = BotRunner(botProperties.token, log = LogLevel.Error, contextObserver, commandExecutor, menuPresenter)
    bot.launch()

    val app = ConsoleApp(commandExecutor, menuPresenter)
    app.start()
}

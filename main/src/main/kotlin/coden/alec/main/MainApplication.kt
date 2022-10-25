package coden.alec.main

import coden.alec.app.actuators.BaseHelpActuator
import coden.alec.app.actuators.BaseScaleActuator
import coden.alec.app.fsm.CreateScaleCommandNoArgs
import coden.alec.app.fsm.HelpCommand
import coden.alec.app.fsm.ListScalesCommand
import coden.alec.app.fsm.Start
import coden.alec.app.menu.MenuExecutor
import coden.alec.app.menu.MenuNavigatorFactory
import coden.alec.app.messages.MessageResource
import coden.alec.bot.AlecTelegramBot
import coden.alec.bot.menu.TelegramMenuExecutor
import coden.alec.bot.menu.TelegramMenuNavigatorDirector
import coden.alec.bot.sender.BaseMessageSender
import coden.alec.bot.sender.TelegramMessageSender
import coden.alec.bot.view.*
import coden.alec.bot.view.format.ReplyMarkupFormatter
import coden.alec.bot.view.format.TelegramMenuFormatter
import coden.alec.console.ConsoleApp
import coden.alec.console.menu.ConsoleMenuExecutor
import coden.alec.console.menu.ConsoleMenuReindexingNavigator
import coden.alec.console.view.ConsoleMenuFormatter
import coden.alec.console.view.ConsoleMenuView
import coden.alec.console.view.ConsoleView
import coden.alec.core.*
import coden.alec.interactors.definer.scale.CreateScaleInteractor
import coden.alec.interactors.definer.scale.ListScalesInteractor
import coden.alec.main.config.AlecBotProperties
import coden.alec.main.config.table.HelpTable
import coden.alec.main.config.table.ScaleTable
import coden.fsm.FSM
import coden.fsm.StateExecutor
import coden.menu.ItemLayout.Companion.itemLayout
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
            itemLayout("View all Scales", action = ListScalesCommand),
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
        {ReplyMarkupFormatter(4)}

    val contextHolder = ContextData()
    val mainTelegramView = ViewController(contextHolder) {
        CommonTelegramView(TelegramChatContext( it.chatId), messageSenderFactory(it))
    }
    val menuTelegramView = MenuViewController() {
        val ctx = contextHolder.context
        ctx.messageId?.let {
            TelegramInlineMenuView(TelegramMessageContext(ctx.chatId, it), messageSenderFactory(ctx), menuFormatterFactory(ctx))
        } ?: TelegramMenuView(TelegramChatContext(ctx.chatId), messageSenderFactory(ctx), menuFormatterFactory(ctx))
    }


    val consoleView = ConsoleView()
    val consoleMenuView = ConsoleMenuView(ConsoleMenuFormatter())



    val view = mainTelegramView
//    val view = consoleView
//
    val menuView = menuTelegramView
//    val menuView = consoleMenuView


    val scaleActuator = BaseScaleActuator(useCaseFactory, view, messages)
    val helpActuator = BaseHelpActuator(useCaseFactory, view, messages)

    val stateExecutor = StateExecutor(FSM(Start, HelpTable(helpActuator) + ScaleTable(scaleActuator)))

    val menuNagivatorFactory = MenuNavigatorFactory(menu)

    val manager = TelegramMenuNavigatorDirector(menuNagivatorFactory)


    val consoleMenuExecutor = ConsoleMenuExecutor(
        view, menuView,
        ConsoleMenuReindexingNavigator(menuNagivatorFactory.mainMenuNavigator())
    )

    val telMenuExecutor = TelegramMenuExecutor(
        view, menuView,
        manager
    )

    val menuExecutor: MenuExecutor = telMenuExecutor

    val bot = AlecTelegramBot(botProperties.token, log = LogLevel.Error, contextHolder, stateExecutor, menuExecutor)
    bot.launch()

    val app = ConsoleApp(stateExecutor, menuExecutor)
    app.start()
}

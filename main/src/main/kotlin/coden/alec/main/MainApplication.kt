package coden.alec.main

import coden.alec.console.*
import coden.alec.core.*
import coden.console.*
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication


@SpringBootApplication
class MainApplication

fun main(args: Array<String>) {
    runApplication<MainApplication>(*args)
//    val commandNames = ConsoleCommandNamesResource().apply {
//        startCommand = "start"
//        helpCommand = "help"
//        textCommand = "text"
//        navCommand = "nav"
//        listScalesCommand = "list_scales"
//        createScaleCommand = "create_scales"
//    }
//
//    val messages = MessageResource()
//    messages.errorMessage = "error"
//    messages.startMessage = "start"
//    messages.listScalesMessage = "list scales"
//    messages.listScalesEmptyMessage = "empty"
//    messages.menuBackMessage = "<-- Back"
//    val botProperties = AlecBotProperties()
//    botProperties.token = "5402767430:AAEXVe8s8IAow9z9Ip69NCU9JnUaEHIgcrw"
//
//    val scalesGateway = ScaleInMemoryGateway()
//    val useCaseFactory = object : UseCaseFactory {
//        override fun listScales(): ListScalesActivator {
//            return ListScalesInteractor(scalesGateway)
//        }
//
//        override fun createScale(): CreateScaleActivator {
//            return CreateScaleInteractor(scalesGateway)
//        }
//
//        override fun deleteScale(): DeleteScaleActivator {
//            TODO("Not yet implemented")
//        }
//
//        override fun purgeScale(): PurgeScaleActivator {
//            TODO("Not yet implemented")
//        }
//
//        override fun updateScale(): UpdateScaleActivator {
//            TODO("Not yet implemented")
//        }
//    }
//
//    val menu = menuLayout(
//        "Choose anything from  the main menu", itemLayout("Back"),
//        itemLayout(
//            "Common", "Common commands",
//            itemLayout("Start", action = HelpCommand),
//            itemLayout("Help", "Help commands", itemLayout("Help again", action = HelpCommand))
//        ),
//        itemLayout(
//            "Scales", "Scale management",
//            itemLayout("Display all Scales", action = ListScalesCommand),
//            itemLayout("Create Scale", action = CreateScaleCommandNoArgs)
//        ),
//        itemLayout(
//            "Factors", "Factor management",
//            itemLayout("Start", action = HelpCommand),
//            itemLayout("Help", action = HelpCommand)
//        ),
//        itemLayout(
//            "Raters", "Rater management",
//            itemLayout("Start", action = HelpCommand),
//            itemLayout("Help", action = HelpCommand)
//        )
//    )
//
//    val messageSenderFactory: (Context) -> TelegramMessageSender =
//        { BaseMessageSender(it.bot) }
//    val menuFormatterFactory: (Context) -> TelegramMenuFormatter =
//        { ReplyMarkupFormatter(4) }
//
//    val contextObserver = ContextObserver()
//    val contextProvider = contextObserver
//
//    val telMessageDisplay = ContextBasedTelegramMessageDisplay(contextProvider, messageSenderFactory)
//    val telMenuDisplay = ContextBasedTelegramMenuDisplay(contextProvider, messageSenderFactory, menuFormatterFactory)
//
//
//    val consoleMenuFormatter = BaseConsoleMenuFormatter()
//    val consoleDisplay = ConsoleMessageDisplay()
//    val consoleMenuDisplay = ConsoleMenuDisplay(consoleMenuFormatter)
//
//
//
////    val display = telMessageDisplay
//    val display = consoleDisplay
////
////    val menuDisplay = telMenuDisplay
//    val menuDisplay = consoleMenuDisplay
//
//
//    val scaleActuator = BaseScaleActuator(useCaseFactory, display, messages)
//    val helpActuator = BaseHelpActuator(useCaseFactory, display, messages)
//
//    val commandExecutor = LoggingCommandExecutor(FSM(Start, HelpTable(helpActuator) + ScaleTable(scaleActuator)))
//
//    val layoutBasedMenuNavigatorFactory = LayoutBasedMenuNavigatorFactory(menu)
//
//    val consoleNavigator = ConsoleMenuReindexingNavigator(layoutBasedMenuNavigatorFactory.newMenuNavigator())
//    val telNavigator = TelegramAggregatedMenuNavigator(layoutBasedMenuNavigatorFactory)
//
////    val menuNavigator = telNavigator
//    val menuNavigator = consoleNavigator
//
//
//    val menuPresenter: MenuPresenter = BaseMenuPresenter(
//        display, menuDisplay, menuNavigator
//    )
//
//    val telConfigurator: BotDispatcherConfigurator = AlecBotConfigurator(contextObserver, commandExecutor, menuPresenter, commandNames)
//    val botFactory: BotFactory = BaseBotFactory(telConfigurator)
//
//    val consoleConfigurator: ConsoleDispatcherConfigurator = AlecConsoleConfigurator(commandExecutor, menuPresenter, commandNames)
//    val commandParser: CommandParser = AliasBasedCommandParser(
//        listOf(object: AliasMapper {
//            override fun canMap(input: String): Boolean {
//                return true
//            }
//
//            override fun map(input: String): String {
//                return when {
//                    input.startsWith("~") -> input.replace("~", "/${commandNames.navCommand} ")
//                    input.first().isDigit() -> "/${commandNames.navCommand} $input"
//                    input.startsWith("!") -> input.replace("!", "/${commandNames.textCommand} ")
//                    input.startsWith("/") -> input
//                    else -> "/${commandNames.textCommand} $input"
//                }
//            }
//        })
//    )
//    val commandReader: CommandReader = ConsoleCommandReader()
//    val dispatcherBuilder: ConsoleDispatcherBuilder = BaseConsoleDispatcherBuilder()
//
//    val botRunner: AppRunner = BotRunnerAdapter(BotRunner(
//         BotConfigurationParameters(botProperties.token, log = LogLevel.Error), botFactory
//    ))
//    val consoleRunner = ConsoleRunnerAdapter(ConsoleRunner(commandParser, commandReader, dispatcherBuilder, consoleConfigurator, display))
//
////    val runner: AppRunner = botRunner
//    val runner: AppRunner = consoleRunner
//
//    runner.run()
}

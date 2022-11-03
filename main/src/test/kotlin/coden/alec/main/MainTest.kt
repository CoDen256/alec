package coden.alec.main

import coden.alec.app.actuators.BaseHelpActuator
import coden.alec.app.actuators.BaseScaleActuator
import coden.alec.app.formatter.ListScalesResponseFormatter
import coden.alec.app.fsm.*
import coden.alec.app.resources.MessageResource
import coden.console.view.ConsoleMessageDisplay
import coden.alec.core.*
import coden.alec.data.Scale
import coden.alec.interactors.definer.scale.BaseCreateScaleInteractor
import coden.alec.interactors.definer.scale.BaseListScalesInteractor
import coden.alec.main.bot.AlecBotProperties
import coden.alec.main.table.HelpTable
import coden.alec.main.table.ScaleTable
import coden.fsm.FSM
import coden.fsm.LoggingCommandExecutor
import gateway.memory.ScaleInMemoryGateway
import org.junit.jupiter.api.Test

class MainTest {
    @Test
    fun launch() {
        val messages = MessageResource()
        messages.errorMessage = "error"
        messages.startMessage = "start"
        messages.listScalesMessage = "list scales"
        messages.listScalesEmptyMessage = "empty"
        val botProperties = AlecBotProperties()
        botProperties.token = "5402767430:AAEXVe8s8IAow9z9Ip69NCU9JnUaEHIgcrw"

        val scalesGateway = ScaleInMemoryGateway()
        val useCaseFactory = object : UseCaseFactory {
            override fun listScales(): coden.alec.core.ListScalesInteractor {
                return BaseListScalesInteractor(scalesGateway)
            }

            override fun createScale(): coden.alec.core.CreateScaleInteractor {
                return BaseCreateScaleInteractor(scalesGateway)
            }

            override fun deleteScale(): DeleteScaleInteractor {
                TODO("Not yet implemented")
            }

            override fun purgeScale(): PurgeScaleInteractor {
                TODO("Not yet implemented")
            }

            override fun updateScale(): UpdateScaleInteractor {
                TODO("Not yet implemented")
            }
        }
        val consoleView = ConsoleMessageDisplay()

//        val view = telegramView
        val view = consoleView


        val scaleActuator = BaseScaleActuator(useCaseFactory, view, messages, object: ListScalesResponseFormatter {
            override fun format(response: List<Scale>): String {
                return response.toString()
            }

        })
        val helpActuator = BaseHelpActuator(useCaseFactory, view, messages)

        val stateExecutor = LoggingCommandExecutor(FSM(Start, HelpTable(helpActuator) + ScaleTable(scaleActuator)))


        stateExecutor.submit(HelpCommand)
        stateExecutor.submit(HelpCommand)
        stateExecutor.submit(HelpCommand)
        stateExecutor.submit(ListScalesCommand)
        stateExecutor.submit(ListScalesCommand)
        stateExecutor.submit(ListScalesCommand)
        stateExecutor.submit(HelpCommand)
        stateExecutor.submit(CreateScaleCommand("something"))
        stateExecutor.submit(CreateScaleCommand("hello\nname\n1-interesting"))
        stateExecutor.submit(ListScalesCommand)

        stateExecutor.submit(CreateScaleCommandNoArgs)
        stateExecutor.submit(TextCommand( " asd asd"))
        stateExecutor.submit(TextCommand( "scale"))
        stateExecutor.submit(TextCommand( " asd asd"))
        stateExecutor.submit(TextCommand( "unit"))
        stateExecutor.submit(TextCommand( "hello"))
        stateExecutor.submit(TextCommand( "1-interesting\n2-somewhatinteresting"))

        stateExecutor.submit(ListScalesCommand)
    }
}
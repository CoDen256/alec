package coden.alec.main

import coden.alec.app.actuators.BaseHelpActuator
import coden.alec.app.actuators.BaseScaleActuator
import coden.alec.app.fsm.*
import coden.alec.app.resources.MessageResource
import coden.console.view.ConsoleMessageDisplay
import coden.alec.core.*
import coden.alec.interactors.definer.scale.CreateScaleInteractor
import coden.alec.interactors.definer.scale.ListScalesInteractor
import coden.alec.main.config.AlecBotProperties
import coden.alec.main.config.table.HelpTable
import coden.alec.main.config.table.ScaleTable
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
        val consoleView = ConsoleMessageDisplay()

//        val view = telegramView
        val view = consoleView


        val scaleActuator = BaseScaleActuator(useCaseFactory, view, messages)
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
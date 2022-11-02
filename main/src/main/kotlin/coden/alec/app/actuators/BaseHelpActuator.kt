package coden.alec.app.actuators

import coden.alec.app.resources.MessageResource
import coden.alec.core.UseCaseFactory
import coden.display.displays.MessageDisplay
import coden.fsm.Command

class BaseHelpActuator(
    private val useCaseFactory: UseCaseFactory,
    private val view: MessageDisplay,
    private val messages: MessageResource,
) : HelpActuator {
    override fun displayHelp(command: Command) {
//        view.displayMenu(messages.startMessage)
    }
}
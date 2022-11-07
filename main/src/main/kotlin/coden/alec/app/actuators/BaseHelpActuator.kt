package coden.alec.app.actuators

import coden.alec.app.resources.MessageResource
import coden.display.displays.MessageDisplay

class BaseHelpActuator(
    private val view: MessageDisplay,
    private val messages: MessageResource,
) : HelpActuator {
    override fun displayHelp() {
        view.displayMessage(messages.startMessage)
    }
}
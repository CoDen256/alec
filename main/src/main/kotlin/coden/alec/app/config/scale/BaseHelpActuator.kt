package coden.alec.app.config.scale

import coden.alec.app.actuators.HelpActuator
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
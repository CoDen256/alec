package coden.alec.app.actuators

import coden.alec.app.config.help.BaseHelpActuator
import coden.alec.app.resources.MessageResource
import coden.display.displays.MessageDisplay
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.times
import org.mockito.kotlin.verify

@ExtendWith(MockitoExtension::class)
class BaseHelpActuatorTest{
    private val resource=  MessageResource().apply {
        start = "startMessage"
    }

    @Mock
    lateinit var display: MessageDisplay

    @Test
    fun execute(){

        BaseHelpActuator(
            display,
            resource,
        ).displayHelp()

        verify(display, times(1)).displayMessage(resource.start)
    }
}
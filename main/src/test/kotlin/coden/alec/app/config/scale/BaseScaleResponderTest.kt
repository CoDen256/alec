package coden.alec.app.config.scale

import coden.alec.app.actuators.scale.InvalidScaleFormatException
import coden.alec.app.actuators.scale.ScaleFormatter
import coden.alec.app.resources.MessageResource
import coden.alec.main.resources.Messages
import coden.display.displays.MessageDisplay
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mock
import org.mockito.kotlin.whenever

internal class BaseScaleResponderTest {
    
    @Mock
    lateinit var messageDisplay: MessageDisplay
    
    @Mock
    lateinit var formatter: ScaleFormatter
    
    lateinit var responder: BaseScaleResponder
    
    private val messages = MessageResource().apply { 
        
    }
    
    @BeforeEach
    fun setup(){
        responder = BaseScaleResponder(messageDisplay, messages, formatter)
    }

    @Test
    fun respondInvalidScaleFormat() {
//        whenever()
        
        responder.respondInvalidScaleFormat(InvalidScaleFormatException(""))
    }

    @Test
    fun respondInvalidScalePropertyFormat() {
    }

    @Test
    fun respondInternalError() {
    }

    @Test
    fun respondListScales() {
    }

    @Test
    fun respondCreateScale() {
    }

    @Test
    fun respondPromptScaleName() {
    }

    @Test
    fun respondPromptScaleUnit() {
    }

    @Test
    fun respondPromptScaleDivisions() {
    }
}
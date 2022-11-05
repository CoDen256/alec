package coden.alec.app.actuators

import coden.alec.app.display.ScaleParser
import coden.alec.app.display.ScaleResponder
import coden.alec.app.fsm.CreateScaleCommand
import coden.alec.app.fsm.ListScalesCommand
import coden.alec.core.*
import coden.alec.data.Scale
import coden.alec.data.ScaleDivision
import coden.alec.interactors.definer.scale.CreateScaleRequest
import coden.alec.interactors.definer.scale.CreateScaleResponse
import coden.alec.interactors.definer.scale.ListScalesRequest
import coden.alec.interactors.definer.scale.ListScalesResponse
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExtendWith(MockitoExtension::class)
class BaseScaleActuatorTest {

    @Mock
    lateinit var useCaseFactory: ScaleUseCaseFactory

    @Mock
    lateinit var responder: ScaleResponder

    @Mock
    lateinit var parser: ScaleParser

    @Mock
    lateinit var listScalesInteractor: ListScalesInteractor

    @Mock
    lateinit var createScaleInteractor: CreateScaleInteractor

    private lateinit var actuator: ScaleActuator

    @BeforeEach
    fun setup() {
        actuator = BaseScaleActuator(
            useCaseFactory, responder, parser
        )
    }


    @Test
    fun getAndDisplayScales() {
        val response = ListScalesResponse(
            Result.success(listOf(Scale("scale-0", "scale", "unit", false, listOf(ScaleDivision(1, "one"))))
            )
        )
        whenever(useCaseFactory.listScales()).thenReturn(listScalesInteractor)
        whenever(listScalesInteractor.execute(any())).thenReturn(response)

        actuator.getAndDisplayScales(ListScalesCommand)

        verify(listScalesInteractor).execute(any<ListScalesRequest>())
        verify(responder).respondListScales(response)
    }


    @Test
    fun createAndDisplayScale() {
        val response = CreateScaleResponse(Result.success("scale-1"))
        val request = CreateScaleRequest(
            name = "name",
            unit = "unit",
            divisions = mapOf(
                1L to "div1",
                2L to "div2",
                3L to "div3"
            )
        )
        whenever(useCaseFactory.createScale()).thenReturn(createScaleInteractor)
        whenever(parser.isValidCreateScaleRequest("hustensaft")).thenReturn(true)
        whenever(parser.parseCreateScaleRequest("hustensaft")).thenReturn(request)
        whenever(createScaleInteractor.execute(any())).thenReturn(response)

        actuator.createAndDisplayScale(CreateScaleCommand("hustensaft"))

        verify(parser).isValidCreateScaleRequest("hustensaft")
        verify(parser).parseCreateScaleRequest("hustensaft")
        verify(createScaleInteractor).execute(request)
        verify(responder).respondCreateScale(response)
    }

    @Test
    fun createScaleInvalidFormat() {

    }


}
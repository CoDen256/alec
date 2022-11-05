package coden.alec.app.actuators

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
    lateinit var listScalesInteractor: ListScalesInteractor

    @Mock
    lateinit var createScaleInteractor: CreateScaleInteractor

    lateinit var actuator: ScaleActuator

    @BeforeEach
    fun setup() {
        actuator = BaseScaleActuator(
            useCaseFactory, responder
        )
    }


    @Test
    fun getAndDisplayScalesSuccess() {

        val listScalesResponse = generateResponse()
        whenever(useCaseFactory.listScales()).thenReturn(listScalesInteractor)
        whenever(listScalesInteractor.execute(any())).thenReturn(listScalesResponse)


        actuator.getAndDisplayScales(ListScalesCommand)

        verify(listScalesInteractor, times(1)).execute(any<ListScalesRequest>())
    }

    @Test
    fun getAndDisplayScalesEmpty() {

        val listScalesResponse = ListScalesResponse(Result.success(emptyList()))
        whenever(useCaseFactory.listScales()).thenReturn(listScalesInteractor)
        whenever(listScalesInteractor.execute(any())).thenReturn(listScalesResponse)


        actuator.getAndDisplayScales(ListScalesCommand)

        verify(listScalesInteractor, times(1)).execute(any<ListScalesRequest>())
    }

    @Test
    fun getAndDisplayScalesFailure() {

        val listScalesResponse = ListScalesResponse(Result.failure(IllegalStateException("e")))
        whenever(useCaseFactory.listScales()).thenReturn(listScalesInteractor)
        whenever(listScalesInteractor.execute(any())).thenReturn(listScalesResponse)


        actuator.getAndDisplayScales(ListScalesCommand)

        verify(listScalesInteractor, times(1)).execute(any<ListScalesRequest>())
    }


    @Test
    fun createScale() {
        val response = CreateScaleResponse(Result.success("scale-1"))
        whenever(useCaseFactory.createScale()).thenReturn(createScaleInteractor)
        whenever(createScaleInteractor.execute(any())).thenReturn(response)

        actuator.createAndDisplayScale(CreateScaleCommand("name\nunit\n1-div1\n2-div2\n3-div3"))

        verify(createScaleInteractor, times(1)).execute(
            CreateScaleRequest(
                name = "name",
                unit = "unit",
                divisions = mapOf(
                    1L to "div1",
                    2L to "div2",
                    3L to "div3"
                )
            )
        )
    }

    @Test
    fun createScaleInvalidFormat() {

        assertThrows<InvalidScaleFormatException> {
            actuator.createAndDisplayScale(CreateScaleCommand(""))
        }

        assertThrows<InvalidScaleFormatException> {
            actuator.createAndDisplayScale(CreateScaleCommand("name"))
        }
    }


    private fun generateResponse() = ListScalesResponse(
        Result.success(
            listOf(
                Scale(
                    "scale-0", "scale", "unit", false, listOf(
                        ScaleDivision(1, "one")
                    )
                )
            )
        )
    )


}
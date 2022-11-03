package coden.alec.app.actuators

import coden.alec.app.formatter.ScaleFormatter
import coden.alec.app.fsm.CreateScaleCommand
import coden.alec.app.fsm.ListScalesCommand
import coden.alec.app.resources.MessageResource
import coden.alec.core.*
import coden.alec.data.Scale
import coden.alec.data.ScaleDivision
import coden.alec.interactors.definer.scale.CreateScaleRequest
import coden.alec.interactors.definer.scale.CreateScaleResponse
import coden.alec.interactors.definer.scale.ListScalesRequest
import coden.alec.interactors.definer.scale.ListScalesResponse
import coden.display.displays.MessageDisplay
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.times
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever

@ExtendWith(MockitoExtension::class)
class BaseScaleActuatorTest {

    val resource=  MessageResource().apply {
        listScalesMessage = "listScalesMessage"
        listScalesEmptyMessage = "listScalesEmptyMessage"
        errorMessage = "errorMessage"
        createdScaleMessage = "createdScaleMessage"
    }

    @Mock
    lateinit var useCaseFactory: UseCaseFactory

    @Mock
    lateinit var display: MessageDisplay

    @Mock
    lateinit var formatter: ScaleFormatter

    @Mock
    lateinit var listScalesInteractor: ListScalesInteractor

    @Mock
    lateinit var createScaleInteractor: CreateScaleInteractor


    @Test
    fun getAndDisplayScalesSuccess() {

        val listScalesResponse = generateResponse()
        whenever(useCaseFactory.listScales()).thenReturn(listScalesInteractor)
        whenever(listScalesInteractor.execute(any())).thenReturn(listScalesResponse)
        whenever(formatter.format(any<List<Scale>>())).thenReturn("")

        val scaleActuator = BaseScaleActuator(
            useCaseFactory,
            display,
            resource,
            formatter
        )

        scaleActuator.getAndDisplayScales(ListScalesCommand)

        verify(listScalesInteractor, times(1)).execute(any<ListScalesRequest>())
        verify(display, times(1)).displayMessage(resource.listScalesMessage)

    }

    @Test
    fun getAndDisplayScalesEmpty() {

        val listScalesResponse = ListScalesResponse(Result.success(emptyList()))
        whenever(useCaseFactory.listScales()).thenReturn(listScalesInteractor)
        whenever(listScalesInteractor.execute(any())).thenReturn(listScalesResponse)

        val scaleActuator = BaseScaleActuator(
            useCaseFactory,
            display,
            resource,
            formatter
        )

        scaleActuator.getAndDisplayScales(ListScalesCommand)

        verify(listScalesInteractor, times(1)).execute(any<ListScalesRequest>())
        verify(display, times(1)).displayMessage(resource.listScalesEmptyMessage)

    }

    @Test
    fun getAndDisplayScalesFailure() {

        val listScalesResponse = ListScalesResponse(Result.failure(IllegalStateException("e")))
        whenever(useCaseFactory.listScales()).thenReturn(listScalesInteractor)
        whenever(listScalesInteractor.execute(any())).thenReturn(listScalesResponse)

        val scaleActuator = BaseScaleActuator(
            useCaseFactory,
            display,
            resource,
            formatter
        )

        scaleActuator.getAndDisplayScales(ListScalesCommand)

        verify(listScalesInteractor, times(1)).execute(any<ListScalesRequest>())
        verify(display, times(1)).displayError(resource.errorMessage +" e")
    }


    @Test
    fun createScale() {
        val response = CreateScaleResponse(Result.success("scale-1"))
        whenever(useCaseFactory.createScale()).thenReturn(createScaleInteractor)
        whenever(createScaleInteractor.execute(any())).thenReturn(response)
        whenever(formatter.formatId(any())).thenReturn("")
        val scaleActuator = BaseScaleActuator(
            useCaseFactory,
            display,
            resource,
            formatter
        )

        scaleActuator.createAndDisplayScale(CreateScaleCommand("name\nunit\n1-div1\n2-div2\n3-div3"))

        verify(createScaleInteractor, times(1)).execute(CreateScaleRequest(
            name = "name",
            unit = "unit",
            divisions = mapOf(
                1L to "div1",
                2L to "div2",
                3L to "div3"
            )
        ))
        verify(display, times(1)).displayMessage(resource.createdScaleMessage)
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
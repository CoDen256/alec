package coden.alec.app.actuators

import coden.alec.app.display.ScaleParser
import coden.alec.app.display.ScaleResponder
import coden.alec.app.fsm.CreateScaleCommand
import coden.alec.app.fsm.ListScalesCommand
import coden.alec.app.fsm.TextCommand
import coden.alec.core.*
import coden.alec.data.Scale
import coden.alec.data.ScaleDivision
import coden.alec.interactors.definer.scale.CreateScaleRequest
import coden.alec.interactors.definer.scale.CreateScaleResponse
import coden.alec.interactors.definer.scale.ListScalesRequest
import coden.alec.interactors.definer.scale.ListScalesResponse
import coden.fsm.NoArgException
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.*

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
        whenever(parser.isValidCreateScaleRequest("hustensaft")).thenReturn(false)
        whenever(parser.isValidCreateScaleRequest("")).thenReturn(false)

        assertThrows<InvalidScaleFormatException> {
            actuator.createAndDisplayScale(CreateScaleCommand("hustensaft"))
        }

        assertThrows<InvalidScaleFormatException> {
            actuator.createAndDisplayScale(CreateScaleCommand(""))
        }

        assertThrows<InvalidScaleFormatException> {
            actuator.createAndDisplayScale(TextCommand(""))
        }

        assertThrows<NoArgException> {
            actuator.createAndDisplayScale(ListScalesCommand)
        }

        verify(parser).isValidCreateScaleRequest("hustensaft")
        verify(parser, times(2)).isValidCreateScaleRequest("")
        verifyNoMoreInteractions(parser)
    }

    @Test
    fun isValidScale() {
        whenever(parser.isValidCreateScaleRequest("hustensaft")).thenReturn(false)
        whenever(parser.isValidCreateScaleRequest("h")).thenReturn(true)

        assertFalse(actuator.isValidScale(CreateScaleCommand("hustensaft")))
        assertFalse(actuator.isValidScale(TextCommand("hustensaft")))
        assertTrue(actuator.isValidScale(CreateScaleCommand("h")))
        assertTrue(actuator.isValidScale(TextCommand("h")))

        assertThrows<NoArgException> {
            actuator.isValidScale(ListScalesCommand)
        }
    }

    @Test
    fun isValidName() {
        whenever(parser.isValidScaleName("hustensaft")).thenReturn(false)
        whenever(parser.isValidScaleName("h")).thenReturn(true)

        assertFalse(actuator.isValidScaleName(CreateScaleCommand("hustensaft")))
        assertFalse(actuator.isValidScaleName(TextCommand("hustensaft")))
        assertTrue(actuator.isValidScaleName(CreateScaleCommand("h")))
        assertTrue(actuator.isValidScaleName(TextCommand("h")))

        assertThrows<NoArgException> {
            actuator.isValidScaleName(ListScalesCommand)
        }
    }

    @Test
    fun isValidUnit() {
        whenever(parser.isValidScaleUnit("hustensaft")).thenReturn(false)
        whenever(parser.isValidScaleUnit("h")).thenReturn(true)

        assertFalse(actuator.isValidScaleUnit(CreateScaleCommand("hustensaft")))
        assertFalse(actuator.isValidScaleUnit(TextCommand("hustensaft")))
        assertTrue(actuator.isValidScaleUnit(CreateScaleCommand("h")))
        assertTrue(actuator.isValidScaleUnit(TextCommand("h")))

        assertThrows<NoArgException> {
            actuator.isValidScaleUnit(ListScalesCommand)
        }
    }


    @Test
    fun isValidDivisions() {
        whenever(parser.isValidDivisions("hustensaft")).thenReturn(false)
        whenever(parser.isValidDivisions("h")).thenReturn(true)

        assertFalse(actuator.isValidScaleDivisions(CreateScaleCommand("hustensaft")))
        assertFalse(actuator.isValidScaleDivisions(TextCommand("hustensaft")))
        assertTrue(actuator.isValidScaleDivisions(CreateScaleCommand("h")))
        assertTrue(actuator.isValidScaleDivisions(TextCommand("h")))

        assertThrows<NoArgException> {
            actuator.isValidScaleDivisions(ListScalesCommand)
        }
    }



    @Test
    fun handleNameUnitDivisions() {
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
        whenever(createScaleInteractor.execute(request)).thenReturn(response)
        whenever(parser.parseCreateScaleRequest("name","unit", "div")).thenReturn(request)

        whenever(parser.isValidScaleName("name")).thenReturn(true)
        whenever(parser.isValidScaleUnit("unit")).thenReturn(true)
        whenever(parser.isValidDivisions("div")).thenReturn(true)

        actuator.isValidScaleName(TextCommand("name"))
        actuator.handleScaleName(TextCommand("name"))

        actuator.isValidScaleUnit(TextCommand("unit"))
        actuator.handleScaleUnit(CreateScaleCommand("unit")) // just to test that the command doesn't matter

        actuator.isValidScaleDivisions(TextCommand("div"))
        actuator.handleScaleDivisions(TextCommand("div"))

        actuator.createAndDisplayScale(TextCommand("div"))

        verify(parser, never()).isValidCreateScaleRequest(any())
        verify(parser).parseCreateScaleRequest("name", "unit", "div")
        verify(createScaleInteractor).execute(request)
        verify(responder).respondCreateScale(response)
    }
}
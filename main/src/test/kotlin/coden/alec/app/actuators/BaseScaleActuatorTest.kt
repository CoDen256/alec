package coden.alec.app.actuators

import coden.alec.app.display.ScaleParser
import coden.alec.app.display.ScaleResponder
import coden.alec.core.*
import coden.alec.interactors.definer.scale.CreateScaleRequest
import coden.alec.interactors.definer.scale.CreateScaleResponse
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
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


//    @Test
//    fun getAndDisplayScales() {
//        val response = ListScalesResponse(
//            Result.success(listOf(Scale("scale-0", "scale", "unit", false, listOf(ScaleDivision(1, "one"))))
//            )
//        )
//        whenever(useCaseFactory.listScales()).thenReturn(listScalesInteractor)
//        whenever(listScalesInteractor.execute(any())).thenReturn(response)
//
//        actuator.getAndDisplayScales()
//
//        verify(listScalesInteractor).execute(any<ListScalesRequest>())
//        verify(responder).respondListScales(response)
//    }



    @Test
    fun createScale() {
        val response = Result.success(CreateScaleResponse("scale-1"))
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
        whenever(parser.parseCreateScaleRequest("hustensaft")).thenReturn(request)
        whenever(createScaleInteractor.execute(any())).thenReturn(response)

        val result = actuator.createScale("hustensaft")

        verify(parser).parseCreateScaleRequest("hustensaft")
        verify(createScaleInteractor).execute(request)
        assertTrue(result.isSuccess)
        assertEquals(response.getOrThrow(), result.getOrThrow())
    }

    @Test
    fun rejectScale() {
        actuator.rejectScale()
        actuator.rejectScaleName()
        actuator.rejectScaleDivisions()
        actuator.rejectScaleUnit()
        verify(responder).respondRejectScale()
    }

    @Test
     fun handleScaleName() {
        whenever(parser.parseScaleName(" name" )).thenReturn("name")

        val result = actuator.parseScaleName("name")
        assertEquals("name", result.getOrThrow())
    }

//    @Test
//    fun createScaleInvalidFormat() {
//        whenever(parser.parseCreateScaleRequest(any())).thenThrow(InvalidScaleFormatException::class.java)
//
//        assertThrows<InvalidScaleFormatException> {
//            actuator.createAndDisplayScale("hustensaft")
//        }
//
//        assertThrows<InvalidScaleFormatException> {
//            actuator.createAndDisplayScale("")
//        }
//
//        verify(parser, times(2)).parseCreateScaleRequest(any())
//        verifyNoMoreInteractions(parser)
//    }



    @Test
    fun isValidName() {
        whenever(parser.isValidScaleName("hustensaft")).thenReturn(false)
        whenever(parser.isValidScaleName("h")).thenReturn(true)

        assertFalse(actuator.isValidScaleName("hustensaft"))
        assertFalse(actuator.isValidScaleName("hustensaft"))
        assertTrue(actuator.isValidScaleName("h"))
        assertTrue(actuator.isValidScaleName("h"))

    }

    @Test
    fun isValidUnit() {
        whenever(parser.isValidScaleUnit("hustensaft")).thenReturn(false)
        whenever(parser.isValidScaleUnit("h")).thenReturn(true)

        assertFalse(actuator.isValidScaleUnit("hustensaft"))
        assertFalse(actuator.isValidScaleUnit("hustensaft"))
        assertTrue(actuator.isValidScaleUnit("h"))
        assertTrue(actuator.isValidScaleUnit("h"))
    }


    @Test
    fun isValidDivisions() {
        whenever(parser.isValidDivisions("hustensaft")).thenReturn(false)
        whenever(parser.isValidDivisions("h")).thenReturn(true)

        assertFalse(actuator.isValidScaleDivisions("hustensaft"))
        assertFalse(actuator.isValidScaleDivisions("hustensaft"))
        assertTrue(actuator.isValidScaleDivisions("h"))
        assertTrue(actuator.isValidScaleDivisions("h"))
    }



//    @Test
//    fun handleNameUnitDivisions() {
//        val response = Result.success(CreateScaleResponse("scale-1"))
//        val request = CreateScaleRequest(
//            name = "name",
//            unit = "unit",
//            divisions = mapOf(
//                1L to "div1",
//                2L to "div2",
//                3L to "div3"
//            )
//        )
//        whenever(useCaseFactory.createScale()).thenReturn(createScaleInteractor)
//        whenever(createScaleInteractor.execute(request)).thenReturn(response)
//        whenever(parser.parseCreateScaleRequest("name","unit", "div")).thenReturn(request)
//
//        whenever(parser.isValidScaleName("name")).thenReturn(true)
//        whenever(parser.isValidScaleUnit("unit")).thenReturn(true)
//        whenever(parser.isValidDivisions("div")).thenReturn(true)
//
//        actuator.isValidScaleName("name")
//        actuator.handleScaleName("name")
//
//        actuator.isValidScaleUnit("unit")
//        actuator.handleScaleUnit("unit")
//
//        actuator.isValidScaleDivisions("div")
//        actuator.handleScaleDivisions("div")
//
//        actuator.createAndDisplayScale("div")
//
//        verify(parser, never()).isValidCreateScaleRequest(any())
//        verify(parser).parseCreateScaleRequest("name", "unit", "div")
//        verify(createScaleInteractor).execute(request)
//        verify(responder).respondCreateScale(response)
//    }
}
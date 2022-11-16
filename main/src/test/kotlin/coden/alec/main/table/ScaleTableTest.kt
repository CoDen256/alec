package coden.alec.main.table

import coden.alec.app.actuators.ScaleActuator
import coden.alec.app.actuators.scale.InvalidScaleFormatException
import coden.alec.app.actuators.scale.InvalidScalePropertyFormatException
import coden.alec.app.fsm.*
import coden.alec.interactors.definer.scale.CreateScaleRequest
import coden.alec.interactors.definer.scale.CreateScaleResponse
import coden.alec.interactors.definer.scale.ListScalesResponse
import coden.fsm.FSMVerifier
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.whenever
import java.lang.IllegalStateException

@ExtendWith(MockitoExtension::class)
class ScaleTableTest{
    @Mock
    lateinit var actuator: ScaleActuator

    private lateinit var verifier: FSMVerifier

    @BeforeEach
    fun setup(){
        verifier = FSMVerifier(ScaleTable(actuator), Start)
    }

    @Test
    fun listScales() {
        val response = ListScalesResponse(emptyList())
        val error = IllegalStateException()

        whenever(actuator.listScales())
            .thenReturn(Result.success(response))
            .thenReturn(Result.failure(error))

        verifier
            .submit(ListScalesCommand)
            .verify(actuator) {listScales()}
            .verify(actuator) {respondListScales(response)}
            .verifyState(Start)

            .submit(ListScalesCommand)
            .verify(actuator) {listScales()}
            .verify(actuator) {respondInternalError(error)}
            .verifyState(Start)

    }
    @Test
     fun createScalesWithArgs() {
        val request = genRequest()
        val response = genResponse()
        val error = IllegalStateException()
        val userError = InvalidScaleFormatException("")
        whenever(actuator.parseCreateScaleRequest("scale"))
            .thenReturn(Result.success(request)) // 0
            .thenReturn(Result.failure(userError)) // 1
            .thenReturn(Result.failure(error))  // 2
            .thenReturn(Result.success(request)) // 3

        whenever(actuator.createScale(request))
            .thenReturn(Result.success(response)) // 0
            .thenReturn(Result.failure(error))   // 3

        verifier
            .submit(CreateScaleCommand("scale"))
            .verify(actuator) {parseCreateScaleRequest("scale")}
            .verify(actuator) {createScale(request) }
            .verify(actuator) {respondCreateScale(response)}
            .verifyState(Start)

            .submit(CreateScaleCommand("scale"))
            .verify(actuator) {parseCreateScaleRequest("scale")}
            .verify(actuator) {respondInvalidScaleFormat(userError)}
            .verifyState(Start)

            .submit(CreateScaleCommand("scale"))
            .verify(actuator) {parseCreateScaleRequest("scale")}
            .verify(actuator) {respondInternalError(error)}
            .verifyState(Start)

            .submit(CreateScaleCommand("scale"))
            .verify(actuator) {parseCreateScaleRequest("scale")}
            .verify(actuator) {createScale(request)}
            .verify(actuator) {respondInternalError(error)}
            .verifyState(Start)

    }

    @Test
    fun createScaleWithoutArgs() {
        val request = genRequest()
        val response = genResponse()
        val error = IllegalStateException()
        val userError = InvalidScalePropertyFormatException("", "")

        whenever(actuator.parseScaleName("name"))
            .thenReturn(Result.failure(userError)) // 0
            .thenReturn(Result.success("name")) // 1

        whenever(actuator.parseScaleUnit("unit"))
            .thenReturn(Result.failure(userError)) // 2
            .thenReturn(Result.success("unit")) // 3

        whenever(actuator.parseScaleDivisions("1-div"))
            .thenReturn(Result.failure(userError)) // 4
            .thenReturn(Result.success(mapOf(1L to "div"))) // 5


        whenever(actuator.build()).thenReturn(request) // 5
        whenever(actuator.createScale(request)).thenReturn(Result.success(response)) // 5

        verifier
            .submit(CreateScaleCommandNoArgs)
            .verify(actuator) {respondPromptScaleName()}
            .verifyState(WaitScaleName)

                // invalid name
            .submit(TextCommand("name"))
            .verify(actuator) {parseScaleName("name")}
            .verify(actuator) {respondInvalidScalePropertyFormat(userError)}
            .verify(actuator) {respondPromptScaleName()}
            .verifyState(WaitScaleName)

                // valid name
            .submit(TextCommand("name"))
            .verify(actuator) {parseScaleName("name")}
            .verify(actuator) {setName("name")}
            .verify(actuator) {respondPromptScaleUnit()}
            .verifyState(WaitScaleUnit)

                // invalid unit
            .submit(TextCommand("unit"))
            .verify(actuator) {parseScaleUnit("unit")}
            .verify(actuator) {respondInvalidScalePropertyFormat(userError)}
            .verify(actuator) {respondPromptScaleUnit()}
            .verifyState(WaitScaleUnit)

                // valid unit
            .submit(TextCommand("unit"))
            .verify(actuator) {parseScaleUnit("unit")}
            .verify(actuator) {setUnit("unit")}
            .verify(actuator) {respondPromptScaleDivisions()}
            .verifyState(WaitScaleDivision)

            // invalid divisions
            .submit(TextCommand("1-div"))
            .verify(actuator) {parseScaleDivisions("1-div")}
            .verify(actuator) {respondInvalidScalePropertyFormat(userError)}
            .verify(actuator) {respondPromptScaleDivisions()}
            .verifyState(WaitScaleDivision)

            // valid divisions
            .submit(TextCommand("1-div"))
            .verify(actuator) {parseScaleDivisions("1-div")}
            .verify(actuator) {setDivisions(mapOf(1L to "div"))}

            .verify(actuator) {build()}
            .verify(actuator) {createScale(request)}
            .verify(actuator) {respondCreateScale(response)}
            .verifyState(Start)

    }

    private fun genResponse() = CreateScaleResponse("scale-0")

    private fun genRequest() = CreateScaleRequest(
        "scale", "unit", mapOf(1L to "some")
    )
}
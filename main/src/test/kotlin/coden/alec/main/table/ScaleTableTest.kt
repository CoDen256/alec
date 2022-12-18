package coden.alec.main.table

import coden.alec.app.actuators.ScaleActuator
import coden.alec.app.actuators.scale.InvalidScaleFormatException
import coden.alec.app.actuators.scale.InvalidScalePropertyFormatException
import coden.alec.app.fsm.*
import coden.alec.core.ScaleIsNotDeletedException
import coden.alec.data.ScaleAlreadyExistsException
import coden.alec.data.ScaleDoesNotExistException
import coden.alec.interactors.definer.scale.*
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
        verifier = FSMVerifier(ScaleTableBuilder().buildTableFrom(actuator), Start)
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
        val request = genCreateRequest()
        val response = genCreateResponse()
        val error = IllegalStateException()
        val userError = InvalidScaleFormatException("")
        val alreadyExistsError = ScaleAlreadyExistsException("")
        whenever(actuator.parseCreateScaleRequest("scale"))
            .thenReturn(Result.success(request)) // 0
            .thenReturn(Result.failure(userError)) // 1
            .thenReturn(Result.failure(error))  // 2
            .thenReturn(Result.success(request)) // 3
            .thenReturn(Result.success(request)) // 4

        whenever(actuator.createScale(request))
            .thenReturn(Result.success(response)) // 0
            .thenReturn(Result.failure(error))   // 3
            .thenReturn(Result.failure(alreadyExistsError))   // 4

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

            .submit(CreateScaleCommand("scale"))
            .verify(actuator) {parseCreateScaleRequest("scale")}
            .verify(actuator) {createScale(request)}
            .verify(actuator) {respondScaleAlreadyExists(alreadyExistsError)}
            .verifyState(Start)

    }

    @Test
    fun createScaleWithoutArgs() {
        val request = genCreateRequest()
        val response = genCreateResponse()
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


        whenever(actuator.buildCreateRequest())
            .thenReturn(request) // 5
        whenever(actuator.createScale(request))
            .thenReturn(Result.success(response)) // 5

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

            .verify(actuator) {buildCreateRequest()}
            .verify(actuator) {createScale(request)}
            .verify(actuator) {respondCreateScale(response)}
            .verify(actuator) {reset()}
            .verifyState(Start)
    }

    @Test
    fun createScaleWithoutArgsWithErrors(){
        val request = genCreateRequest()
        val error = IllegalStateException()
        val alreadyExists = ScaleAlreadyExistsException("scale")

        whenever(actuator.parseScaleName("name"))
            .thenReturn(Result.success("name")) // 0
            .thenReturn(Result.success("name")) // 1
            .thenReturn(Result.success("name")) // 2
        whenever(actuator.parseScaleUnit("unit"))
            .thenReturn(Result.failure(error)) // 0
            .thenReturn(Result.success("unit")) // 1
            .thenReturn(Result.success("unit")) // 2
        whenever(actuator.parseScaleDivisions("1-div"))
            .thenReturn(Result.success(mapOf(1L to "div"))) // 1
            .thenReturn(Result.success(mapOf(1L to "div"))) // 2

        whenever(actuator.buildCreateRequest())
            .thenReturn(request) // 1
            .thenReturn(request) // 2
        whenever(actuator.createScale(request))
            .thenReturn(Result.failure(error)) // 1
            .thenReturn(Result.failure(alreadyExists)) // 2

        verifier
                // 0
            .submit(CreateScaleCommandNoArgs)
            .verifyState(WaitScaleName)

            .submit(TextCommand("name"))
            .verifyState(WaitScaleUnit)

            .submit(TextCommand("unit"))
            .verify(actuator) {respondInternalError(error)}
            .verify(actuator) {actuator.reset()}
            .verifyState(Start)
                // 1
            .submit(CreateScaleCommandNoArgs)
            .verifyState(WaitScaleName)

            .submit(TextCommand("name"))
            .verifyState(WaitScaleUnit)

            .submit(TextCommand("unit"))
            .verifyState(WaitScaleDivision)

            .submit(TextCommand("1-div"))
            .verify(actuator) {buildCreateRequest()}
            .verify(actuator) {createScale(request)}
            .verify(actuator) {respondInternalError(error)}
            .verify(actuator) {reset()}
            .verifyState(Start)

            // 2
            .submit(CreateScaleCommandNoArgs)
            .submit(TextCommand("name"))
            .submit(TextCommand("unit"))
            .verifyState(WaitScaleDivision)

            .submit(TextCommand("1-div"))
            .verify(actuator) {buildCreateRequest()}
            .verify(actuator) {createScale(request)}
            .verify(actuator) {respondScaleAlreadyExists(alreadyExists)}
            .verify(actuator) {reset()}
            .verifyState(Start)
    }

    @Test
    fun deleteScaleByIdWithArgs() {
        val request = genDeleteRequest()
        val response = genDeleteResponse()
        val userError = ScaleDoesNotExistException("scale-0")

        whenever(actuator.parseDeleteScaleRequest("scale-0"))
            .thenReturn(Result.success(request))
            .thenReturn(Result.success(request))

        whenever(actuator.deleteScale(request))
            .thenReturn(Result.success(response))
            .thenReturn(Result.failure(userError))


        verifier
            .submit(DeleteScaleCommand("scale-0"))
            .verify(actuator) {parseDeleteScaleRequest("scale-0")}
            .verify(actuator) {deleteScale(request)}
            .verify(actuator) {respondDeleteScale(response)}
            .verifyState(Start)


            .submit(DeleteScaleCommand("scale-0"))
            .verify(actuator) {parseDeleteScaleRequest("scale-0")}
            .verify(actuator) {deleteScale(request)}
            .verify(actuator) {respondScaleDoesNotExist(userError)}
            .verifyState(Start)
    }

    @Test
    fun deleteScaleByIdWithoutArgs() {
        val request = genDeleteRequest()
        val response = genDeleteResponse()
        val userError = ScaleDoesNotExistException("scale-0")

        whenever(actuator.parseDeleteScaleRequest("scale-0"))
            .thenReturn(Result.success(request))
            .thenReturn(Result.success(request))
            .thenReturn(Result.success(request))

        whenever(actuator.deleteScale(request))
            .thenReturn(Result.success(response))
            .thenReturn(Result.failure(userError))
            .thenReturn(Result.success(response))



        verifier
            //  scale id exists
            .submit(DeleteScaleCommandNoArgs)
            .verify(actuator) {respondPromptScaleId()}
            .verifyState(WaitScaleIdForDelete)

            .submit(TextCommand("scale-0"))
            .verify(actuator) {parseDeleteScaleRequest("scale-0")}
            .verify(actuator) {deleteScale(request)}
            .verify(actuator) {respondDeleteScale(response)}
            .verifyState(Start)

             // Second round
            .submit(DeleteScaleCommandNoArgs)
            .verify(actuator) {respondPromptScaleId()}
            .verifyState(WaitScaleIdForDelete)

                // scale id does not exist
            .submit(TextCommand("scale-0"))
            .verify(actuator) {parseDeleteScaleRequest("scale-0")}
            .verify(actuator) {deleteScale(request)}
            .verify(actuator) {respondScaleDoesNotExist(userError)}
            .verify(actuator) {respondPromptScaleId()}
            .verifyState(WaitScaleIdForDelete)

                // scale id exists trying again
            .submit(TextCommand("scale-0"))
            .verify(actuator) {parseDeleteScaleRequest("scale-0")}
            .verify(actuator) {deleteScale(request)}
            .verify(actuator) {respondDeleteScale(response)}
            .verifyState(Start)
    }

    @Test
    fun purgeScaleByIdWithArgs() {
        val request = genPurgeRequest()
        val response = genPurgeResponse()
        val userError = ScaleDoesNotExistException("scale-0")
        val userError2 = ScaleIsNotDeletedException("scale-0")

        whenever(actuator.parsePurgeScaleRequest("scale-0"))
            .thenReturn(Result.success(request))
            .thenReturn(Result.success(request))
            .thenReturn(Result.success(request))

        whenever(actuator.purgeScale(request))
            .thenReturn(Result.success(response))
            .thenReturn(Result.failure(userError))
            .thenReturn(Result.failure(userError2))


        verifier
            .submit(PurgeScaleCommand("scale-0"))
            .verify(actuator) {parsePurgeScaleRequest("scale-0")}
            .verify(actuator) {purgeScale(request)}
            .verify(actuator) {respondPurgeScale(response)}
            .verifyState(Start)


            .submit(PurgeScaleCommand("scale-0"))
            .verify(actuator) {parsePurgeScaleRequest("scale-0")}
            .verify(actuator) {purgeScale(request)}
            .verify(actuator) {respondScaleDoesNotExist(userError)}
            .verifyState(Start)

            .submit(PurgeScaleCommand("scale-0"))
            .verify(actuator) {parsePurgeScaleRequest("scale-0")}
            .verify(actuator) {purgeScale(request)}
            .verify(actuator) {respondScaleIsNotDeleted(userError2)}
            .verifyState(Start)
    }

    @Test
    fun purgeScaleByIdWithoutArgs() {
        val request = genPurgeRequest()
        val response = genPurgeResponse()
        val userError = ScaleDoesNotExistException("scale-0")
        val userError2 = ScaleIsNotDeletedException("scale-0")

        whenever(actuator.parsePurgeScaleRequest("scale-0"))
            .thenReturn(Result.success(request))
            .thenReturn(Result.success(request))
            .thenReturn(Result.success(request))
            .thenReturn(Result.success(request))

        whenever(actuator.purgeScale(request))
            .thenReturn(Result.success(response))
            .thenReturn(Result.failure(userError))
            .thenReturn(Result.success(response))
            .thenReturn(Result.failure(userError2))



        verifier
            //  scale id exists
            .submit(PurgeScaleCommandNoArgs)
            .verify(actuator) {respondWarnAboutPurging()}
            .verify(actuator) {respondPromptScaleId()}
            .verifyState(WaitScaleIdForPurge)

            .submit(TextCommand("scale-0"))
            .verify(actuator) {parsePurgeScaleRequest("scale-0")}
            .verify(actuator) {purgeScale(request)}
            .verify(actuator) {respondPurgeScale(response)}
            .verifyState(Start)

            // Second round
            .submit(PurgeScaleCommandNoArgs)
            .verify(actuator) {respondWarnAboutPurging()}
            .verify(actuator) {respondPromptScaleId()}
            .verifyState(WaitScaleIdForPurge)

            // scale id does not exist
            .submit(TextCommand("scale-0"))
            .verify(actuator) {parsePurgeScaleRequest("scale-0")}
            .verify(actuator) {purgeScale(request)}
            .verify(actuator) {respondScaleDoesNotExist(userError)}
            .verify(actuator) {respondPromptScaleId()}
            .verifyState(WaitScaleIdForPurge)

            // scale id exists trying again
            .submit(TextCommand("scale-0"))
            .verify(actuator) {parsePurgeScaleRequest("scale-0")}
            .verify(actuator) {purgeScale(request)}
            .verify(actuator) {respondPurgeScale(response)}
            .verifyState(Start)

            // Third round
            .submit(PurgeScaleCommandNoArgs)
            .verify(actuator) {respondWarnAboutPurging()}
            .verify(actuator) {respondPromptScaleId()}
            .verifyState(WaitScaleIdForPurge)

            // scale is not deleted
            .submit(TextCommand("scale-0"))
            .verify(actuator) {parsePurgeScaleRequest("scale-0")}
            .verify(actuator) {purgeScale(request)}
            .verify(actuator) {respondScaleIsNotDeleted(userError2)}
            .verifyState(Start)
    }

    private fun genCreateResponse() = CreateScaleResponse("scale-0")
    private fun genDeleteResponse() = DeleteScaleResponse()
    private fun genPurgeResponse() = PurgeScaleResponse()

    private fun genCreateRequest() = CreateScaleRequest(
        "scale", "unit", mapOf(1L to "some")
    )

    private fun genDeleteRequest() = DeleteScaleRequest("scale-0")
    private fun genPurgeRequest() = PurgeScaleRequest("scale-0")
}
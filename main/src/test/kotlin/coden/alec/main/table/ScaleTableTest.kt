package coden.alec.main.table

import coden.alec.app.actuators.ScaleActuator
import coden.alec.app.fsm.ListScalesCommand
import coden.alec.app.fsm.Start
import coden.alec.interactors.definer.scale.ListScalesResponse
import coden.fsm.FSMVerifier
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever

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
        whenever(actuator.listScales()).thenReturn(Result.success(response))

        verifier
            .submit(ListScalesCommand)
            .verify(actuator) {listScales()}
            .verify(actuator) {respondListScales(any())}
            .verifyState(Start)

    }
}
package coden.alec.main.table

import coden.alec.app.actuators.HelpActuator
import coden.alec.app.fsm.HelpCommand
import coden.alec.app.fsm.Start
import coden.alec.app.fsm.TextCommand
import coden.fsm.FSMVerifier
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.never

@ExtendWith(MockitoExtension::class)
class HelpTableTest{

    @Mock
    lateinit var actuator: HelpActuator

    private lateinit var verifier: FSMVerifier

    @BeforeEach
    fun setup(){
        verifier = FSMVerifier(HelpTable(actuator), Start)
    }

    @Test
    fun helpCommand() {
        verifier
            .verifyState(Start)

            .submit(HelpCommand)
            .verify(actuator) {displayHelp()}
            .verifyState(Start)

            .submit(TextCommand(""))
            .verify(actuator, never()) {displayHelp()}
            .verifyState(Start)
    }
}
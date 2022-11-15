package coden.fsm

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.reset
import org.mockito.kotlin.verify

@ExtendWith(MockitoExtension::class)
class FSMVerifierTest{

    @Mock
    lateinit var actuator: Actuator

    interface Actuator{
        fun act(arg: String)
    }

    object Start: State
    object A: State
    object B: State

    object C1: BaseCommand()
    object C2: BaseCommand()



    @Test
    internal fun verifyInStartState() {
        val verifier = FSMVerifier(FSMTable(
            Entry.entry(Start, C1, A) {actuator.act("1")},
            Entry.entry(A, C1, B) {actuator.act("2")},
            Entry.entry(B, C1, Start) {actuator.act("3")},
            Entry.entry(A, C2, Start) {actuator.act("4")},
            Entry.entry(B, C2, A) {actuator.act("5")},
        ), Start)

        verifier
            .verifyState(Start)
            .verifyState(Start)

            .submit(C1)
            .and { verify(actuator).act("1") }
            .and { reset(actuator) }
            .verifyState(A)


            .submit(C1)
            .and { verify(actuator).act("2") }
            .and { reset(actuator) }
            .verifyState(B)


            .submit(C1)
            .and { verify(actuator).act("3") }
            .and { reset(actuator) }
            .verifyState(Start)


            .submit(C1)
            .and { verify(actuator).act("1") }
            .and { reset(actuator) }
            .verifyState(A)

            .submit(C2)
            .and { verify(actuator).act("4") }
            .and { reset(actuator) }
            .verifyState(Start)

    }
}
package coden.fsm

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.times

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
            Entry.entry(B, C1, Start) {actuator.act("3"); actuator.act("3")},
            Entry.entry(A, C2, Start) {actuator.act("4")},
            Entry.entry(B, C2, A) {actuator.act("5")},
        ), Start)

        verifier
            .verifyState(Start)
            .verifyState(Start)

            .submit(C1)
            .verify(actuator) { act("1") }
            .verifyState(A)


            .submit(C1)
            .verify(actuator) { act("2") }
            .verifyState(B)


            .submit(C1)
            .verify(actuator, times(2)) { act("3") }
            .verifyState(Start)


            .submit(C1)
            .verify(actuator) { act("1") }
            .verifyState(A)

            .submit(C2)
            .verify(actuator) { act("4") }
            .verifyState(Start)

    }
}
package coden.fsm

import org.junit.jupiter.api.Assertions.assertEquals
import org.mockito.Mockito
import org.mockito.verification.VerificationMode

class FSMVerifier(table: FSMTable, start: State) {

    private val executor = StateBasedCommandExecutor(FSM(start, table))

    fun verifyState(state: State): FSMVerifier = apply {
        assertEquals(state, executor.current)
    }

    fun submit(command: Command) = apply {
        executor.submit(command)
    }

    fun and(apply: () -> Unit) = apply { apply()  }

    fun <T> verify(mock: T, verification: T.() -> Unit) = apply {
        verification(Mockito.verify(mock)!!)
        Mockito.reset(mock)
    }

    fun <T> verify(mock: T, mode: VerificationMode, verification: T.() -> Unit) = apply {
        verification(Mockito.verify(mock, mode)!!)
        Mockito.reset(mock)
    }
}
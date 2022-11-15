package coden.fsm

import org.junit.jupiter.api.Assertions.assertEquals
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.verification.VerificationMode

class FSMVerifier(table: FSMTable, start: State) {

    private val executor = StateBasedCommandExecutor(FSM(start, table))

    private val mocks = ArrayList<Any>()

    fun verifyState(state: State): FSMVerifier = apply {
        mocks.forEach { Mockito.clearInvocations(it) }
        mocks.clear()
        assertEquals(state, executor.current)
    }

    fun submit(command: Command) = apply {
        executor.submit(command)
    }

    fun and(apply: () -> Unit) = apply { apply()  }

    fun <T : Any> verify(mock: T, verification: T.() -> Unit) = apply {
        mocks.add(mock)
        verification(Mockito.verify(mock)!!)
    }

    fun <T : Any> verify(mock: T, mode: VerificationMode, verification: T.() -> Unit) = apply {
        mocks.add(mock)
        verification(Mockito.verify(mock, mode)!!)
    }
}
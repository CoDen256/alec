package coden.alec.app.util

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class UtilsKtTest {

    @Test
     fun format() {
        assertEquals("Scales: \n\n1", "Scales: \n\n{scales}".format("scales" to  "1"))
    }

    @Test
    fun flatMapSuccessSuccess() {
        val result = Result.success(1).flatMap {
            Result.success(it + 2)
        }
        assertEquals(3, result.getOrThrow())
    }

    @Test
    fun flatMapFailureSuccess() {
        val result = Result.failure<Int>(IllegalStateException()).flatMap {
            Result.success(it + 2)
        }
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is IllegalStateException)
    }

    @Test
    fun flatMapSuccessFailure() {
        val result = Result.success(1).flatMap {
            Result.failure<Int>(IllegalStateException())
        }
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is IllegalStateException)
    }


    @Test
    fun flatMapFailureFailure() {
        val result = Result.failure<Int>(IllegalStateException()).flatMap {
            Result.failure<Int>(IllegalStateException())
        }
        assertTrue(result.isFailure)
        assertTrue(result.exceptionOrNull() is IllegalStateException)
    }
}
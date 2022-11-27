package coden.alec.utils

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class UtilsTest{
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
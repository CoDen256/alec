package coden.alec.data

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

class ScaleTest{
    @Test
    fun equal(){
        assertTrue(
            Scale("scale-1", "1", "unit", false, listOf(ScaleDivision(1, "first"))) ==
            Scale("scale-1", "1", "unit", false, listOf(ScaleDivision(1, "first")))
        )
    }
}
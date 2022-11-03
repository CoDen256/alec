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
        assertFalse(
            Scale("scale-1", "1", "unit", true, listOf(ScaleDivision(1, "first"))) ==
                    Scale("scale-1", "1", "unit", false, listOf(ScaleDivision(1, "first")))
        )
        assertFalse(
            Scale("scale-1", "1", "unit", true, listOf(ScaleDivision(1, "first"))) ==
                    Scale("scale-1", "1", "unit", true, listOf(ScaleDivision(1, "first ")))
        )
    }

    @Test
    fun equalDivisions(){
        assertTrue(
            ScaleDivision(1, "first") ==
                    ScaleDivision(1, "first")
        )

        assertTrue(
            ScaleDivision(2, "sec") ==
                    ScaleDivision(2, "sec")
        )
        assertFalse(
            ScaleDivision(2, "sec") ==
                    ScaleDivision(2, "sec ")
        )
    }
}
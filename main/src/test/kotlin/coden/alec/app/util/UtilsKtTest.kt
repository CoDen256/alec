package coden.alec.app.util

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class UtilsKtTest {

    @Test
     fun format() {
        assertEquals("Scales: \n\n1", "Scales: \n\n{scales}".inline("scales" to  "1"))
    }

}
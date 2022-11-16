package coden.alec.app.config.scale

import coden.alec.app.actuators.scale.InvalidScalePropertyFormatException
import org.junit.jupiter.api.Assertions.*

import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class BaseScaleParserTest {

    val sut = BaseScaleParser()

    @Test
    fun parseCreateScaleRequest() {

    }

    @CsvSource(
        "name,name",
        " name,name",
        " name ,name",
        "name a second name,name a second name",
        "'\nname name\t',name name",
        "'',<invalid>",
        " ,<invalid>",
        "'\n',<invalid>",
        "\t,<invalid>",
        "name 3,name 3",
        "  name !  ,name !",
        "' \tname !@#$%^&*() \n\t',name !@#\$%^&*()",
        "'name\n second',name  second",
        "'\t\tname\n\n\tsecond\n\n\n\tagain\n\t','name   second    again'",
        delimiter = ',',
        ignoreLeadingAndTrailingWhitespace = false
    )
    @ParameterizedTest
    fun parseScaleNameAndScaleUnit(input: String, expected: String) {
        verifNameParsedCorrectly(sut.parseScaleName(input), expected)
        verifNameParsedCorrectly(sut.parseScaleUnit(input), expected)
    }

    private fun verifNameParsedCorrectly(parseName: Result<String>, expected: String) {
        if (expected == "<invalid>") {
            assertTrue(parseName.isFailure)
            assertTrue(parseName.exceptionOrNull() is InvalidScalePropertyFormatException)
        } else {
            assertTrue(parseName.isSuccess)
            assertEquals(expected, parseName.getOrThrow())
        }
    }

    @Test
    fun parseScaleDivisions() {
    }
}
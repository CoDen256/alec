package coden.alec.app.config.scale

import coden.alec.app.actuators.scale.InvalidScalePropertyFormatException
import org.junit.jupiter.api.Assertions.*

import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class BaseScaleParserTest {

    private val parser = BaseScaleParser()

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
        val invalidToNull = invalidToNullOr(expected)
        verifyParsedCorrectly(parser.parseScaleName(input), invalidToNull)
        verifyParsedCorrectly(parser.parseScaleUnit(input), invalidToNull)
    }

    private fun invalidToNullOr(expected: String) = invalidToNullOr(expected) {expected}
    private fun <T> invalidToNullOr(expected: String, default: String.() -> T): T? = if (expected == "<invalid>") null else default(expected)

    @CsvSource(
        "1-hello,{1:hello}",
        "2-world,{2:world}",
        "3 - hello,{3:hello}",
        " 4 -  hello,{4:hello}",
        "\t2\t-\t\t\thel\t\tlo\t\t\t,{2:hel  lo}",
        "\t\t       100    \t\t\t - 12344678056($*&!@($*!)@$*&)@!$!_@$(&hello \t\t\t,{100:12344678056($*&!@($*!)@$*&)@!$!_@$(&hello}",
        "\t 1234       \t-1234,{1234:1234}",
        "1-hello,{1:hello}",
        "'6   \n\t-hello',<invalid>",
        "2--hello',<invalid>",
        "-2-hello',<invalid>",
        "2-hel-lo',<invalid>",
        "2-hello-',<invalid>",
        "  -hello,<invalid>",
        "abcd-hello,<invalid>",
        "1234f-hello,<invalid>",

        "'3 - hello\n\t\t\t2- 4 \t\t\t\n\t 1234       \t-12 \t34s',{3:hello;2:4;1234:12  34s}",
        "' 1 -  hello\n2- world\n3- my world\n   1 - oops  ',{1:hello;2:world;3:my world;1:oops}",
        "'2-hello\n3-hello-',<invalid>",
        "'2-hello\nh-hello',<invalid>",
        delimiter = ',',
        ignoreLeadingAndTrailingWhitespace = false
    )
    @ParameterizedTest
    fun parseScaleDivisions(input: String, expected: String) {
        val divisions = invalidToNullOr(expected) {
            drop(1)
                .dropLast(1)
                .split(";")
                .map { it.split(":", limit = 2) }
                .associate { it[0].toLong() to it[1] }
        }
        verifyParsedCorrectly(parser.parseScaleDivisions(input), divisions)
    }

    private fun <T> verifyParsedCorrectly(parseName: Result<T>, expected: T?) {
        if (expected == null) {
            assertTrue(parseName.isFailure)
            assertTrue(parseName.exceptionOrNull() is InvalidScalePropertyFormatException)
        } else {
            assertTrue(parseName.isSuccess)
            assertEquals(expected, parseName.getOrThrow())
        }
    }
}
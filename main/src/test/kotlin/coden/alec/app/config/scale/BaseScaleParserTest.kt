package coden.alec.app.config.scale

import coden.alec.app.actuators.scale.InvalidScaleFormatException
import coden.alec.app.actuators.scale.InvalidScalePropertyFormatException
import coden.alec.interactors.definer.scale.CreateScaleRequest
import coden.alec.interactors.definer.scale.DeleteScaleRequest
import org.junit.jupiter.api.Assertions.*

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class BaseScaleParserTest {

    private val parser = BaseScaleParser()

    @CsvSource(
        "' scale  - 123',scale-123",
        "'\t s c a l e \t-\n123',scale-123",
        delimiter = ',',
        ignoreLeadingAndTrailingWhitespace = false
    )
    @ParameterizedTest
    fun parseScaleId(input: String, expected: String) {
        verifyPropertyParsedCorrectly(parser.parseDeleteScaleRequest(input), DeleteScaleRequest(expected))
    }

    @CsvSource(
        "'name\nunit\n1-div',name;unit;{1:div}",
        "' my \t\t\t  name \t\t\t \n\t some \tunit  \t\n \t1   \t -  some \tdiv   \n 4 - something else ',my      name;some  unit;{1:some  div;4:something else}",
        "'na\nme\nunit\n1-div',<invalid>",
        "'name\nun\nit\n1-div',<invalid>",
        "'name\nunit\n1-di\nv',<invalid>",
        "' \nunit\n1-div',<invalid>",
        "'n\nu\nn-div',<invalid>",
        "'n\nu\n1-div\n123s-div',<invalid>",
        delimiter = ',',
        ignoreLeadingAndTrailingWhitespace = false
    )
    @ParameterizedTest
    fun parseCreateScaleRequest(input: String, expected: String) {
        val scaleRequest = invalidToNullOr(expected){
            val split = expected.split(";", limit = 3)
            val (name, unit, divisions) = Triple(split[0], split[1], split[2])
            CreateScaleRequest(
                name, unit, divisions.parseDivisions()
            )
        }
        verifyScaleParsedCorrectly(parser.parseCreateScaleRequest(input), scaleRequest)
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
        val parsedName = invalidToNullOr(expected)
        verifyPropertyParsedCorrectly(parser.parseScaleName(input), parsedName)
        verifyPropertyParsedCorrectly(parser.parseScaleUnit(input), parsedName)
    }


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
        val divisions = invalidToNullOr(expected) { parseDivisions() }
        verifyPropertyParsedCorrectly(parser.parseScaleDivisions(input), divisions)
    }

    private fun String.parseDivisions() = drop(1)
        .dropLast(1)
        .split(";")
        .map { it.split(":", limit = 2) }
        .associate { it[0].toLong() to it[1] }

    private fun invalidToNullOr(expected: String) = invalidToNullOr(expected) {expected}
    private fun <T> invalidToNullOr(expected: String, default: String.() -> T): T? = if (expected == "<invalid>") null else default(expected)

    private fun <T> verifyPropertyParsedCorrectly(parseName: Result<T>, expected: T?) {
        return verifyParsedCorrectlyWithException<T, InvalidScalePropertyFormatException>(parseName, expected)
    }

    private fun <T> verifyScaleParsedCorrectly(parseName: Result<T>, expected: T?) {
        return verifyParsedCorrectlyWithException<T, InvalidScaleFormatException>(parseName, expected)
    }

    private inline fun <T,reified E> verifyParsedCorrectlyWithException(parseName: Result<T>, expected: T?) {
        if (expected == null) {
            assertTrue(parseName.isFailure)
            assertTrue(parseName.exceptionOrNull() is E)
        } else {
            assertTrue(parseName.isSuccess)
            assertEquals(expected, parseName.getOrThrow())
        }
    }
}
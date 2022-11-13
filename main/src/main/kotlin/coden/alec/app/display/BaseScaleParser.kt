package coden.alec.app.display

import coden.alec.interactors.definer.scale.CreateScaleRequest
import java.util.regex.Pattern

class BaseScaleParser : ScaleParser {

    private val scalePattern = Pattern.compile(
        "" +
                "[A-Za-z0-9_-]{1,10}" +
                "\n[A-Za-z/-]{1,10}" +
                "(\n\\d+-[A-Za-z0-9_-]+)+"
    )

    private val namePattern = Pattern.compile("\\w+")

    private val divisionPattern = Pattern.compile(
        "\\d+-[A-Za-z0-9_-]+" +
                "(\n\\d+-[A-Za-z0-9_-]+)*"
    )

    override fun parseCreateScaleRequest(input: String): CreateScaleRequest {
        input.verify("scale", this::isValidCreateScaleRequest)
//        val (name, unit, divisionString) = collectArgs(command.arguments.getOrThrow())
//        val divisions = HashMap<Long, String>()
//        for (arg in divisionString.split("\n")) {
//            val division = arg.split("-")
//            divisions[division[0].toLong()] = division[1]
//        }
        TODO("Not yet implemented")
    }

    override fun parseCreateScaleRequest(name: String, unit: String, divisions: String): CreateScaleRequest {
        TODO("Not yet implemented")
    }

    override fun parseScaleName(input: String): Result<String> {
        input.verify("scale name", this::isValidScaleName)
        TODO("Not yet implemented")
    }

    override fun parseScaleUnit(input: String): String {
        input.verify("scale unit", this::isValidScaleUnit)
        TODO("Not yet implemented")
    }

    override fun parseScaleDivisions(input: String): Map<String, Long> {
        input.verify("scale divisions", this::isValidDivisions)
        TODO("Not yet implemented")
    }

    override fun isValidCreateScaleRequest(input: String): Boolean {
        return input.matches(scalePattern)
    }

    private fun isValidScaleName(input: String): Boolean {
        return input.matches(namePattern)
    }

    override fun isValidScaleUnit(input: String): Boolean {
        return input.matches(namePattern)
    }

    override fun isValidDivisions(input: String): Boolean {
        return input.matches(divisionPattern)

    }

    private fun String.verify(name: String, check: (String) -> Boolean): String {
        if (!check(this)) throw InvalidScaleFormatException("Invalid format of '$name'")
        return this
    }

    private fun String.matches(pattern: Pattern) = this.matches(pattern.toRegex())

//    private fun collectArgs(args: String): Triple<String, String, String> {
//        if (name != null && unit != null && divisions != null) {
//            return Triple(name!!, unit!!, divisions!!)
//        }
//        val split = args.split("\n", limit = 3)
//        return Triple(split[0], split[1], split[2])
//    }

    class InvalidScaleFormatException(msg: String) : RuntimeException(msg)
}
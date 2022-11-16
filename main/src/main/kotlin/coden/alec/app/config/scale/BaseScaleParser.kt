package coden.alec.app.config.scale

import coden.alec.app.actuators.scale.InvalidScalePropertyFormatException
import coden.alec.app.actuators.scale.ScaleParser
import coden.alec.interactors.definer.scale.CreateScaleRequest
import java.util.regex.Pattern

class BaseScaleParser : ScaleParser {

    private val scalePattern = Pattern.compile(
        "" +
                "[A-Za-z0-9_-]{1,10}" +
                "\n[A-Za-z/-]{1,10}" +
                "(\n\\d+-[A-Za-z0-9_-]+)+"
    )

    private val whitespaceReplacement = Pattern.compile("\\s", Pattern.MULTILINE).toRegex()

    private val divisionPattern = Pattern.compile(
        "\\d+-[A-Za-z0-9_-]+" +
                "(\n\\d+-[A-Za-z0-9_-]+)*"
    )

    override fun parseCreateScaleRequest(input: String): Result<CreateScaleRequest> {
        input.verify("scale", this::isValidCreateScaleRequest)
//        val (name, unit, divisionString) = collectArgs(command.arguments.getOrThrow())
//        val divisions = HashMap<Long, String>()
//        for (arg in divisionString.split("\n")) {
//            val division = arg.split("-")
//            divisions[division[0].toLong()] = division[1]
//        }
        TODO("Not yet implemented")
    }


    override fun parseScaleName(input: String): Result<String> {
        return input.verify("scale name", this::isValidScaleName).map {
                it.trim().replace(whitespaceReplacement, " ")
            }
    }

    override fun parseScaleUnit(input: String): Result<String> {
        input.verify("scale unit", this::isValidScaleUnit)
        TODO("Not yet implemented")
    }

    override fun parseScaleDivisions(input: String): Result<Map<Long, String>> {
        input.verify("scale divisions", this::isValidDivisions)
        TODO("Not yet implemented")
    }

    private fun isValidCreateScaleRequest(input: String): Boolean {
        return input.matches(scalePattern)
    }

    private fun isValidScaleName(input: String): Boolean {
        return input.isNotBlank()
    }

    private fun isValidScaleUnit(input: String): Boolean {
        return input.isNotBlank()
    }

    private  fun isValidDivisions(input: String): Boolean {
        return input.matches(divisionPattern)

    }

    private fun String.verify(name: String, check: (String) -> Boolean): Result<String> {
        if (!check(this)) return Result.failure(InvalidScalePropertyFormatException(name, this))
        return Result.success(this)
    }

    private fun String.matches(pattern: Pattern) = this.matches(pattern.toRegex())

//    private fun collectArgs(args: String): Triple<String, String, String> {
//        if (name != null && unit != null && divisions != null) {
//            return Triple(name!!, unit!!, divisions!!)
//        }
//        val split = args.split("\n", limit = 3)
//        return Triple(split[0], split[1], split[2])
//    }
}
package coden.alec.app.config.scale

import coden.alec.app.actuators.scale.InvalidScaleFormatException
import coden.alec.app.actuators.scale.InvalidScalePropertyFormatException
import coden.alec.app.actuators.scale.ScaleParser
import coden.alec.interactors.definer.scale.CreateScaleRequest
import coden.alec.interactors.definer.scale.DeleteScaleRequest
import coden.alec.interactors.definer.scale.PurgeScaleRequest
import java.util.regex.Pattern

class BaseScaleParser : ScaleParser {

    private val whitespaceReplacement = Pattern.compile("\\s", Pattern.MULTILINE).toRegex()


    override fun parseCreateScaleRequest(input: String): Result<CreateScaleRequest> {
        return Result.success(input)
            .mapCatching { parseScaleRequest(it) }
            .recoverCatching { throw InvalidScaleFormatException(input) }
    }

    private fun parseScaleRequest(it: String): CreateScaleRequest {
        val split = it.split("\n", limit = 3)
        val name = split[0]
        val unit = split[1]
        val divisions = split[2]
        return CreateScaleRequest(
            parseScaleName(name).getOrThrow(),
            parseScaleUnit(unit).getOrThrow(),
            parseScaleDivisions(divisions).getOrThrow()
        )
    }


    override fun parseScaleName(input: String): Result<String> {
        return input.verify("scale name", this::isValidScaleName).map {
            parseName(it)
            }
    }

    override fun parseScaleUnit(input: String): Result<String> {
        return input.verify("scale unit", this::isValidScaleUnit).map {
            parseName(it)
        }
    }

    private fun parseName(it: String) = it.trim().replace(whitespaceReplacement, " ")

    override fun parseScaleDivisions(input: String): Result<Map<Long, String>> {
        return Result.success(input)
            .mapCatching { parseDivisions(it) }
            .recoverCatching { throw InvalidScalePropertyFormatException("scale divisions", input) }
    }

    override fun parseDeleteScaleRequest(input: String): Result<DeleteScaleRequest> {
        return Result.success(DeleteScaleRequest(parseId(input)))
    }

    private fun parseId(input: String) = input.replace(whitespaceReplacement, "")

    override fun parsePurgeScaleRequest(input: String): Result<PurgeScaleRequest> {
        return Result.success(PurgeScaleRequest(parseId(input)))
    }

    private fun parseDivisions(it: String): Map<Long, String> =
        it.split("\n")
            .map { verifyDivision(it); it }
            .map { l -> l.split("-", limit = 2) }
            .associate { parseName(it[0]).toLong() to parseName(it[1].trim())
        }

    private fun verifyDivision(input: String){
        if (input.count {  it == '-' } != 1) throw IllegalArgumentException()
    }

    private fun isValidScaleName(input: String): Boolean {
        return isValidName(input)
    }

    private fun isValidScaleUnit(input: String): Boolean {
        return isValidName(input)
    }

    private fun isValidName(input: String) = input.isNotBlank()


    private fun String.verify(name: String, check: (String) -> Boolean): Result<String> {
        if (!check(this)) return Result.failure(InvalidScalePropertyFormatException(name, this))
        return Result.success(this)
    }
}
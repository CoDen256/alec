package coden.alec.app.display

interface ScaleParser {
    fun parseCreateScaleRequest(input: String): Result<ParsedScaleRequest>
    fun parseScaleName(input: String): Result<String>
    fun parseScaleUnit(input: String): Result<String>
    fun parseScaleDivisions(input: String): Result<Map<Long, String>>
}

data class ParsedScaleRequest(
    val parsedName: String,
    val parsedUnit: String,
    val parsedDivisions: Map<Long, String>
)